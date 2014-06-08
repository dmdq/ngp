package com.ngnsoft.ngp.service;

import java.io.IOException;

import org.json.JSONException;

import com.ngnsoft.ngp.model.LoginUser;

public interface LoginUserService extends GenericManager {

	public LoginUser getLoginUser(LoginUser user);
    
    public void updateUser(LoginUser user) throws JSONException, IOException;
    
}
