package com.ngnsoft.ngp.component.dragon.model;

import com.ngnsoft.ngp.model.ClanUser;

public class DragonClanUser extends ClanUser {

	@Override
	public String getDbName() {
		return "dragon";
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(role == null) {
			role = ROLE_MEMBER;
		}
	}
	
}
