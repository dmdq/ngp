package com.ngnsoft.ngp.model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class SnsData extends BaseModel {
    
    private Long id;
    
    private String  userKey;
    
    private String keyType;
    
    private String snsJson;
    
    public SnsData() {
        
    }
    
    public SnsData(Long id) {
        this.id = id;
    }
    
    public SnsData(Long id, String userKey, String keyType, String snsJson) {
        this.id = id;
        this.userKey = userKey;
        this.keyType = keyType;
        this.snsJson = snsJson;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSnsJson() {
        return snsJson;
    }

    public void setSnsJson(String snsJson) {
        this.snsJson = snsJson;
    }

    public String getKeyType() {
        return keyType;
    }

    public void setKeyType(String keyType) {
        this.keyType = keyType;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }
    
    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("id");
        jo.remove("snsJson");
        return jo;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(snsJson);
        } catch (Exception ex) {
        }
        return jo;
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
