package com.ngnsoft.ngp.service.impl;

import java.io.IOException;

import org.json.JSONException;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.LoginUser;
import com.ngnsoft.ngp.service.LoginUserService;

@Service
public class LoginUserServiceImpl extends GenericManagerImpl implements
		LoginUserService {

	@Override
	public LoginUser getLoginUser(LoginUser user) {
		LoginUser u = (LoginUser)findObject(user);
		return u;
	}

	@Override
	public void updateUser(LoginUser user) throws JSONException,
			IOException {
	}


}
