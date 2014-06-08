package com.ngnsoft.ngp.component.dragon.model;

import org.springframework.util.ClassUtils;

import com.ngnsoft.ngp.model.UserFriend;

public class DragonUserFriend extends UserFriend {
	
	@Override
	public String getModelName() {
		return ClassUtils.getShortName(this.getClass().getSuperclass());
	}

	@Override
	public String getDbName() {
		return "dragon";
	}
	
	@Override
	public String getTableName() {
		return "user_friend";
	}

}
