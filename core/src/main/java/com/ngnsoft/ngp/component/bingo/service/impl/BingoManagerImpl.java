package com.ngnsoft.ngp.component.bingo.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.bingo.BingoComponent;
import com.ngnsoft.ngp.component.bingo.RobotService;
import com.ngnsoft.ngp.component.bingo.model.Activity;
import com.ngnsoft.ngp.component.bingo.model.ActivityData;
import com.ngnsoft.ngp.component.bingo.model.BingoRobot;
import com.ngnsoft.ngp.component.bingo.service.BingoManager;
import com.ngnsoft.ngp.component.service.impl.ComponentManagerImpl;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.ConvertUtil;
import com.ngnsoft.ngp.util.RedisUtil;

/**
 *
 * @author fcy
 */
@Service
public class BingoManagerImpl extends ComponentManagerImpl implements BingoManager {
	
	private Logger LOGGER = Logger.getLogger(BingoManagerImpl.class);

    private static final String PARAM_DATA = "data";
    private static final String PARAM_CARD_NUM = "cnum";
    private static final String PARAM_LAST_TIME_KEY = "time";
    public static final String BINGO_REDIS_PREFIX = BingoComponent.APP_BASE_ID + "_BINGO_";
    //bingo start time
    public static final String BINGO_START_TIME_REDIS_PREFIX = BINGO_REDIS_PREFIX + "TIME_";
    //total number of the card
    public static final String BINGO_CNUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "CNUM_";
    //total number of the ready user
    public static final String BINGO_READY_NUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "RNUM_";
    //total number of the left bingo
    public static final String BINGO_NUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "NUM_";
    //the top list key
    public static final String BINGO_TOP_LIST_REDIS_PREFIX = BINGO_REDIS_PREFIX + "TOPLIST_";
    //the user with bingo key
    public static final String BINGO_ROLE_DATA_REDIS_PREFIX = BINGO_REDIS_PREFIX + "ROLE_DATA_";
    //all of the users in the room
    public static final String BINGO_MEMBER_DATA_REDIS_PREFIX = BINGO_REDIS_PREFIX + "MEMBER_DATA_";
    //list of the ready user
    public static final String BINGO_READY_MEMBER_REDIS_PREFIX = BINGO_REDIS_PREFIX + "R_MEMBER_DATA_";
    //list of the validate user
    public static final String BINGO_VALID_MEMBER_REDIS_PREFIX = BINGO_REDIS_PREFIX + "V_MEMBER_DATA_";
    //all of the activities in the room
    public static final String BINGO_ACTIVITY_REDIS_PREFIX = BINGO_REDIS_PREFIX + "ACTIVITY_";
    //all of the chats in the room
    public static final String BINGO_CHAT_REDIS_PREFIX = BINGO_REDIS_PREFIX + "CHAT_";
    //the digits queue for every game
    public static final String BINGO_QUEUE_REDIS_PREFIX = BINGO_REDIS_PREFIX + "QUEUE_";
    
    @Autowired
    @Qualifier("redisImpl")
    private RedisImpl redisImpl;
    
    @Override
    public Response count(App app, Request req) throws Exception {
    	Set<String> set = redisImpl.keys("*" + BingoManagerImpl.BINGO_MEMBER_DATA_REDIS_PREFIX + "*");
    	JSONArray array = new JSONArray();
		for(String key : set) {
			JSONObject room = new JSONObject();
			Long num = redisImpl.zCountByScore(key, 0, Long.MAX_VALUE);
			String gid = key.substring(BingoManagerImpl.BINGO_MEMBER_DATA_REDIS_PREFIX.length());
			room.put(gid, num);
			array.put(room);
		}
    	return new Response(Response.YES, array);
    }

    @Override
    public Response join(App app, String gid, Request req) throws Exception {
        if (!req.getJSONObject(BingoComponent.CMD_JOIN).has(PARAM_DATA)) {
            return new Response(Response.NO, "ERR_" + PARAM_DATA);
        }
        String data = req.getJSONObject(BingoComponent.CMD_JOIN).getJSONObject(PARAM_DATA).toString();
        Long rid = req.getUserId();
        joinGame(rid, gid, data);
        JSONObject resultObj = new JSONObject();
        JSONArray memberArray = new JSONArray();
        Set<byte[]> dataSet = redisImpl.zRange(BINGO_MEMBER_DATA_REDIS_PREFIX + gid);
        for (byte[] dataByte : dataSet) {
            Activity entity = (Activity) RedisUtil.getObjectFromBytes(dataByte);
            ActivityData jsonData = new ActivityData();
            jsonData.setRid(entity.getRid());
            jsonData.setTime(entity.getTime());
            jsonData.setType(entity.getType());
            jsonData.setData(new JSONObject(entity.getData()));
            memberArray.put(new JSONObject(jsonData));

        }
        resultObj.put("data", memberArray);
        resultObj.put("rid", req.getUserId());
        return new Response(Response.YES, resultObj);
    }

    @Override
    public Response ready(App app, String gid, Request req) throws Exception {
        //请求参数，cnum，卡牌数量，返回当前准备人数及卡牌数量
        if (!req.getJSONObject(BingoComponent.CMD_READY).has(PARAM_CARD_NUM)) {
            return new Response(Response.NO, "ERR_" + PARAM_CARD_NUM);
        }
        Long cardNum = req.getJSONObject(BingoComponent.CMD_READY).getLong(PARAM_CARD_NUM);
        long gameStartTime = (Long) redisImpl.get(BINGO_START_TIME_REDIS_PREFIX + gid);
        long curMillis = System.currentTimeMillis();
        if (gameStartTime > curMillis || (curMillis - gameStartTime < 20000)) {
        	
        	redisImpl.zadd(BINGO_READY_MEMBER_REDIS_PREFIX + gid, req.getUserId(), cardNum);
//        	if(redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, req.getUserId(), req.getUserId()) == 1) {
        		redisImpl.zadd(BINGO_VALID_MEMBER_REDIS_PREFIX + gid, req.getUserId(), cardNum);
        		System.out.println("valid count++" + redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, req.getUserId(), req.getUserId()) + " successful ++++++++++++++++++++++++++++");
        		JSONObject resultObject = new JSONObject();
                Long totalCardNum = redisImpl.incr(BINGO_CNUM_REDIS_PREFIX + gid, cardNum);
                Long totalReadyNum = redisImpl.incr(BINGO_READY_NUM_REDIS_PREFIX + gid, 1);
                String queue = (String) redisImpl.get(BINGO_QUEUE_REDIS_PREFIX + gid);
                resultObject.put("bnum", calTotalBingoNum(totalReadyNum));
                resultObject.put("cnum", totalCardNum);
                resultObject.put("rnum", totalReadyNum);
                resultObject.put("queue", queue);
                return new Response(Response.YES, resultObject);
//        	} else {
//        		cardNum = 0l;
//        		JSONObject resultObject = new JSONObject();
//                Long totalCardNum = redisImpl.incr(BINGO_CNUM_REDIS_PREFIX + gid, 0);
//                String queue = (String) redisImpl.get(BINGO_QUEUE_REDIS_PREFIX + gid);
//                Long totalReadyNum = redisImpl.incr(BINGO_READY_NUM_REDIS_PREFIX + gid, 0);
//                resultObject.put("bnum", calTotalBingoNum(totalReadyNum));
//                resultObject.put("cnum", totalCardNum);
//                resultObject.put("rnum", totalReadyNum);
//                resultObject.put("queue", queue);
//                return new Response(Response.YES, resultObject);
//        	}
        } else {
            return new Response(Response.NO, "GAME_STARTED");
        }
    }
    
    private static long calTotalBingoNum(long totalReadyNum) {
        long tbn = totalReadyNum / 3;
        if (tbn < 1) {
        	if(totalReadyNum > 0) {
        		tbn = 1;
        	} else {
        		tbn = 0;
        	}
        }
        return tbn;
    }
    
    @Override
    public Response bingo(App app, String gid, Request req) throws Exception {
        //请求参数，data,返回是否成功
        if (!req.getJSONObject(BingoComponent.CMD_BINGO).has(PARAM_DATA)) {
            return new Response(Response.NO, "ERR_" + PARAM_DATA);
        }
        String data = req.getJSONObject(BingoComponent.CMD_BINGO).getJSONObject(PARAM_DATA).toString();
        long gameStartTime = (Long) redisImpl.get(BINGO_START_TIME_REDIS_PREFIX + gid);
        System.out.println((System.currentTimeMillis() - gameStartTime) + "--------------------------" + redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, req.getUserId(), req.getUserId()));
        if (gameStartTime < System.currentTimeMillis() /*&& redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, req.getUserId(), req.getUserId()) > 0*/) {
        	long bingoNum = redisImpl.decr(BINGO_NUM_REDIS_PREFIX + gid);
            if (bingoNum < 0) {
            	System.out.println("bingo failed ---------------------------1");
                return new Response(Response.NO, "BINGO_GONE");
            } else {
            	System.out.println("userId: " + req.getUserId() + " bingo successful....left bingo num :" + bingoNum);
                JSONObject resultObject = new JSONObject();
                long curMillis = System.currentTimeMillis();
                redisImpl.zadd(BINGO_TOP_LIST_REDIS_PREFIX + gid, curMillis, req.getUserId());
                redisImpl.set(BINGO_ROLE_DATA_REDIS_PREFIX + req.getUserId(), data);
                if (bingoNum == 0) {
                	LOGGER.error("game over");
                    onGameOver(gid);
                }
                resultObject.put("bnum", bingoNum);
                return new Response(Response.YES, resultObject);
            }
        } else {
        	
        	System.out.println("bingo failed ---------------------------2");
        	return new Response(Response.NO, "BINGO_GONE");
        }
    }
    
    @Override
    public synchronized void onGameOver(String gid) throws Exception {
    	redisImpl.del(BINGO_READY_MEMBER_REDIS_PREFIX + gid);
        redisImpl.del(BINGO_READY_NUM_REDIS_PREFIX + gid);
        redisImpl.del(BINGO_CNUM_REDIS_PREFIX + gid);
        redisImpl.del(BINGO_NUM_REDIS_PREFIX + gid);
        redisImpl.set(BINGO_START_TIME_REDIS_PREFIX + gid, -1, System.currentTimeMillis() + 45 * 1000);
        redisImpl.set(BINGO_QUEUE_REDIS_PREFIX + gid, 0, ConvertUtil.generateRangeQueue(1, 75));
    }

    private synchronized void initGame(String gid) throws Exception {
        redisImpl.setNx(BINGO_START_TIME_REDIS_PREFIX + gid, -1, System.currentTimeMillis() + 45 * 1000);
        redisImpl.setNx(BINGO_QUEUE_REDIS_PREFIX + gid, 0, ConvertUtil.generateRangeQueue(1, 75));
    }

    @Override
    public Response quit(App app, String gid, Request req) {
        if (!req.getJSONObject(BingoComponent.CMD_QUIT).has(PARAM_DATA)) {
            return new Response(Response.NO, "ERR_" + PARAM_DATA);
        }
        String data = req.getJSONObject(BingoComponent.CMD_QUIT).getJSONObject(PARAM_DATA).toString();
        if(quit(gid, req.getUserId(), data)){
        	return new Response(Response.YES);
        } else {
        	return new Response(Response.NO, "HAD_READY");
        }
    }
    
    public boolean quit(String gid, Long rid, String data) {
        long curMillis = System.currentTimeMillis();
        Activity a = new Activity();
        a.setType(Activity.TYPE_EXIT);
        a.setTime(curMillis);
        a.setRid(rid);
        a.setData(data);
        redisImpl.zadd(BINGO_ACTIVITY_REDIS_PREFIX + gid, curMillis, a);
        long gameStartTime;
		try {
			gameStartTime = (Long) redisImpl.get(BINGO_START_TIME_REDIS_PREFIX + gid);
			Long count = redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, rid, rid);
			Set<byte[]> cardBytes = redisImpl.zRangeByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, rid, rid);
			if(count != 0) {
				if (gameStartTime >= System.currentTimeMillis()) {
		        	// user ready and game has no started
				    resetBingoNum(gid, redisImpl.incr(BINGO_READY_NUM_REDIS_PREFIX + gid, -1));
				    if(cardBytes != null && cardBytes.size() > 0) {
				    	Long cardNum = (Long) RedisUtil.getObjectFromBytes(cardBytes.iterator().next());
				    	redisImpl.decr(BINGO_CNUM_REDIS_PREFIX + gid, cardNum);
				    }
		        }
			}
		} catch (Exception e) {
		}
        redisImpl.zRemRangeByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, rid, rid);
        redisImpl.zRemRangeByScore(BINGO_MEMBER_DATA_REDIS_PREFIX + gid, rid, rid);
        return true;
    }
    
    public Response over(String gid, Long rid) {
		try {
			Long count = redisImpl.zCountByScore(BINGO_VALID_MEMBER_REDIS_PREFIX + gid, 0, Long.MAX_VALUE);
			System.out.println("--------------------------left ount" + count);
			Long flag = redisImpl.zRemRangeByScore(BINGO_VALID_MEMBER_REDIS_PREFIX + gid, rid, rid);
			if(count == 1 && flag > 0) {
				onGameOver(gid);
			}
		} catch (Exception e) {
		}
		return new Response(Response.YES);
    }

    @Override
    public Response htbt(App app, String gid, Request req) {
        if (!req.getJSONObject(BingoComponent.CMD_HTBT).has(PARAM_LAST_TIME_KEY)) {
            return new Response(Response.NO, "ERR_" + PARAM_LAST_TIME_KEY);
        }
        Long lastTimeStamp = req.getJSONObject(BingoComponent.CMD_HTBT).getLong(PARAM_LAST_TIME_KEY);
        JSONObject jo = getGameDataIncr(req, gid, lastTimeStamp);
        return new Response(Response.YES, jo);
    }

    @Override
    public Response chat(App app, String gid, Request req) {
        if (!req.getJSONObject(BingoComponent.CMD_CHAT).has(PARAM_DATA)) {
            return new Response(Response.NO, "ERR_" + PARAM_DATA);
        }
        String data = req.getJSONObject(BingoComponent.CMD_CHAT).getJSONObject(PARAM_DATA).toString();
        long curMillis = System.currentTimeMillis();
        Activity a = new Activity();
        a.setType(Activity.TYPE_CHAT);
        a.setTime(curMillis);
        a.setRid(req.getUserId());
        a.setData(data);
        redisImpl.zadd(BINGO_CHAT_REDIS_PREFIX + gid, curMillis, a);
        return new Response(Response.YES);
    }

    private JSONObject getGameDataIncr(Request req, String gid, Long lastTimeStamp) {
        JSONObject wrapperData = new JSONObject();

        Set<byte[]> activitySet = redisImpl.zRangeByScore(BINGO_ACTIVITY_REDIS_PREFIX + gid, lastTimeStamp, new Date().getTime());
        Set<byte[]> messageSet = redisImpl.zRangeByScore(BINGO_CHAT_REDIS_PREFIX + gid, lastTimeStamp, new Date().getTime());
        Map<Long, Integer> userStatusMap = new HashMap<Long, Integer>();
        List<ActivityData> activities = new ArrayList<ActivityData>();
        List<ActivityData> chats = new ArrayList<ActivityData>();
        List<Map> topList = new ArrayList<Map>();
        for (byte[] activityByte : activitySet) {
            Activity entity = (Activity) RedisUtil.getObjectFromBytes(activityByte);
            ActivityData jsonData = new ActivityData();
            jsonData.setRid(entity.getRid());
            jsonData.setTime(entity.getTime());
            jsonData.setType(entity.getType());
            if(entity.getData() != null) {
            	try {
					jsonData.setData(new JSONObject(entity.getData()));
				} catch (JSONException e) {
					Set<byte[]> datas = redisImpl.zRangeByScore(RobotService.BINGO_ROBOT_LIST_ALL_REDIS_KEY, entity.getRid(), entity.getRid());
					if(datas.size() > 0) {
						BingoRobot robot = (BingoRobot) RedisUtil.getObjectFromBytes(datas.iterator().next());
						jsonData.setData(new JSONObject(robot.getData()));
					}
				}
            }
            if (entity.getType() == Activity.TYPE_EXIT) {
                if (userStatusMap.containsKey(entity.getRid())) {
                    userStatusMap.put(entity.getRid(), userStatusMap.get(entity.getRid()) + -1);
                } else {
                    userStatusMap.put(entity.getRid(), -1);
                }
            } else {
                if (userStatusMap.containsKey(entity.getRid())) {
                    userStatusMap.put(entity.getRid(), userStatusMap.get(entity.getRid()) + 1);
                } else {
                    userStatusMap.put(entity.getRid(), 1);
                }
            }
            if (userStatusMap.get(entity.getRid()) == 0) {
                activities.remove(jsonData);
            } else {
                activities.add(jsonData);
            }
        }
        wrapperData.put("activity", activities);
        for (byte[] messageByte : messageSet) {
            Activity entity = (Activity) RedisUtil.getObjectFromBytes(messageByte);
            ActivityData jsonData = new ActivityData();
            jsonData.setRid(entity.getRid());
            jsonData.setTime(entity.getTime());
            jsonData.setType(entity.getType());
            jsonData.setData(new JSONObject(entity.getData()));
            chats.add(jsonData);
        }
        wrapperData.put("chat", chats);
        JSONObject gameData = new JSONObject();

        try {
            Long totalCardNum = redisImpl.incr(BINGO_CNUM_REDIS_PREFIX + gid, 0);
            Long totalReadyNum = redisImpl.incr(BINGO_READY_NUM_REDIS_PREFIX + gid, 0);
            Long gameStartTime = (Long)redisImpl.get(BINGO_START_TIME_REDIS_PREFIX + gid);
            Long bingNum = 0l;
            if(!redisImpl.exist(BINGO_NUM_REDIS_PREFIX + gid)) {
            	if(gameStartTime < System.currentTimeMillis()) {
            		bingNum = resetBingoNum(gid, totalReadyNum);
            	} else {
            		bingNum = calTotalBingoNum(totalReadyNum);
            	}
            } else {  
            	bingNum = redisImpl.incr(BINGO_NUM_REDIS_PREFIX + gid, 0);
            }
            
            Long totalNum = redisImpl.zCountByScore(BINGO_MEMBER_DATA_REDIS_PREFIX + gid, 0, Long.MAX_VALUE);
            
            gameData.put("bnum", bingNum);
            gameData.put("cnum", totalCardNum);
            gameData.put("rnum", totalReadyNum);
            gameData.put("tnum", totalNum);
            gameData.put("time", gameStartTime);
            wrapperData.put("game", gameData);

            Set<byte[]> userIdSet = redisImpl.zRangeByScore(BINGO_TOP_LIST_REDIS_PREFIX + gid, lastTimeStamp, new Date().getTime());
            for (byte[] userIdByte : userIdSet) {
                Long userId = (Long) RedisUtil.getObjectFromBytes(userIdByte);
                String data = (String) redisImpl.get(BINGO_ROLE_DATA_REDIS_PREFIX + userId);
                Map dataMap = new HashMap();
                dataMap.put("userId", userId);
                dataMap.put("data", new JSONObject(data));
                topList.add(dataMap);
            }
            wrapperData.put("topList", topList);
            String queue = (String) redisImpl.get(BINGO_QUEUE_REDIS_PREFIX + gid);
            wrapperData.put("queue", queue);
        } catch (Exception e) {
            e.printStackTrace();
        }


        return wrapperData;
    }
    
    private synchronized Long resetBingoNum(String gid, Long readyNum) throws Exception {
    	redisImpl.del(BINGO_NUM_REDIS_PREFIX + gid);
    	Long bingNum = redisImpl.incr(BINGO_NUM_REDIS_PREFIX + gid, calTotalBingoNum(readyNum));
    	return bingNum;
    }

    public void joinGame(Long rid, String gid, String data) throws Exception {
        long curMillis = System.currentTimeMillis();
        redisImpl.setNx(BINGO_START_TIME_REDIS_PREFIX + gid, -1, curMillis + 45 * 1000);
        Activity a = new Activity();
        a.setType(Activity.TYPE_JOIN);
        a.setTime(curMillis);
        a.setRid(rid);
        a.setData(data);
        if (redisImpl.zRangeByScore(BINGO_MEMBER_DATA_REDIS_PREFIX + gid, rid, rid).size() > 0) {
            redisImpl.zRemRangeByScore(BINGO_MEMBER_DATA_REDIS_PREFIX + gid , rid, rid);
        } else {
            redisImpl.zadd(BINGO_ACTIVITY_REDIS_PREFIX + gid, curMillis, a);
        }
        redisImpl.zadd(BINGO_MEMBER_DATA_REDIS_PREFIX + gid, rid, a);
    }

    @Override
    public void checkGame(App app, String gid) throws Exception {
        if (!redisImpl.exist(BINGO_START_TIME_REDIS_PREFIX + gid)) {
            initGame(gid);
        } else {
            Long startTime = (Long) redisImpl.get(BINGO_START_TIME_REDIS_PREFIX + gid);
            Long nw = System.currentTimeMillis();
            if(startTime > nw) {
            	System.out.println((nw - startTime) / 1000 + " _-------------------------------" + gid);
            }
            System.out.println();  
            if(Math.abs(startTime - nw) <= 3000) {
            	redisImpl.del(BINGO_TOP_LIST_REDIS_PREFIX + gid);
            }
            if (nw >= (startTime + 75 * 5 * 1000)) {
                onGameOver(gid);
            } else if (nw >= startTime) {
            	long readyNum = redisImpl.zCountByScore(BINGO_READY_MEMBER_REDIS_PREFIX + gid, 0, Long.MAX_VALUE);
            	if(redisImpl.exist(BINGO_NUM_REDIS_PREFIX + gid)) {
                	if (startTime < nw && readyNum == 0) {
                        onGameOver(gid);
                    }
                } else if(readyNum == 0) {
                	onGameOver(gid);
                }
            }
        }
    }
    
    public static void main(String[] args) {
		System.out.println(calTotalBingoNum(7));
	}
    
}
