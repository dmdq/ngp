package com.ngnsoft.ngp.model.show;

import java.io.Serializable;
import java.util.Date;

public class Comrade implements Serializable {

	private static final long serialVersionUID = -4431322108290236425L;

	private Long userId;
	
	private String jsonData;
	
	private boolean isFrd;
	
	private int level;
	
	private Date actionTime;
	
	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public Date getActionTime() {
		return actionTime;
	}

	public void setActionTime(Date actionTime) {
		this.actionTime = actionTime;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public boolean getIsFrd() {
		return isFrd;
	}

	public void setIsFrd(boolean isFrd) {
		this.isFrd = isFrd;
	}
	
}
