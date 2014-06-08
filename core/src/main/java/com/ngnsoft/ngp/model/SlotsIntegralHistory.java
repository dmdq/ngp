package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

public class SlotsIntegralHistory extends BaseModel {

	private Long id;
	
	private Long userId;
	
	private String weekNum;
	
	private Integer integral;
	
	private Integer eligible;
	
	private Integer ranking;
	
	private String jsonData;
	
	private String activityData;
	
	private Date createTime;
	
	private Integer collectStatus;
	
	private Date collectTime;
	
	
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

	public String getActivityData() {
		return activityData;
	}

	public void setActivityData(String activityData) {
		this.activityData = activityData;
	}

	public Integer getEligible() {
		return eligible;
	}

	public void setEligible(Integer eligible) {
		this.eligible = eligible;
	}

	public Integer getRanking() {
		return ranking;
	}

	public void setRanking(Integer ranking) {
		this.ranking = ranking;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getWeekNum() {
		return weekNum;
	}

	public void setWeekNum(String weekNum) {
		this.weekNum = weekNum;
	}

	public Integer getIntegral() {
		return integral;
	}

	public void setIntegral(Integer integral) {
		this.integral = integral;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Override
	public Serializable getPrimaryKey() {
		return (Long)id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(integral == null) integral = 0;
		if(collectTime == null) collectTime = new Date();
	}
	
	@Override
	public String getDbName() {
		return "slots";
	}
	
	@Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
        	if(jsonData == null) {
        		jo = new JSONObject();
        	} else {
        		jo = new JSONObject(jsonData);
        	}
        } catch (Exception ex) {
        }
        return jo;
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
