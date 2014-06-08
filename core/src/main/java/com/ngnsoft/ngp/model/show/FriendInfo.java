package com.ngnsoft.ngp.model.show;

import org.json.JSONException;
import org.json.JSONObject;

public class FriendInfo {

	private String userKey;
	
	private String keyType;
	
	private Long userId;
	
	private String msg;
	
	private JSONObject appData;
	
	private String snsData;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public JSONObject getAppData() {
		return appData;
	}

	public void setAppData(JSONObject appData) {
		this.appData = appData;
	}

	public String getUserKey() {
		return userKey;
	}

	public void setUserKey(String userKey) {
		this.userKey = userKey;
	}

	public String getKeyType() {
		return keyType;
	}

	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}

	public String getSnsData() {
		return snsData;
	}

	public void setSnsData(String snsData) {
		this.snsData = snsData;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject(this);
		obj.remove("touchTime");
		obj.remove("snsData");
		try {
			if(snsData != null) {
				JSONObject snsObj = new JSONObject(snsData);
				obj.put("snsData", snsObj);
			}
		} catch (JSONException e) {
		}
		return obj;
	}

}
