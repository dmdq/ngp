package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.util.JSONUtil;
import java.io.Serializable;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Sale extends BaseModel {
    
    protected Long id;
    
    protected String ad;
    
    protected String appId;
    
    protected String deviceId;
    
    protected String pid;
    
    protected String jsonData;
    
    protected String ip;
    
    protected App app;
    
    public Sale() {
        
    }

    public Sale(Long id) {
        this.id = id;
    }
    
    public Sale(Long id, String ad, String appId, String deviceId, String pid, String jsonData, String ip) {
        this.id = id;
        this.ad = ad;
        this.appId = appId;
        this.deviceId = deviceId;
        this.pid = pid;
        this.jsonData = jsonData;
        this.ip = ip;
    }
    
    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
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

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	@Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        this.id = (Long)o;
    }
    
    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("jsonData");
        return jo;
    }
    
    @Override
    public JSONObject toJSONObject() {
        try {
            JSONObject jo1 = new JSONObject(jsonData);
            JSONObject jo2 = doJSONObject();
            return JSONUtil.mergeAndUpdate(jo1, jo2);
        } catch (JSONException ex) {
        }
        return null;
    }
    
}
