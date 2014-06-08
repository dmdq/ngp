package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class UserApp extends BaseModel {
    
    private Long id;
    
    private Long userId;
    
    private String appId;
    
    public UserApp() {
        
    }
    
    public UserApp(Long id) {
        this.id = id;
    }

    public UserApp(Long id, Long userId, String appId) {
        this.id = id;
        this.userId = userId;
        this.appId = appId;
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
//        id = getId(userId, appId);
//    }
//    
//    public static String getId(Long userId, String appId) {
//        return userId + "_" + appId;
//    }
    
}
