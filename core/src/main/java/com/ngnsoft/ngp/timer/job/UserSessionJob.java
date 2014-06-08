package com.ngnsoft.ngp.timer.job;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.model.UserSessionCount;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.service.impl.UserSessionManagerImpl;
import com.ngnsoft.ngp.sp.redis.RedisImpl;


/**
 * Only USER CENTER engine can check-mark and remove TIMEOUT session of all engines
 * Other engines can check-mark TIMEOUT session of himeself
 * 
 * @author fcy
 */
public class UserSessionJob {
    
    @Autowired
    private UserSessionManager usm;
    
    @Autowired
    @Qualifier("redisImpl")
    private RedisImpl redisImpl;
    
    private boolean ucEnable = false;
    
    private String engineId;

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public void setUcEnable(boolean ucEnable) {
        this.ucEnable = ucEnable;
    }
    
    public void execute() {
        long loading = 0;
        /*UserSession us = new UserSession();
        if (!ucEnable) {
            us.setEngineId(engineId);
        }
        List<UserSession> usl = usm.findMulti(us);
        for (UserSession aus : usl) {
            if (aus.getStatus() == UserSession.STATUS_TIMEOUT) {
                if (ucEnable) {
                    try {
                        usm.remove(aus);
                    } catch (Exception e) {
                        //ignore
                    }
                }
            } else if (aus.timeOut()) {
                UserSession usUpdate = new UserSession(aus.getId());
                usUpdate.setStatus(UserSession.STATUS_TIMEOUT);
                try {
                    usm.update(usUpdate);
                } catch (Exception e) {
                    //ignore
                }
            } else {
                if (aus.getEngineId().equals(engineId)) {
                    loading++;
                }
            }
        }*/
        try {
			Set<String> keys = redisImpl.keys("*" + UserSessionManagerImpl.SESSION_KEY_PREFIX + Engine.getInstance().getId() + "_*");
			loading = keys.size();
		} catch (Exception e) {
		}
        Engine.getInstance().setLoading(loading);
        usm.save(new UserSessionCount(null, engineId, loading));
    }

}
