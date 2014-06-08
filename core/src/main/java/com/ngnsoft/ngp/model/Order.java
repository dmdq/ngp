package com.ngnsoft.ngp.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import org.json.JSONObject;

public class Order extends BaseModel {

	@XmlElement(name = "order_id")
	private String orderId;
	
	@XmlElement(name = "login_name")
	private String loginName;
	
	@XmlElement
	private String amount;
	
	@XmlElement
	private String status;
	
	@XmlElement(name = "pay_time")
	private String payTime;
	
	@XmlElement(name = "game_role")
	private String gameRole;
	
	private String deviceId;
	
	private String pid;
	
	private String jsonData;
	
	private String appId;
	
	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	@XmlTransient
	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	@XmlTransient
	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	@XmlTransient
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@XmlTransient
	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	@XmlTransient
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	@XmlTransient
	public String getGameRole() {
		return gameRole;
	}

	public void setGameRole(String gameRole) {
		this.gameRole = gameRole;
	}
	
	public String getJsonData() {
		JSONObject obj = toJSONObject();
		obj.remove("jsonData");
		obj.remove("pid");
		obj.remove("deviceId");
		return obj.toString();
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getPid() {
		return pid;
	}

	public void setPid(String pid) {
		this.pid = pid;
	}

	@Override
	public Serializable getPrimaryKey() {
		return orderId;
	}

	@Override
	public void setPrimaryKey(Object o) {
		orderId = (String)o;
	}
	
	@Override
	public String getTableName() {
		return "orders";
	}
	
}
