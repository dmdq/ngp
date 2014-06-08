package com.ngnsoft.ngp.service.impl;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author fcy
 */
@Service
public class UserSessionManagerImpl extends GenericManagerImpl implements UserSessionManager {
	
	private static final Logger logger = LoggerFactory.getLogger(UserSessionManagerImpl.class);
    
	@Autowired
    @Qualifier("redisImpl")
    private RedisImpl redisImpl;
	
	private static final int SESSION_TIME_OUT = 30 * 60; //30minutes
	
	public static final String SESSION_KEY_PREFIX = "US_";
	
    @Override
    public UserSession touchSession(String sid){
        UserSession us = getUserSessionBySid(sid);
        if(us == null || UserSession.STATUS_TIMEOUT == us.getStatus()) {
            return null;
        }
        /*if (us.timeOut()) {
            UserSession usUpdate = new UserSession(us.getId());
            usUpdate.setStatus(UserSession.STATUS_TIMEOUT);
            updateUserSession(usUpdate);
            return null;
        }*/
        if (UserSession.STATUS_VALID == us.getStatus()) {
            UserSession usUpdate = new UserSession(us.getId());
            usUpdate.setUpdateTime(us.getUpdateTime());
            String engineId = Engine.getInstance().getId();
            if (!us.getEngineId().equals(engineId)) {
                usUpdate.setEngineId(engineId);
                //TODO: use listener to do migrate
            }
            updateUserSession(usUpdate);
        }
        return us;
    }
    
    @Override
    public UserSession matchValidSession(Long userId){
        return getValidUserSession(userId);
    }

    @Override
    public void kickSession(UserSession us) {
        UserSession usUpdate = new UserSession(us.getId());
        usUpdate.setStatus(UserSession.STATUS_KICKED);
        updateUserSession(usUpdate);
    }
    
    @Override
    public void expireUser(Long userId, String baid) {
    	/*App app = new App();
    	app.setBaseId(baid);
    	List<App> apps = findMulti(app);
    	
    	String[] appIds = new String[apps.size()];
    	for (int i = 0; i < apps.size(); i++) {
    		appIds[i] = apps.get(i).getId();
		}
    	
    	Map paramMap = new HashMap();
    	paramMap.put("userId", userId);
    	paramMap.put("rangeList", appIds);
    	
    	List<UserSession> userSessions = findByNamedQuery("find_user_session_by_userid_and_appid_and_status", paramMap, UserSession.class);
    	
    	for (UserSession userSession : userSessions) {
    		userSession.setStatus(UserSession.STATUS_TIMEOUT);
    		this.update(userSession);
		}*/
    }

    @Override
    public Response checkSid(Request req, App app) {
        Response resp = new Response(Response.YES);
        String sid = req.getSession();
        if (sid == null || sid.isEmpty()) {
            return resp;
        }
        UserSession us = touchSession(sid);
        if (us != null) {
            if (UserSession.STATUS_VALID == us.getStatus()) {
                resp.setAttachObject(us.getUserId());
            } else if (UserSession.STATUS_KICKED == us.getStatus()){
                resp = new Response(Response.NO, SESSION_KICKED);
            }
        }
        return resp;
    }

    @Override
    public String login(Long userId, String deviceId, Request req) {
        UserSession nus = new UserSession(null, userId, req.getAppId(), req.getAppVersion(), deviceId, Engine.getInstance().getId(), req.getIP());
        saveUserSession(nus);
        return nus.getId();
    }
    
    private UserSession getUserSessionBySid(String sid) {
        try {
            Set<String> keys = redisImpl.keys("*_" + sid + "*");
            return keys.isEmpty() ? null : (UserSession) redisImpl.get(keys.iterator().next());
        } catch (Exception e) {
            return null;
        }
    }
    
    private UserSession getValidUserSession(Long userId) {
        try {
            Set<String> set = redisImpl.keys("*" + "_" + userId + "_*");
            List<String> keys = new ArrayList<String>(set);
            for (String key : keys) {
                UserSession session = (UserSession) redisImpl.get(key);
                if (session.getStatus() == UserSession.STATUS_VALID && userId.longValue() == session.getUserId()) {
                    return session;
                }
            }
        } catch (Exception e) {
        }
        return null;
    }
    
    private void saveUserSession(UserSession userSession) {
    	try {
    		userSession.prepareForSave();
			redisImpl.set(SESSION_KEY_PREFIX + Engine.getInstance().getId() + "_" + userSession.getUserId() + "_" + userSession.getId(), SESSION_TIME_OUT, userSession);
		} catch (Exception e) {
		}
    }
    
    private void updateUserSession(UserSession userSession) {
    	try {
    		Set<String> keys = redisImpl.keys("*" + "_" + userSession.getId() + "*");
    		UserSession oldSession = keys.size() > 0 ? (UserSession)redisImpl.get(keys.iterator().next()) : null;
    		if(oldSession != null) {
    			oldSession.prepareForUpdate();
    			if(userSession.getStatus() != null) {
    				oldSession.setStatus(userSession.getStatus());
    			}
    			if(userSession.getEngineId() != null) {
    				oldSession.setEngineId(userSession.getEngineId());
    			}
    			if(userSession.getUpdateTime() != null) {
    				oldSession.setUpdateTime(userSession.getUpdateTime());
    			}
    			redisImpl.clear(keys);
    			redisImpl.set(SESSION_KEY_PREFIX + Engine.getInstance().getId() + "_" + oldSession.getUserId() + "_" + userSession.getId(), SESSION_TIME_OUT, oldSession);
    		}
		} catch (Exception e) {
			
		}
    }
    
}
