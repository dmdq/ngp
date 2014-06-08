package com.ngnsoft.ngp.component.coc;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.service.UserDataManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;


public class UserDataJob {
	
	private static Logger LOGGER = Logger.getLogger(UserDataJob.class);
	
	@Autowired
	private UserDataManager userDataManager;
	
	@Autowired
	private RedisImpl redisImpl;

	/**
	 * unlock userData status after the user was locked for 8 minutes
	 */
	@SuppressWarnings("unchecked")
	public void unlockUserData() {
		LOGGER.debug("Beginning unlocking user status >>>");
		Map paramMap = new HashMap();
		paramMap.put("timeoutInterval", UserData.LOCK_TIMEOUT_MINUTE);
		paramMap.put("sysDate", new Date());
		userDataManager.update("unlockUserBySchedule", paramMap, UserData.class);
		/*String pattern = "*" + CocComponent.BE_SRH_TARGET_ID_PREFIX + "*";
		try {
			Set<String> keys = redisImpl.keys(pattern);
			Set<String> removeKeys = new HashSet<String>();
			for(String key : keys) {
				Long storeMillis = (Long)redisImpl.get(key);
				if(DateUtil.getDateIntervalWithMinute(System.currentTimeMillis(), storeMillis) > 4){
					removeKeys.add(key);
				}
			}
			redisImpl.clear(removeKeys);
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}*/
		
		LOGGER.debug("Succeed in unlocking user status <<<");
	}
	
}
