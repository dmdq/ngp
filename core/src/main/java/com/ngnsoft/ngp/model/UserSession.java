package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author fcy
 */
public class UserSession extends BaseModel {
    
    public static final long TIMEOUT_SECOND = 1800;//30*60
    
    public static final int STATUS_VALID = 0;
    
    public static final int STATUS_KICKED = 1;
    
    public static final int STATUS_TIMEOUT = 2;
    
    private String id;
    
    private Long userId;
    
    private String appId;
    
    private String appVersion;
    
    private String deviceId;
    
    private String engineId;
    
    private Integer status;
    
    private String ip;
    
    public UserSession() {
        
    }
    
    public UserSession(String id) {
        this.id = id;
    }
    
    public UserSession(String id, Long userId, String appId, String appVersion, String deviceId, String engineId, String ip) {
        this.id = id;
        this.userId = userId;
        this.appId = appId;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.engineId = engineId;
        this.ip = ip;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (String)o;
    }
    
    @Override
    public void prepareForSave() {
        super.prepareForSave();
        id = UUID.randomUUID().toString();
        status = STATUS_VALID;
    }
    
    public boolean timeOut() {
        boolean ret = false;
        if (System.currentTimeMillis() - updateTime.getTime() > UserSession.TIMEOUT_SECOND * 1000L) {
            ret = true;
        }
        return ret;
    }
}
