package com.ngnsoft.ngp.component.coc.model;

import java.io.Serializable;
import java.util.Date;

import org.json.JSONObject;

import com.ngnsoft.ngp.model.BaseModel;


public class BattleLog extends BaseModel {
	
	public static final int UNREVENGE_TYPE = 1;
	
	public static final int REVENGE_TYPE = 0;
	
	public static final int ONLY_REVENGE_TYPE = -1;
	
	private Long id;
	
	private Long attacker;
	
	private Long defenser;
	
	private Integer trophy;
	
	private Integer type;
	
	private Long duration;
	
	private String baseData;
	
	private String jsonData;
	
	private Date battleTime;
	
	private Long tm;

	public Integer getTrophy() {
		return trophy;
	}

	public void setTrophy(Integer trophy) {
		this.trophy = trophy;
	}

	public String getBaseData() {
		return baseData;
	}

	public void setBaseData(String baseData) {
		this.baseData = baseData;
	}

	public Long getDuration() {
		return duration;
	}

	public void setDuration(Long duration) {
		this.duration = duration;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttacker() {
		return attacker;
	}

	public void setAttacker(Long attacker) {
		this.attacker = attacker;
	}

	public Long getDefenser() {
		return defenser;
	}

	public void setDefenser(Long defenser) {
		this.defenser = defenser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getJsonData() {
		return jsonData;
	}

	public void setJsonData(String jsonData) {
		this.jsonData = jsonData;
	}

	public Date getBattleTime() {
		return battleTime;
	}

	public void setBattleTime(Date battleTime) {
		this.battleTime = battleTime;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}
	
	public Long getTm() {
		return tm;
	}

	public void setTm(Long tm) {
		this.tm = tm;
	}

	@Override
	public String getDbName() {
		return "coc";
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject jo = null;
		try {
			jo = new JSONObject(jsonData);
		} catch (Exception ex) {
		}
		return jo;
	}

}
