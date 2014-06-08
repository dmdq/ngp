package com.ngnsoft.ngp.factory;

import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.enums.Game;
import com.ngnsoft.ngp.model.UserAppData;

public class UserAppDataFactory {
	
	public static UserAppData getUserAppData(String name) {
		if(Game.COC.value().equalsIgnoreCase(name)) {
			return new UserData();
		} else if(Game.DRAGON.value().equalsIgnoreCase(name)) {
			return new DragonData();
		} 
		
		return null;
	}

}
