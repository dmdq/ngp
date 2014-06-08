package com.ngnsoft.ngp.sp.handler;

import java.io.Serializable;
import java.util.Map;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

public class PlacardModifyHandler implements MessageHandler {
	
	private RedisImpl redisImpl;

	@Override
	public void handle(Serializable message) throws Exception {
		Map<String, Object> map = (Map)message;
		if(map.containsKey("action")) {
			Placard placard = (Placard)map.get("placard");
			if(map.get("action").toString().equals("modify")) {
				redisImpl.set(Engine.PLACARD_REDIS_PREFIX + placard.getId(), placard);
			} else if(map.get("action").toString().equals("del")) {
				redisImpl.del(Engine.PLACARD_REDIS_PREFIX + placard.getId());
			}
		}
	}

	public void setRedisImpl(RedisImpl redisImpl) {
		this.redisImpl = redisImpl;
	}

}
