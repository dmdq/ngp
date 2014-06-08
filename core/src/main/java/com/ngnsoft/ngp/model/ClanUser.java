package com.ngnsoft.ngp.model;

import java.io.Serializable;

public abstract class ClanUser extends BaseModel {
	
	public static final int ROLE_ELDER = 1;
	
	public static final int ROLE_PRESIDENT = 2;
	
	public static final int ROLE_MEMBER = 0;
	
	protected Long id;
	
	protected Long clanId;
	
	protected Long userId;
	
	protected Integer role;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getClanId() {
		return clanId;
	}

	public void setClanId(Long clanId) {
		this.clanId = clanId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Integer getRole() {
		return role;
	}

	public void setRole(Integer role) {
		this.role = role;
	}

	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		id = (Long)o;
	}

}
