package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.BattleLog;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.misc.PushNotify;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Notification;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.AsyncOperManager;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.UserDataManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;


@SuppressWarnings("unchecked")
@Service("userDataManager")
public class UserDataManagerImpl extends UserAppDataManagerImpl implements UserDataManager {
	
	private Logger LOGGER = Logger.getLogger(UserDataManagerImpl.class);

	private static final String SHIELD_END_TIME_KEY = "Shield";

	private static final String TROPS_FIELD_KEY = "Cup";
	
	private static final String GEM_FIELD_KEY = "Gem";

	private static final int TROPS_INTERVAL = 100;

	private static final int TIME_OUT_INTERVAL = 5; //unit minute

	private static final String BATTLE_DATA_KEY = "BattleData";

	private static final String BATTLE_REPORT_TYPE_KEY = "type";

	private static final String RIVAL_WRAPPER_DATA_KEY = "RivalData";
	
	private static final String BATTLE_DURATION_DATA_KEY = "duration";

	private static final String BATTLE_RIVAL_DATA_KEY = "RivalData";

	private static final String BATTLE_BASE_DATA_KEY = "BaseData";

	private static final String BATTLE_VIDEO_DATA_KEY = "VideoData";

	private static final String RIVAL_USER_DATA_KEY = "UserData";

	private static final String RIVAL_USER_ID_KEY = "userId";

	public static final String USER_INFO_KEY = "Key00000";

	public static final String USER_COIN_KEY = "Gold";
	
	public static final String USER_GEM_KEY = "Gem";

	public static final String USER_LEVEL_KEY = "Level";

	private static final String USER_TOWN_LEVEL_KEY = "TownHall";

	private static final String REVENGE_BATTLE_ID_KEY = "battleId";

	private static final String BATTLE_ARRAY_KEY = "battles";
	
	public static final String USER_NICK_KEY = "NickName";
	
	public static final int SRH_SYNC_WAIT_TIME_OUT = 10; //unit:second
	
	public static final String SRH_ASSIGN_RIVAL_ID_KEY = "rivalId"; 
	
	public static final String SRH_CONCURRENCY_COUNT_KEY = "coc_srh_concurrency_count";
	
	public static final String SRH_CONCURRENCY_SUCC_COUNT_KEY = "coc_srh_concurrency_succ_count";
	
	public static final String SRH_CONCURRENCY_FAILED_COUNT_KEY = "coc_srh_concurrency_failed_count";
	
	public static final int TEMP_SRH_USER_TIME_OUNT = 30;
	
	public static final int MARK_SRH_USER_TIME_OUNT = 60;
	
	@Autowired
	private DataManager dataManager;
	
	@Autowired
	private RedisImpl redisImpl;
	
	@Value("${user.attacked.notification.prefix}")
	private String notificationPrefix;
	
	@Autowired
	private AsyncOperManager asyncOperManager;

	@Override
	public Response executeUserAppDataByAction(Request req, App app) throws JSONException {
		Response res = null;
		JSONObject jo = req.getBizData();
		BaseModel baseData = get(req.getUserId(), UserData.class);
		UserData userData = baseData == null ? null : (UserData)baseData;

		if(jo.has(CocComponent.REQ_SEARCH_ACTION)) {
			//search target
			if(userData != null) {
				Integer deduct;
				try {
					deduct = jo.getJSONObject(CocComponent.REQ_SEARCH_ACTION).getInt(CocComponent.SEARCH_DEDUCT_KEY);
				} catch (Exception e) {
					//get deduct ERROR
					return new Response(Response.NO, "NO_deduct");
				}
				Long rivalId = jo.getJSONObject(CocComponent.REQ_SEARCH_ACTION).has(SRH_ASSIGN_RIVAL_ID_KEY) ? jo.getJSONObject(CocComponent.REQ_SEARCH_ACTION).getLong(SRH_ASSIGN_RIVAL_ID_KEY) : 0l;
				JSONArray appDataKeys = new JSONArray();
				appDataKeys.put("all");
				if (!req.getJSONObject(CocComponent.REQ_SEARCH_ACTION).has("appData")) {
					jo.getJSONObject(CocComponent.REQ_SEARCH_ACTION).put("appData", appDataKeys);
				}
				if(userData.getCoin() >= deduct) {
					JSONObject searchJsonObj = null;
					if(rivalId == 0) {
						searchJsonObj = searchTarget(userData, deduct, jo.getJSONObject(CocComponent.REQ_SEARCH_ACTION));
					} else {
						searchJsonObj = searchAssignRivalTarget(userData, deduct, rivalId);
					}
					
					if(searchJsonObj == null) {
						res = new Response(Response.NO, "NO_RESULT");
					} else {
						res = new Response(Response.YES, searchJsonObj);
					}
				} else {
					res = new Response(Response.NO, "COIN_LOCK");
				}
			} else {
				res = new Response(Response.NO, "NO_USER");
			}
		} else if(jo.has(CocComponent.REQ_RESULT_ACTION)) {
			//upload battle result
			if(userData != null) {
				if (jo.has(CocComponent.REQ_RESULT_ACTION)) {
					boolean flag = true;
		            if(jo.has(CocComponent.REQ_RESULT_TM_KEY)) {
		            	Long tm = jo.getLong(CocComponent.REQ_RESULT_TM_KEY);
		                BattleLog searchlog = new BattleLog();
		                searchlog.setAttacker(userData.getUserId());
		                searchlog.setTm(tm);
		                Long count = dataManager.countTotalNum(searchlog);
		                if(count == 0) {
		                	flag = true;
		                } else {
		                	return new Response(Response.NO, "DUPL_DATA");
		                }
		            } else {
		            	flag = true;
		            }
		            if(flag) {
		            	JSONObject resultJson = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION);
	    				if(resultJson.length() == 1 && jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).has(RIVAL_USER_ID_KEY)) {
	    					Long rivalId = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getLong(RIVAL_USER_ID_KEY);
	    					JSONObject resultObj = unlockRival(userData, rivalId);
	    					res = new Response(Response.YES, resultObj);
	    				} else {
	    					if(!jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).has(BATTLE_REPORT_TYPE_KEY)) {
	    						res = new Response(Response.NO, "NO_type");
	    					} else if(!jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).has(BATTLE_DURATION_DATA_KEY)) {
	    						res = new Response(Response.NO, "NO_duration");
	    					} else {
	    						Long type = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).has(BATTLE_REPORT_TYPE_KEY) ? jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getLong(BATTLE_REPORT_TYPE_KEY) : 0l;
	    						Long duration = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getLong(BATTLE_DURATION_DATA_KEY);
	    						Long battleId = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).has(REVENGE_BATTLE_ID_KEY) ? jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getLong(REVENGE_BATTLE_ID_KEY) : 0l;
	    						if(type == 1 && battleId == 0) {
	    							res = new Response(Response.NO, "NO_battleId");
	    						} else {
	    							JSONObject battleData = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getJSONObject(BATTLE_DATA_KEY);
	    							JSONObject rivalJsonData = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getJSONObject(RIVAL_WRAPPER_DATA_KEY).getJSONObject(RIVAL_USER_DATA_KEY);
	    							JSONObject userJsonData = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getJSONObject(USER_INFO_KEY);
	    							Long rivalId = jo.getJSONObject(CocComponent.REQ_RESULT_ACTION).getJSONObject(RIVAL_WRAPPER_DATA_KEY).getLong(RIVAL_USER_ID_KEY);

	    							JSONObject resultObj = uploadBattleLog(type, battleId, userData, rivalId, duration, battleData, rivalJsonData, userJsonData, app);
	    							res = new Response(Response.YES, resultObj);
	    						}
	    					}
	    				}
		            }
		        }
			} else {
				res = new Response(Response.NO, "NO_USER");
			}
		} else if(jo.has(CocComponent.REQ_LOG_ACTION)){
			// get battle_log by page 
			if(userData != null) {
				Long type = jo.getJSONObject(CocComponent.REQ_LOG_ACTION).has("type") ? jo.getJSONObject(CocComponent.REQ_LOG_ACTION).getLong("type") : 0l;
				JSONObject battles = getBattleList(type, userData);
				res = new Response(Response.YES, battles);
			} else {
				res = new Response(Response.NO, "NO_USER"); 
			}
		} else if(jo.has(CocComponent.REQ_REPLAY_ACTION)) {
			Long battleId = jo.getLong(CocComponent.REQ_REPLAY_ACTION);
			res = getReplayInfo(userData, battleId);
		} else if(jo.has(CocComponent.REQ_REVENGE_ACTION)) {
			Long rivalId =  jo.getJSONObject(CocComponent.REQ_REVENGE_ACTION).getLong("userId");
			Long battleId =  jo.getJSONObject(CocComponent.REQ_REVENGE_ACTION).getLong(REVENGE_BATTLE_ID_KEY);
			if(userData != null) {
				return getRevengeTargetInfo(userData, rivalId, battleId);
			} else {
				res = new Response(Response.NO, "NO_USER");
			}
		} else if(jo.has(CocComponent.REQ_TOP_ACTION)) {
			// get trophy
			res = getUserTop(req, userData);
		} else {
			//del,get,upd, incr
			JSONObject resultObj = getOrUpdateOrDel(jo, userData, req.getUserId(), req.getMacId());
			//upd or del
			res = new Response(Response.YES, resultObj);
		}
		return res;
	}

	private Response getReplayInfo(UserData userData, Long battleId) {
		BattleLog battle = null;
		try {
			battle = (BattleLog)get(battleId, BattleLog.class);
		} catch (Exception e) {
			e.printStackTrace();
			return new Response(Response.NO, "NO_USER");
		}
		JSONObject battleObj = battle.toJSONObject();

		if(battleObj.has(BATTLE_RIVAL_DATA_KEY) && 
				battleObj.getJSONObject(BATTLE_RIVAL_DATA_KEY).has(USER_INFO_KEY)) {
			battleObj.getJSONObject(BATTLE_RIVAL_DATA_KEY).remove(USER_INFO_KEY);
		}
		battleObj.remove(BATTLE_BASE_DATA_KEY);
		battleObj.put(BATTLE_DURATION_DATA_KEY, battle.getDuration());

		//update userData's actionTime
		userData.setActionTime(new Date());
		update(userData);
		battleObj.put(UserData.TOUCH_TIME_KEY, (userData.getTouchTime().getTime()/1000) * 1000);
		return new Response(Response.YES, battleObj);
	}

	private Response getRevengeTargetInfo(UserData userData, Long rivalId, Long battleId) {
		UserData rivalData = (UserData)get(rivalId, UserData.class);
		if(rivalData != null) {
			long touchTime  = rivalData.getTouchTime().getTime();
			long currentTime = System.currentTimeMillis();
			long interval = (currentTime - touchTime) / (1000 * 60);
			if(rivalData.getShieldTime().after(new Date())) {
				return new Response(Response.NO, "USER_PROTECTED");
			} else if(interval < TIME_OUT_INTERVAL) {
				//user is onLine
				return new Response(Response.NO, "USER_ONLINE");
			} else if(rivalData.getSearchLock() == 1) {
				//user was locked
				return new Response(Response.NO, "USER_LOCK");
			} else {
				// TODO synchronized with search target
				Date curDate = new Date();
				
				userData.setLockTime(curDate);
				userData.setActionTime(curDate);
//				userData.setSearchLock(1);
				update(userData);

				rivalData.setLockTime(curDate);
				rivalData.setSearchLock(1);
				update(rivalData);

				rivalData.setTouchTime(userData.getTouchTime());
				JSONObject jsonObj = rivalData.toJSONObject();
				jsonObj.put("userId", rivalData.getUserId());
				jsonObj.put(REVENGE_BATTLE_ID_KEY, battleId);
				return new Response(Response.YES, jsonObj);
			}
		} else {
			return new Response(Response.NO, "NO_USER");
		}
	}

	private JSONObject getBattleList(Long type, UserData userData) {
		JSONObject resultObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		if(type == 0) {
			//get defense battle logs
			BattleLog searchLog = new BattleLog();
			searchLog.setDefenser(userData.getUserId());
			Pagination page = new Pagination();
			page.setCurrent_page(0);
			page.setItems_per_page(10);
			List<BattleLog> battles = findByNamedQuery("findBattleLogWithoutRevenge", searchLog, page, BattleLog.class); 
			int index = 1;
			for(BattleLog battle : battles) {
				JSONObject jsonObj = battle.toJSONObject();
				JSONObject rivalObj = new JSONObject();
				UserData rivalData = get(battle.getAttacker(), UserData.class);
				rivalObj.put("nick", rivalData.getNick());
				rivalObj.put("userId", rivalData.getUserId());
				rivalObj.put("society", "no society");
				if(jsonObj.has(BATTLE_RIVAL_DATA_KEY) && 
						jsonObj.getJSONObject(BATTLE_RIVAL_DATA_KEY).has(USER_INFO_KEY) && 
						jsonObj.getJSONObject(BATTLE_RIVAL_DATA_KEY).getJSONObject(USER_INFO_KEY).has(TROPS_FIELD_KEY)) {
					rivalObj.put(TROPS_FIELD_KEY, jsonObj.getJSONObject(BATTLE_RIVAL_DATA_KEY).getJSONObject(USER_INFO_KEY).getInt(TROPS_FIELD_KEY));
				}
				if(jsonObj.has(BATTLE_VIDEO_DATA_KEY) && index <= 5) {
					jsonObj.put("video", 1);
				} else {
					jsonObj.put("video", 0);
				}
				jsonObj.remove(BATTLE_VIDEO_DATA_KEY);
				jsonObj.remove(BATTLE_RIVAL_DATA_KEY);;
				jsonObj.put(BATTLE_RIVAL_DATA_KEY, rivalObj);

				//type - 1:unrevenge, type-0: revenge
				jsonObj.put("revenge", battle.getType());
				jsonObj.put("battleId", battle.getId());
				jsonObj.put("time", battle.getBattleTime().getTime());
				jsonArray.put(jsonObj);
				index ++;
			}
			resultObj.put(BATTLE_ARRAY_KEY, jsonArray);
			//update userData's touchTime
			userData.setActionTime(new Date());
			update(userData);
			resultObj.put(UserData.TOUCH_TIME_KEY, (userData.getTouchTime().getTime()/1000) * 1000);
		} else {
			// get attack battle log

			//TODO
		}

		return resultObj;
	}
	
	@Transactional(value = "cocTransactionManager")
	private JSONObject searchAssignRivalTarget(UserData userData, Integer deduct, Long rivalId) throws JSONException {
		JSONObject jsonObj = null;
		List<String> blockingQueue = null;
		try {
			blockingQueue = redisImpl.bRPop(SRH_SYNC_WAIT_TIME_OUT, Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY);
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
		String syncValue = null;
		if(blockingQueue != null && blockingQueue.size() > 0) {
			syncValue = blockingQueue.get(1);
			LOGGER.debug("Beginning to search the rival>>>");
		}
		if(Engine.REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE.equals(syncValue)) {
			try {
				Map paramMap = new HashMap();
				paramMap.put("userId", rivalId);
				List<UserData> userDatas = findByNamedQuery("search_assign_target_by_user", paramMap, UserData.class);
				if(userDatas.size() > 0) {
					Random random = new Random();
					UserData result = userDatas.get(random.nextInt(userDatas.size()));
					//lock defenser
//					userData.setSearchLock(1);
					Date curDate = new Date();
					userData.setLockTime(curDate);
					restUserCoin(userData, deduct);
					userData.setActionTime(curDate); 
					update(userData);
					
					//lock attacker
					result.setSearchLock(1);
					result.setLockTime(curDate);
					update(result);
					
					
					result.setTouchTime(userData.getTouchTime());
					jsonObj = result.toJSONObject();
					jsonObj.put("userId", result.getUserId());
					
				}
				//restore srh blockingQueue after searching rival
				redisImpl.rpush(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0, Engine.REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE);
			} catch (Exception e) {
				//restore srh blockingQueue after searching rival
				try {
					redisImpl.rpush(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0, Engine.REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE);
				} catch (Exception e1) {
					LOGGER.error("Redis operate ERROR:", e1);
				}
			}
		}
		return jsonObj;
	}

//	@Transactional(value = "cocTransactionManager")
	private JSONObject searchTarget(UserData userData, Integer deduct, JSONObject srhObject) throws JSONException {
		JSONObject jsonObj = null;
		try {
			long startTime = System.currentTimeMillis();
			redisImpl.incr(SRH_CONCURRENCY_COUNT_KEY, 1);
			Map paramMap = new HashMap();
			paramMap.put("userId", userData.getUserId());
			paramMap.put("lowerLimit", userData.getTrophy() - TROPS_INTERVAL);
			paramMap.put("upperLimit", userData.getTrophy() + TROPS_INTERVAL);
			paramMap.put("timeoutInterval", TIME_OUT_INTERVAL);
			
			paramMap.put("rangeList", getUserIdListFromRedis(userData.getUserId()));
			paramMap.put("sysDate", new Date());
			long countStartTime = System.currentTimeMillis();
			Long count = countByNameQuery("count_target_by_user", paramMap, UserData.class);
			long countEndTime = System.currentTimeMillis();
			List<UserData> userDatas = new ArrayList<UserData>();
			LOGGER.debug(userData.getUserId() + " Count: " + count + ", Time:" + (countEndTime - countStartTime));
			if(count >= 0) {
				paramMap.put("start", count == 0 ? 0 : count - 1);
				userDatas = findByNamedQuery("search_target_by_user", paramMap, UserData.class);
			}
			if(userDatas == null || userDatas.size() == 0) {
				jsonObj = executeSearchUnLimitTrophy(userData, paramMap, deduct, srhObject);
			} else {
				jsonObj = executeSearchWithTrophy(userDatas, userData, paramMap, deduct, srhObject);
			}
			redisImpl.decr(SRH_CONCURRENCY_COUNT_KEY, 1);
			long endTime = System.currentTimeMillis();
			long userId = jsonObj != null ? jsonObj.getLong("userId") : -1;
			if(userId == -1) {
				redisImpl.incr(SRH_CONCURRENCY_FAILED_COUNT_KEY, 1);
			} else {
				redisImpl.incr(SRH_CONCURRENCY_SUCC_COUNT_KEY, 1);
			}
			LOGGER.debug(userData.getUserId() + " srh:" + userId + ", Time:" + (endTime - startTime));
		} catch (Exception e) {
			//restore srh blockingQueue after searching rival
			try {
				redisImpl.decr(SRH_CONCURRENCY_COUNT_KEY, 1);
			} catch (Exception e1) {
				LOGGER.error("Redis operate ERROR:", e1);
			}
		}
		return jsonObj;
	}
	
	private List<Long> getUserIdListFromRedis(Long userId) throws Exception {
		Set<String> lockUserIds = redisImpl.keys("*" + CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + "*");
		Set<String> markUserIds = redisImpl.keys("*" + CocComponent.BE_MARK_SRH_TARGET_ID_PREFIX + userId + "_" + "*");
		List<Long> userIds = new ArrayList<Long>();
		userIds.add(userId);
		if(lockUserIds != null) {
			for(String key : lockUserIds) {
				Long id = (Long)redisImpl.get(key);
				if(id != null) userIds.add(id);
			}
		}
		if(markUserIds != null) {
			for(String key : markUserIds) {
				Long id = (Long)redisImpl.get(key);
				if(id != null && userIds.contains(id)) {
					userIds.add(id);
				}
			}
		}
		return userIds;
	}
	
	private JSONObject executeSearchWithTrophy(List<UserData> userDatas, UserData userData, Map paramMap, int deduct, JSONObject srhObject) throws Exception {
		JSONObject jsonObj = null;
		UserData result = userDatas.get(0);
		String key = CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + result.getUserId();
		if(redisImpl.setNx(key, TEMP_SRH_USER_TIME_OUNT, result.getUserId())) {
			jsonObj = lockTarget(userData, result, deduct, srhObject);
		} else {
			long countStartTime = System.currentTimeMillis();
			Long count = countByNameQuery("count_target_by_user", paramMap, UserData.class);
			long countEndTime = System.currentTimeMillis();
			LOGGER.debug("Count user waste time : " + (countEndTime - countStartTime));
			
			if(count >= 0) {
				paramMap.put("start", count == 0 ? 0 : count - 1);
				userDatas = findByNamedQuery("search_target_by_user", paramMap, UserData.class);
			}
			if(userDatas != null && userDatas.size() > 0) {
				result = userDatas.get(0);
				key = CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + result.getUserId();
				if(redisImpl.setNx(key, TEMP_SRH_USER_TIME_OUNT, result.getUserId())) {
					jsonObj = lockTarget(userData, result, deduct, srhObject);
				} else {
					jsonObj = executeSearchUnLimitTrophy(userData, paramMap, deduct, srhObject);
				}
			}
		}
		return jsonObj;
	}
	
	private JSONObject executeSearchUnLimitTrophy(UserData userData, Map paramMap, int deduct, JSONObject srhObject) throws Exception {
		JSONObject jsonObj = null;
		List<UserData> userDatas = null;
		paramMap.remove("lowerLimit");
		paramMap.remove("upperLimit");
		userDatas = findByNamedQuery("search_target_by_user", paramMap, UserData.class);
		if(userDatas.size() > 0) {
			UserData result = userDatas.get(0);
			String key = CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + result.getUserId();
			if(redisImpl.setNx(key, TEMP_SRH_USER_TIME_OUNT, result.getUserId())) {
				jsonObj = lockTarget(userData, result, deduct, srhObject);
			} else {
				long countStartTime = System.currentTimeMillis();
				Long count = countByNameQuery("count_target_by_user", paramMap, UserData.class);
				long countEndTime = System.currentTimeMillis();
				LOGGER.debug("Count user waste time : " + (countEndTime - countStartTime));
				if(count >= 0) {
					paramMap.put("start", count == 0 ? 0 : count - 1);
					userDatas = findByNamedQuery("search_target_by_user", paramMap, UserData.class);
				}
				if(userDatas != null && userDatas.size() > 0) {
					result = userDatas.get(0);
					key = CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + result.getUserId();
					if(redisImpl.setNx(key, TEMP_SRH_USER_TIME_OUNT, result.getUserId())) {
						jsonObj = lockTarget(userData, result, deduct, srhObject);
					}
				}
			}
		}
		return jsonObj;
	}
	
	private JSONObject lockTarget(UserData userData, UserData result, int deduct, JSONObject srhObject) throws Exception {
		String key = CocComponent.BE_MARK_SRH_TARGET_ID_PREFIX + userData.getUserId() + "_" + result.getUserId();
		redisImpl.set(key, MARK_SRH_USER_TIME_OUNT, result.getUserId());
		
		JSONObject jsonObj = null;
		//lock defenser
//		userData.setSearchLock(1);
		Date curDate = new Date();
		userData.setLockTime(curDate);
		restUserCoin(userData, deduct);
		userData.setActionTime(curDate);
		asyncOperManager.updateData(userData);
		
		//lock attacker
		result.setSearchLock(1);
		result.setLockTime(curDate);
		asyncOperManager.updateData(result);
		
		
		result.setTouchTime(userData.getTouchTime());
		jsonObj = dataManager.getDataFromJsonData(srhObject, result.toJSONObject(), "appData");
		jsonObj.put("userId", result.getUserId());
		
		return jsonObj;
	}
	
	private JSONObject unlockRival(UserData userData, Long rivalId) {
		UserData rivalUserData = (UserData)get(rivalId, UserData.class);
		rivalUserData.setSearchLock(0);
		update(rivalUserData);
		
		Date curDate = new Date();
		userData.setActionTime(curDate);
		update(userData);
		
		String key = CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + rivalId;
		try {
			redisImpl.del(key);
		} catch (Exception e) {
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put(UserData.TOUCH_TIME_KEY, (userData.getTouchTime().getTime()/1000) * 1000);
		return resultObj;
	}

	private JSONObject uploadBattleLog(Long type, Long battleId, UserData userData, Long rivalId, Long duration, JSONObject battleData, JSONObject rivalData, JSONObject userBaseData, App app) {
		UserData rivalUserData = (UserData)get(rivalId, UserData.class);
		JSONObject oldUserData = rivalUserData.toJSONObject();
		oldUserData.remove(UserData.TOUCH_TIME_KEY);
		BattleLog battleLog = new BattleLog();
		replaceCupWithAttacker(oldUserData, userBaseData, battleLog);
		battleData.put(BATTLE_RIVAL_DATA_KEY, oldUserData);

		JSONObject baseDataJson = battleData.has(BATTLE_BASE_DATA_KEY) ? battleData.getJSONObject(BATTLE_BASE_DATA_KEY) : new JSONObject();
		battleLog.setAttacker(userData.getUserId());
		battleLog.setDefenser(rivalId);
		battleLog.setDuration(duration);
		if(type == 1) {
			battleLog.setType(BattleLog.ONLY_REVENGE_TYPE);
			
			//Modify the revenge battle
			BattleLog revengeBattle = new BattleLog();
			revengeBattle.setId(battleId);
			revengeBattle.setType(BattleLog.REVENGE_TYPE);
			update(revengeBattle);
		} else {
			battleLog.setType(BattleLog.UNREVENGE_TYPE);
		}
		battleLog.setBaseData(baseDataJson.toString());
		battleLog.setJsonData(battleData.toString());
		battleLog.setBattleTime(new Date());
		save(battleLog);
		/*//unlock user
		Map paramMap = new HashMap();
		paramMap.put("attacker", userData.getUserId());
		paramMap.put("defenser", rivalId);
		update("unlockUser", paramMap, UserData.class);*/

		//update rival's json data
		JSONObject rivalJsonObj = rivalUserData.toJSONObject();
		dataManager.updateJsonData(rivalData, rivalJsonObj);
		dataManager.delJsonData(rivalData, rivalJsonObj);
		rivalJsonObj.remove(UserData.TOUCH_TIME_KEY);
		rivalJsonObj.remove(UserData.ACTION_TIME_KEY);
		rivalJsonObj.remove(UserData.LOCK_TIME_KEY);
		rivalJsonObj.remove(UserData.LOCK_STATUS_KEY);
		rivalUserData.setJsonData(rivalJsonObj.toString());
		resetUserDataFieldFromJsonData(rivalUserData, rivalJsonObj);
		rivalUserData.setSearchLock(0);
		update(rivalUserData);

		//update user's json data(Key000000)
		JSONObject fromJsonData = new JSONObject();
		JSONObject subJsonData = new JSONObject();
		subJsonData.put(USER_INFO_KEY, userBaseData);
		fromJsonData.put(AppComponent.REQ_UPDATE_ACTION, subJsonData);

		Date curDate = new Date();
		JSONObject userJsonObj = userData.toJSONObject();
		dataManager.updateJsonData(fromJsonData, userJsonObj);
		userJsonObj.remove(UserData.TOUCH_TIME_KEY);
		userJsonObj.remove(UserData.ACTION_TIME_KEY);
		userJsonObj.remove(UserData.LOCK_TIME_KEY);
		userJsonObj.remove(UserData.LOCK_STATUS_KEY);
		userData.setJsonData(userJsonObj.toString());
		userData.setTouchTime(curDate);
		userData.setActionTime(curDate);
		update(userData);
		
		PushNotify pn = new PushNotify();
        pn.setMessage(String.format(notificationPrefix + " %s", userData.getNick()));
        pn.setBadge(1);
        
        Notification notif = new Notification(rivalUserData.getUserId(), app, pn);
        try {
        	asyncOperManager.addNotification(notif);
		} catch (Exception e) {
			logger.error("Send notification failed.", e);
		}

		JSONObject resultObj = new JSONObject();
		resultObj.put(UserData.TOUCH_TIME_KEY, (userData.getTouchTime().getTime()/1000) * 1000);
		return resultObj;
	}
	
	private void replaceCupWithAttacker(JSONObject rivalUserData, JSONObject userBaseData, BattleLog battleLog) {
		int cup = userBaseData.has(TROPS_FIELD_KEY) ? userBaseData.getInt(TROPS_FIELD_KEY) : 0;
		if(rivalUserData.has(USER_INFO_KEY)) {
			rivalUserData.getJSONObject(USER_INFO_KEY).put(TROPS_FIELD_KEY, cup);
			battleLog.setTrophy(cup);
		}
	}
	
	/*private JSONObject increaseForUserData(JSONObject jo, UserData userData, Long userId, String deviceId) {
		if (userData == null) {
			userData = new UserData();
		}
		JSONObject appDataJson = userData.toJSONObject() == null ? new JSONObject() : userData.toJSONObject();
		dataManager.incrJsonData(jo, appDataJson);
		JSONObject getJSON = jo.has(AppComponent.REQ_INCR_ACTION) && !jo.isNull(AppComponent.REQ_INCR_ACTION) ? jo.getJSONObject(AppComponent.REQ_INCR_ACTION) : new JSONObject();
		
		Date curDate = new Date();
		UserData newUserData = new UserData();
		newUserData.setUlld(deviceId);
		newUserData.setUserId(userId);
		resetUserDataFieldFromJsonData(newUserData, appDataJson);
		appDataJson.remove(UserAppData.TOUCH_TIME_KEY);
		appDataJson.remove(UserAppData.ACTION_TIME_KEY);
		appDataJson.remove(UserData.LOCK_TIME_KEY);
		appDataJson.remove(UserData.LOCK_STATUS_KEY);
		if(userData.getUserId() != null) {
			newUserData.setJsonData(appDataJson.toString());
		} else if (appDataJson.keys().hasNext()) {
			newUserData.setJsonData(appDataJson.toString());
		}
		newUserData.setTouchTime(curDate);
		newUserData.setActionTime(curDate);
		saveOrUpdate(newUserData);
		getJSON.put(UserAppData.TOUCH_TIME_KEY, (newUserData.getTouchTime().getTime() / 1000) * 1000);
		
		return getJSON;
	}*/
	
	protected JSONObject getOrUpdateOrDel(JSONObject jo, UserData userData, Long userId, String deviceId) throws JSONException {
		JSONObject getJSON = new JSONObject();
		if (userData == null) {
			userData = new UserData();
		}
		JSONObject appDataJson = userData.toJSONObject() == null ? new JSONObject() : userData.toJSONObject();
		//del
		if(jo.has(AppComponent.REQ_DEL_ACTION)) {
			dataManager.delJsonData(jo, appDataJson);
		}
		//upd
		if(jo.has(AppComponent.REQ_UPDATE_ACTION)) {
			dataManager.updateJsonData(jo, appDataJson);
		}
		//incr
		if(jo.has(AppComponent.REQ_INCR_ACTION)) {
			dataManager.incrJsonData(jo, appDataJson);
		}
		//get
		if(jo.has(AppComponent.REQ_GET_ACTION)) {
			getJSON = dataManager.getDataFromJsonData(jo, appDataJson, AppComponent.REQ_GET_ACTION);
		}
		Date curDate = new Date();
		if(jo.has(AppComponent.REQ_UPDATE_ACTION) || jo.has(AppComponent.REQ_DEL_ACTION)) {
			UserData newUserData = new UserData();
			newUserData.setUlld(deviceId);
			newUserData.setUserId(userId);
			resetUserDataFieldFromJsonData(newUserData, appDataJson);
			appDataJson.remove(UserAppData.TOUCH_TIME_KEY);
			appDataJson.remove(UserAppData.ACTION_TIME_KEY);
			appDataJson.remove(UserData.LOCK_TIME_KEY);
			appDataJson.remove(UserData.LOCK_STATUS_KEY);
			if(userData.getUserId() != null) {
				newUserData.setJsonData(appDataJson.toString());
			} else if (appDataJson.keys().hasNext()) {
				newUserData.setJsonData(appDataJson.toString());
			}
			newUserData.setTouchTime(curDate);
			newUserData.setActionTime(curDate);
			saveOrUpdate(newUserData);
			getJSON.put(UserAppData.TOUCH_TIME_KEY, (newUserData.getTouchTime().getTime() / 1000) * 1000);
		} else {
			UserData newUserData = new UserData();
			newUserData.setUserId(userId);
			newUserData.setActionTime(curDate);
			update(newUserData);
			getJSON.put(UserAppData.TOUCH_TIME_KEY, (userData.getTouchTime().getTime() / 1000) * 1000);
		}

		return getJSON;
	}

	private void resetUserDataFieldFromJsonData(UserData userData, JSONObject jsonData) throws JSONException {
		if(jsonData.getJSONObject(USER_INFO_KEY).has(SHIELD_END_TIME_KEY)) {
			Long curentTimeMillis = System.currentTimeMillis();
			Long shieldTimeSeconds = jsonData.getJSONObject(USER_INFO_KEY).getLong(SHIELD_END_TIME_KEY);
			long excapeMillis = 12 * 3600 * 100;
			if(curentTimeMillis != excapeMillis) shieldTimeSeconds = 0l;
			curentTimeMillis += shieldTimeSeconds * 1000;
			userData.setShieldTime(new Date(curentTimeMillis));
		} else {
			userData.setShieldTime(new Date());
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(USER_COIN_KEY)) {
			userData.setCoin(jsonData.getJSONObject(USER_INFO_KEY).getLong(USER_COIN_KEY));
		} else {
			userData.setCoin(0l);
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(GEM_FIELD_KEY)) {
			userData.setGem(jsonData.getJSONObject(USER_INFO_KEY).getInt(GEM_FIELD_KEY));
		} else {
			userData.setGem(0);
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(USER_LEVEL_KEY)) {
			userData.setLevel(jsonData.getJSONObject(USER_INFO_KEY).getInt(USER_LEVEL_KEY));
		} else {
			userData.setLevel(0);
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(USER_NICK_KEY)) {
			userData.setNick(jsonData.getJSONObject(USER_INFO_KEY).getString(USER_NICK_KEY));
		} else {
			userData.setNick(null);
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(USER_TOWN_LEVEL_KEY)) {
			userData.setTownLevel(jsonData.getJSONObject(USER_INFO_KEY).getInt(USER_TOWN_LEVEL_KEY));
		} else {
			userData.setTownLevel(0);
		}
		if(jsonData.getJSONObject(USER_INFO_KEY).has(TROPS_FIELD_KEY)) {
			userData.setTrophy(jsonData.getJSONObject(USER_INFO_KEY).getInt(TROPS_FIELD_KEY));
		} else {
			userData.setTrophy(0);
		}
	}

	private void restUserCoin(UserData userData, Integer deduct) throws JSONException {
		userData.setCoin(userData.getCoin() - deduct);
		JSONObject jsonData = userData.toJSONObject();
		if(jsonData.has(USER_INFO_KEY)) {
			jsonData.getJSONObject(USER_INFO_KEY).put(USER_COIN_KEY, userData.getCoin());
		}
		if(jsonData.has(UserAppData.TOUCH_TIME_KEY)) {
			jsonData.remove(UserAppData.TOUCH_TIME_KEY);
		}
		userData.setJsonData(jsonData.toString());
	}

    @Override
    public boolean lockTimeout(UserData ud) {
        boolean ret = false;
        if(ud.getLockTime() != null &&(System.currentTimeMillis() - ud.getLockTime().getTime()) > UserData.LOCK_TIMEOUT_MILLIS) {
            ret = true;
        }
        return ret;
    }

}
