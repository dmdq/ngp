package com.ngnsoft.ngp.model;

import java.io.Serializable;

public class AppVersion extends BaseModel {

	private Long id;
	private String appId;
	private String version;
	
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

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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
