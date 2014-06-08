package com.ngnsoft.ngp.service;

import org.json.JSONException;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;


public interface UserAppDataManager extends GenericManager {
	
    Response executeUserAppDataByAction(Request req, App app) throws JSONException;
	
}
