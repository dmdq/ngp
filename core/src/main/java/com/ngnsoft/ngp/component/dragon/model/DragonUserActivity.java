package com.ngnsoft.ngp.component.dragon.model;

import com.ngnsoft.ngp.model.UserActivity;

/**
 *
 * @author fcy
 */
public class DragonUserActivity extends UserActivity {

    public DragonUserActivity () {
        
    }
    
    public DragonUserActivity(Long id, String action, Long userId, String appId, String appVersion, String deviceId, String engineId, String ip) {
        super(id, action, userId, appId, appVersion, deviceId, engineId, ip);
    }

    @Override
    public String getDbName() {
        return "dragon";
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
