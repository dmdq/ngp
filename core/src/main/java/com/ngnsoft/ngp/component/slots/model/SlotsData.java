package com.ngnsoft.ngp.component.slots.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

import com.ngnsoft.ngp.model.UserAppData;

public class SlotsData extends UserAppData {

	private Integer integral;
	
	private Integer eligible;
	
	private Integer collectStatus;
	
	private Date collectTime;
	
	private Integer rewards;
	
	private String activityData;
	
	private Integer level;
	
	private Long coins;
	
	
	
	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Long getCoins() {
		return coins;
	}

	public void setCoins(Long coins) {
		this.coins = coins;
	}

	public SlotsData() {}

	public SlotsData(Long userId) {
		this.userId = userId;
	}

	public String getActivityData() {
		return activityData;
	}

	public void setActivityData(String activityData) {
		this.activityData = activityData;
	}

	public Integer getRewards() {
		return rewards;
	}

	public Integer getCollectStatus() {
		return collectStatus;
	}

	public void setCollectStatus(Integer collectStatus) {
		this.collectStatus = collectStatus;
	}

	public Date getCollectTime() {
		return collectTime;
	}

	public void setCollectTime(Date collectTime) {
		this.collectTime = collectTime;
	}

	public void setRewards(Integer rewards) {
		this.rewards = rewards;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}
	
	public Integer getEligible() {
		return eligible;
	}

	public void setEligible(Integer eligible) {
		this.eligible = eligible;
	}

	@Override
    public String getDbName() {
        return "slots";
    }
	
	@Override
    public Serializable getPrimaryKey() {
        return userId;
    }

    @Override
    public void setPrimaryKey(Object o) {
    	userId = (Long) o;
    }
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(eligible == null) eligible = 0;
		if(rewards == null) rewards = 0;
		if(collectTime == null) collectTime = new Date();
		if(collectStatus == null) collectStatus = 0;
	}
	
    public JSONObject getActivityObject() {
        JSONObject jo = null;
        try {
        	if(activityData == null) {
        		jo = new JSONObject();
        	} else {
        		jo = new JSONObject(activityData);
        	}
        } catch (Exception ex) {
        }
        return jo;
    }

}
