package com.ngnsoft.ngp.component.dragon.model;

import org.json.JSONObject;

import com.ngnsoft.ngp.model.ClanBase;

/**
 *
 * @author fcy
 */
public class DragonClan extends ClanBase {
	
	public static final int TYPE_ANY = 0;
	public static final int TYPE_INVITE = 1;
	public static final int TYPE_CLOSED = 2;
	
	public static final int STATUS_NORMAL = 0;
	public static final int STATUS_EXPIRED = 1;

	private Integer type;
	
	private Integer status;
	
	private String limit;
	
	private Integer maxMember;
	
	private Integer memberNum;
	
	private Integer level;

	public String getLimit() {
		return limit;
	}

	public void setLimit(String limit) {
		this.limit = limit;
	}

	public Integer getMaxMember() {
		return maxMember;
	}

	public void setMaxMember(Integer maxMember) {
		this.maxMember = maxMember;
	}

	public Integer getMemberNum() {
		return memberNum;
	}

	public void setMemberNum(Integer memberNum) {
		this.memberNum = memberNum;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	@Override
	public String getDbName() {
		return "dragon";
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(status == null) {
			status = STATUS_NORMAL;
		}
	}
	
	@Override
	public JSONObject toJSONObject() {
		JSONObject jsonObj = super.toJSONObject();
		if(limit != null) {
			JSONObject limitObj = new JSONObject(limit);
			jsonObj.put("limit", limitObj);
		}
		return jsonObj;
	}
	
}
