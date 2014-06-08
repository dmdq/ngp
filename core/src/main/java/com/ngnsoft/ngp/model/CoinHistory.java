package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class CoinHistory extends BaseModel {
    
    private Long id;
    
    private Long userId;
    
    private String appId;
    
    private Long incrmt;
    
    private String incrmtKey;
    
    public CoinHistory() {
        
    }
    
    public CoinHistory(Long id) {
        this.id = id;
    }
    
    public CoinHistory(Long id, Long userId, String appId, Long incrmt, String incrmtKey) {
        this.id = id;
        this.userId = userId;
        this.incrmt = incrmt;
        this.incrmtKey = incrmtKey;
        this.appId = appId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public Long getIncrmt() {
        return incrmt;
    }

    public void setIncrmt(Long incrmt) {
        this.incrmt = incrmt;
    }

    public String getIncrmtKey() {
        return incrmtKey;
    }

    public void setIncrmtKey(String incrmtKey) {
        this.incrmtKey = incrmtKey;
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
        id = (Long)id;
    }
    
}
