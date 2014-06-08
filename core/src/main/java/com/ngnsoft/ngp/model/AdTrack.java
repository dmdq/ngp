package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class AdTrack extends BaseModelExt {
    
	public static final String AD_INPRESSION = "inpression";
	
	public static final String AD_INSTALL = "install";
	
    private Long id;
    
    private String adAppid;
    
    private String adAction;
    
    private String appId;
    
    private String deviceId;
    
    public AdTrack() {
        super();
    }
    
    public AdTrack(Long id, String adAppid, String adAction, String appId, String deviceId) {
        this.id = id;
        this.adAppid = adAppid;
        this.adAction = adAction;
        this.appId = appId;
        this.deviceId = deviceId;
    }

    public String getAdAction() {
        return adAction;
    }

    public void setAdAction(String adAction) {
        this.adAction = adAction;
    }

    public String getAdAppid() {
        return adAppid;
    }

    public void setAdAppid(String adAppid) {
        this.adAppid = adAppid;
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

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long)o;
    }
    
    
    
}
