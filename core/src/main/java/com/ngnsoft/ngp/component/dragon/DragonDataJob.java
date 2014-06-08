package com.ngnsoft.ngp.component.dragon;

import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.sp.redis.RedisImpl;


public class DragonDataJob {
	
	private static Logger LOGGER = Logger.getLogger(DragonDataJob.class);
	
	@Autowired
	private RedisImpl redisImpl;
	
	/**
	 * clear user's data helping for battle
	 */
	public void clearFacData() {
		String pattern = "*" + DragonComponent.REDIS_DRAGON_HELP_ID_PREFIX + "*";
		try {
			Set<String> set = redisImpl.keys(pattern);
			redisImpl.clear(set);
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
	}
	
}
