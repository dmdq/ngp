package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserSession;

/**
 *
 * @author fcy
 */
public interface UserSessionManager extends GenericManager {
    
    public static final String SESSION_KICKED = "KICKED";
    
    public UserSession touchSession(String sid);
    
    public UserSession matchValidSession(Long userId);

    public void kickSession(UserSession us);
    
    public void expireUser(Long userId, String baid);

    public Response checkSid(Request req, App app);

    public String login(Long userId, String deviceId, Request req);
    
}
