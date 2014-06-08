package com.ngnsoft.ngp.model;

import java.io.Serializable;

import com.mysql.jdbc.Util;

public class PlacardTarget extends BaseModel {
	
	private Long id;
	
	private Long placardId;
	
	private String appId;
	
	private String appVersion;
	
	private Boolean isBaid;
	
	private Long zoneId; 
	
	private Placard placard;
	
	private Zone zone;
	
	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}

	public Placard getPlacard() {
		return placard;
	}

	public void setPlacard(Placard placard) {
		this.placard = placard;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getPlacardId() {
		return placardId;
	}

	public void setPlacardId(Long placardId) {
		this.placardId = placardId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
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

	public Boolean getIsBaid() {
		return isBaid;
	}

	public void setBaid(Boolean isBaid) {
		this.isBaid = isBaid;
	}

	@Override
	public void prepareForSave() {
		// TODO Auto-generated method stub
		super.prepareForSave();
		if (appVersion == null || "".equals(appVersion)) {
			appVersion = null;
        }
	}
	
}
