package com.ngnsoft.ngp.model;

import java.io.Serializable;

public class UserRole extends BaseModel {
	
	private Long userId;
	
	private Long roleId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	@Override
	public Serializable getPrimaryKey() {
		return userId;
	}

	@Override
	public void setPrimaryKey(Object o) {
		userId = (Long)o; 
	}

}
