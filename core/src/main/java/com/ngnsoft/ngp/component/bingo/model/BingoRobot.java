package com.ngnsoft.ngp.component.bingo.model;

import java.io.Serializable;
import java.util.Set;

import com.ngnsoft.ngp.component.bingo.BingoComponent;
import com.ngnsoft.ngp.component.bingo.service.BingoManager;
import com.ngnsoft.ngp.component.bingo.service.impl.BingoManagerImpl;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Robot;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.ConvertUtil;
import com.ngnsoft.ngp.util.MiscUtil;

public class BingoRobot extends BaseModel implements Robot {
	
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Long id;
	
	private String data;
	
	private float readyRate;
	
	private float bingoRate;
	
	private float joinRate;
	
	private float quitRate;
	
	private RedisImpl redisImpl;
	
	private BingoManager bingoManager;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public float getReadyRate() {
		return readyRate;
	}

	public void setReadyRate(float readyRate) {
		this.readyRate = readyRate;
	}

	public float getBingoRate() {
		return bingoRate;
	}

	public void setBingoRate(float bingoRate) {
		this.bingoRate = bingoRate;
	}

	public float getJoinRate() {
		return joinRate;
	}

	public void setJoinRate(float joinRate) {
		this.joinRate = joinRate;
	}

	public float getQuitRate() {
		return quitRate;
	}

	public void setQuitRate(float quitRate) {
		this.quitRate = quitRate;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}
	
	@Override
	public String getDbName() {
		return "bingo";
	}
	
	public void prepareForRun(RedisImpl redisImpl, BingoManager bm) {
		this.redisImpl = redisImpl;
		this.bingoManager = bm;
	}
	
	/**
	 * Control the robot to do the task
	 */
	@Override
	public void run() {
		try {
			System.out.println("robot" + id + " start run...");
			Set<String> set = redisImpl.keys("*" + BingoManagerImpl.BINGO_MEMBER_DATA_REDIS_PREFIX + "*");
			String gid = null;
			for(String key : set) {
				Set<byte[]> robotBytes = redisImpl.zRangeByScore(key, id, id);
				if(robotBytes.size() > 0) {
					gid = key.substring(BingoManagerImpl.BINGO_MEMBER_DATA_REDIS_PREFIX.length());
					break;
				}
			}
			if(gid == null) {
				if(set.size() > 0) {
					Object[] array = set.toArray();
					String randomKey = (String)(array[ConvertUtil.generateRandom(0, array.length)]);
					gid = randomKey.substring(BingoManagerImpl.BINGO_MEMBER_DATA_REDIS_PREFIX.length());
					if(MiscUtil.chanceSelect(joinRate)) {
						doJoin(gid);
					}
				}
			}
			System.out.println("gid:" + gid);
			App app = new App();
			app.setBaseId(BingoComponent.APP_BASE_ID);
			app.setId(BingoComponent.APP_BASE_ID);
			bingoManager.checkGame(app, gid);
			if(set.size() > 0) {
				//ready
				if(MiscUtil.chanceSelect(readyRate)) {
					doReady(gid);
				}
				//bingo
				if(MiscUtil.chanceSelect(bingoRate)) {
					doBingo(gid);
				}
				
				//bingo
				if(MiscUtil.chanceSelect(quitRate)) {
					doQuit(gid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	private void doJoin(String gid) {
        try {
			bingoManager.joinGame(id, gid, data);
			System.out.println("user :" + id + " join in room " + gid);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doReady(String gid) {
		try {
			Long cardNum = (long)ConvertUtil.generateRandom(1, 4);
			long gameStartTime = (Long) redisImpl.get(BingoManagerImpl.BINGO_START_TIME_REDIS_PREFIX + gid);
			if (gameStartTime > System.currentTimeMillis()) {
				if(redisImpl.zCountByScore(BingoManagerImpl.BINGO_READY_MEMBER_REDIS_PREFIX + gid, id, id) == 0) {
			        redisImpl.incr(BingoManagerImpl.BINGO_CNUM_REDIS_PREFIX + gid, cardNum);
			        redisImpl.incr(BingoManagerImpl.BINGO_READY_NUM_REDIS_PREFIX);
			        redisImpl.zadd(BingoManagerImpl.BINGO_READY_MEMBER_REDIS_PREFIX + gid, id, id);
			        System.out.println("user :" + id + " cardNum:" + cardNum + " ready in room " + gid);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doBingo(String gid) {
        try {
        	if(redisImpl.exist(BingoManagerImpl.BINGO_START_TIME_REDIS_PREFIX + gid)) {
        		long gameStartTime = (Long) redisImpl.get(BingoManagerImpl.BINGO_START_TIME_REDIS_PREFIX + gid);
        		if(gameStartTime > System.currentTimeMillis()) {
        			System.out.println("game no started");
        		} else {
        			System.out.println("game started");
        		}
    			if (gameStartTime < System.currentTimeMillis() && redisImpl.zRangeByScore(BingoManagerImpl.BINGO_READY_MEMBER_REDIS_PREFIX + gid, id, id).size() > 0) {
    				if((System.currentTimeMillis() - gameStartTime) > 20000) {
    					Long oldNum = redisImpl.incr(BingoManagerImpl.BINGO_NUM_REDIS_PREFIX + gid, 0);
        				long bingoNum = redisImpl.decr(BingoManagerImpl.BINGO_NUM_REDIS_PREFIX + gid);
        				System.out.println("bingoNum : " + bingoNum + " oldNum:" + oldNum);
        			    if (bingoNum >= 0) {
        			    	System.out.println("user :" + id + " bingo in room " + gid);
        			        long curMillis = System.currentTimeMillis();
        			        redisImpl.zadd(BingoManagerImpl.BINGO_TOP_LIST_REDIS_PREFIX + gid, curMillis, id);
        			        redisImpl.set(BingoManagerImpl.BINGO_ROLE_DATA_REDIS_PREFIX + id, data);
        			        if (bingoNum == 0) {
        			        	bingoManager.onGameOver(gid);
        			        	System.out.println("game over ...");
        			        }
        			    }
    				}
    			}
        	}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void doQuit(String gid) {
		if(redisImpl.zCountByScore(BingoManagerImpl.BINGO_READY_MEMBER_REDIS_PREFIX + gid, id, id) == 0) {
			if(bingoManager.quit(gid, id, data)) {
				System.out.println("user :" + id + " quit from room " + gid + " data:------" + data);
			}
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj instanceof BingoRobot) {
			id.equals(((BingoRobot)obj).getId());
		}
		return false;
	}

}
