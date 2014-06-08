package com.ngnsoft.ngp.component.slots.model;

import java.io.Serializable;

import com.ngnsoft.ngp.model.BaseModel;

public class Event extends BaseModel {

	public static final int CUR_STATUS_NORMAL = 1;
	public static final int CUR_STATUS_CLOSED = 0;
	public static final int CUR_TYPE_INTEGRAL = 0;
	public static final int NEXT_STATUS_NORMAL = 1;
	public static final int NEXT_STATUS_CLOSED = 0;
	public static final int NEXT_TYPE_COLLECT_TIME= 1;
	
	private Integer id;
	
	private String curUnit;
	
	private String nextUnit;
	
	private Integer curTotalHours;
	
	private Integer nextTotalHours;
	
	private Long bakStartTime;
	
	private Long refreshStartTime;
	
	private Integer period;
	
	private Integer curStatus;
	
	private Integer nextStatus;
	
	private Integer curType;
	
	private Integer nextType;
	
	private Integer lastType;
	
	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (Integer)o;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getCurTotalHours() {
		return curTotalHours;
	}

	public Integer getLastType() {
		return lastType;
	}

	public void setLastType(Integer lastType) {
		this.lastType = lastType;
	}

	public Integer getCurType() {
		return curType;
	}

	public void setCurType(Integer curType) {
		this.curType = curType;
	}

	public Integer getNextType() {
		return nextType;
	}

	public void setNextType(Integer nextType) {
		this.nextType = nextType;
	}

	public void setCurTotalHours(Integer curTotalHours) {
		this.curTotalHours = curTotalHours;
	}

	public Integer getNextTotalHours() {
		return nextTotalHours;
	}

	public void setNextTotalHours(Integer nextTotalHours) {
		this.nextTotalHours = nextTotalHours;
	}

	public String getCurUnit() {
		return curUnit;
	}

	public void setCurUnit(String curUnit) {
		this.curUnit = curUnit;
	}

	public String getNextUnit() {
		return nextUnit;
	}

	public void setNextUnit(String nextUnit) {
		this.nextUnit = nextUnit;
	}

	public Long getBakStartTime() {
		return bakStartTime;
	}

	public void setBakStartTime(Long bakStartTime) {
		this.bakStartTime = bakStartTime;
	}

	public Long getRefreshStartTime() {
		return refreshStartTime;
	}

	public void setRefreshStartTime(Long refreshStartTime) {
		this.refreshStartTime = refreshStartTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public String getDbName() {
		return "slots";
	}

	public Integer getCurStatus() {
		return curStatus;
	}

	public void setCurStatus(Integer curStatus) {
		this.curStatus = curStatus;
	}

	public Integer getNextStatus() {
		return nextStatus;
	}

	public void setNextStatus(Integer nextStatus) {
		this.nextStatus = nextStatus;
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(curStatus == null) curStatus = CUR_STATUS_NORMAL;
		if(nextStatus == null) nextStatus = NEXT_STATUS_NORMAL;
		if(curType == null) curType = CUR_TYPE_INTEGRAL;
	}
}
