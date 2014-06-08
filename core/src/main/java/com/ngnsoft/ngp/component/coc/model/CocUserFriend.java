package com.ngnsoft.ngp.component.coc.model;

import org.springframework.util.ClassUtils;

import com.ngnsoft.ngp.model.UserFriend;

public class CocUserFriend extends UserFriend {
	
	@Override
	public String getModelName() {
		return ClassUtils.getShortName(this.getClass().getSuperclass());
	}

	@Override
	public String getDbName() {
		return "coc";
	}
	
	@Override
	public String getTableName() {
		return "user_friend";
	}

}
