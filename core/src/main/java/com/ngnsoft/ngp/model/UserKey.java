package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.util.MiscUtil;
import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class UserKey extends BaseModel {
    
    protected Long id;
    
    protected String userKey;
    
    protected Long userId;
    
    protected String keyType;
    
    protected String keyFrom;
    
    public UserKey() {
        tableName = MiscUtil.classToDbName(UserKey.class);
    }
    
    public UserKey(Long id) {
        tableName = MiscUtil.classToDbName(UserKey.class);
        this.id = id;
    }
    
    public UserKey(Long id, String userKey, Long userId, String keyType, String keyFrom) {
        tableName = MiscUtil.classToDbName(UserKey.class);
        this.id = id;
        if (userKey != null) {
            this.userKey = userKey.toLowerCase();
        }
        this.userId = userId;
        this.keyType = keyType;
        this.keyFrom = keyFrom;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKeyFrom() {
        return keyFrom;
    }

    public void setKeyFrom(String keyFrom) {
        this.keyFrom = keyFrom;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        if (userKey == null) {
            return userKey;
        }
        return userKey.toLowerCase();
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey.toLowerCase();
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long)o;
    }
    
}
