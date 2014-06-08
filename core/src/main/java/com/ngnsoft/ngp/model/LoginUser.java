package com.ngnsoft.ngp.model;

import java.io.Serializable;
import java.util.Date;

/**
*
* @author michael.li
*/
public class LoginUser extends BaseModel {
	
	private Long id;
	
	private String username;
	
	private String password;
	
	private Integer type;
	
	private Integer status;
	
	private Date lastLoginTime;
	
	@Override
	public String getTableName() {
		return tableName;
	}
	
	@Override
	public Serializable getPrimaryKey() {
		return id;
	}

	@Override
	public void setPrimaryKey(Object o) {
		this.id = (Long)o;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Date getLastLoginTime() {
		return lastLoginTime;
	}

	public void setLastLoginTime(Date lastLoginTime) {
		this.lastLoginTime = lastLoginTime;
	}
	
	@Override
	public String getDbName() {
		return "ngp_user";
	}

}
