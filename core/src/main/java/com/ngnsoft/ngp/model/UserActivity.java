package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author fcy
 */
public class UserActivity extends BaseModel {
    
    public static final String ACTION_LOGIN = "login";
    
    private Long id;
    
    private String action;
    
    private Long userId;
    
    private String appId;
    
    private String appVersion;
    
    private String deviceId;
    
    private String engineId;
    
    private String ip;
    
    private App app;
    
    private EngineNode engineNode;
    
	public UserActivity() {
        
    }
    
    public UserActivity(Long id) {
        this.id = id;
    }
    
    public UserActivity(Long id, String action, Long userId, String appId, String appVersion, String deviceId, String engineId, String ip) {
        this.id = id;
        this.action = action;
        this.userId = userId;
        this.appId = appId;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.engineId = engineId;
        this.ip = ip;
    }

    public EngineNode getEngineNode() {
		return engineNode;
	}

	public void setEngineNode(EngineNode engineNode) {
		this.engineNode = engineNode;
	}
    
    public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
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
    public void prepareForUpdate() {
    	updateTime = new Date();
    }
    
}
