package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.misc.PushNotify;

public class Notification {

	private Long userId;
	
	private App app;
	
	private PushNotify pn;

	public Notification(Long userId, App app, PushNotify pn) {
		this.userId = userId;
		this.app = app;
		this.pn = pn;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public App getApp() {
		return app;
	}

	public void setApp(App app) {
		this.app = app;
	}

	public PushNotify getPn() {
		return pn;
	}

	public void setPn(PushNotify pn) {
		this.pn = pn;
	}

}
