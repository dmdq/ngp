package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class UserSessionCount extends BaseModel {
    
    private Long id;
    
    private String engineId;
    
    private Long count;
    
    public UserSessionCount() {
        
    }
    
    public UserSessionCount(Long id, String engineId, long count) {
        this.id = id;
        this.engineId = engineId;
        this.count = count;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getEngineId() {
        return engineId;
    }

    public void setEngineId(String engineId) {
        this.engineId = engineId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        this.id = (Long)o;
    }
    
}
