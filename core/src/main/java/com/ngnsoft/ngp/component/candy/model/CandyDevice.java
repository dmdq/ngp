package com.ngnsoft.ngp.component.candy.model;

import java.io.Serializable;

import com.ngnsoft.ngp.model.BaseModel;

public class CandyDevice extends BaseModel {

	private String id;
	private String appId;
	private String appVersion;
	private String model;
	private String osVersion;
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (String) o;;
	}
	
}
