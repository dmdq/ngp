package com.ngnsoft.ngp.component.slots.model;

import java.io.Serializable;
import java.util.Date;

import com.ngnsoft.ngp.model.BaseModel;

public class SlotsBuy extends BaseModel {

	private Integer id;
	private Long userId;
	private String nick;
	private String productId;
	private String level;
	private String coins;
	private String rank;
	private String appId;
	private String bundleId;
	
	

	public String getBundleId() {
		return bundleId;
	}

	public void setBundleId(String bundleId) {
		this.bundleId = bundleId;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getCoins() {
		return coins;
	}

	public void setCoins(String coins) {
		this.coins = coins;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (Integer) o;
	}
	
	@Override
	public void prepareForSave() {
		if(createTime == null) {
			createTime = new Date();
		}
	}
	
	@Override
	public String getDbName() {
		return "slots";
	}
	
}
