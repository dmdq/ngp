package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class PushToken extends BaseModel {
    
    private Long id;
    
    private String appId;
    
    private String deviceId;
    
    private String token;
    
    private String OS;

    public PushToken() {
    }

    public PushToken(Long id) {
        this.id = id;
    }

    public PushToken(Long id, String appId, String deviceId, String token, String OS) {
        this.id = id;
        this.appId = appId;
        this.deviceId = deviceId;
        this.token = token;
        this.OS = OS;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long)o;
    }
    
//    @Override
//    public void prepareForSave() {
//        super.prepareForSave();
//        id = getId(appId, deviceId);
//    }
//    
//    public static String getId(String appId, String deviceId) {
//        return appId + "_" + deviceId;
//    }
    
}
