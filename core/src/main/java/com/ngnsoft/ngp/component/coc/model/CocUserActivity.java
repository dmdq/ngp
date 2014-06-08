package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.UserActivity;

/**
 *
 * @author fcy
 */
public class CocUserActivity extends UserActivity {
    
    public CocUserActivity() {
        
    }

    public CocUserActivity(Long id, String action, Long userId, String appId, String appVersion, String deviceId, String engineId, String ip) {
        super(id, action, userId, appId, appVersion, deviceId, engineId, ip);
    }

    @Override
    public String getDbName() {
        return "coc";
    }

    @Override
    public String getTableName() {
        return "user_activity";
    }

    @Override
    public String getModelName() {
        return "UserActivity";
    }
    
}
