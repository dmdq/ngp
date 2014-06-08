package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.component.coc.model.UserData;


public interface UserDataManager extends UserAppDataManager {
    
    public boolean lockTimeout(UserData ud);
	
}
