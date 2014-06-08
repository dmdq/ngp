package com.ngnsoft.ngp.model;

import java.io.Serializable;

import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class AppConfig extends BaseModel {
    
    private String appId;
    
    private String appName;
    
    private String jsonAll;
    
    private App app;
    
    public AppConfig() {
        
    }
    
    
    
    public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public AppConfig(String appId) {
        this.appId = appId;
    }
    
    public AppConfig(String appId, String jsonAll) {
        this.appId = appId;
        this.jsonAll = jsonAll;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getJsonAll() {
        return jsonAll;
    }

    public void setJsonAll(String jsonAll) {
        this.jsonAll = jsonAll;
    }

    @Override
    public Serializable getPrimaryKey() {
        return appId;
    }

    @Override
    public void setPrimaryKey(Object o) {
        appId = (String)o;
    }

    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("jsonAll");
        return jo;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(jsonAll);
        } catch (Exception ex) {
        }
        return jo;
    }

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}
    
}
