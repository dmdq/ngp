package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.List;

public class AppZone extends BaseModel {

	private Long id;
	
	private String appId;
	
	private boolean isBaid;
	
	private Long zoneId;
	
	private List<Zone> zones;

	private List<App> apps;
	
	public List<App> getApps() {
		return apps;
	}

	public void setApps(List<App> apps) {
		this.apps = apps;
	}

	public List<Zone> getZones() {
		return zones;
	}

	public void setZones(List<Zone> zones) {
		this.zones = zones;
	}

	public AppZone(){
		
	}
	
	public AppZone(Long id, String appId, boolean isBaid, Long zoneId){
		this.id = id;
		this.appId = appId;
		this.isBaid = isBaid;
		this.zoneId = zoneId;
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

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public boolean getIsBaid() {
		return isBaid;
	}

	public void setIsBaid(boolean isBaid) {
		this.isBaid = isBaid;
	}
	
	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long) o;
	}

}
