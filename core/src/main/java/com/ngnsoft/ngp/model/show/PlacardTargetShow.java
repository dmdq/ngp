package com.ngnsoft.ngp.model.show;

import com.ngnsoft.ngp.model.PlacardTarget;

public class PlacardTargetShow extends PlacardTarget {
	
	private String appName;
	private String title;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

}
