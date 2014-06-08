package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;

public interface CocManager {
	
	public Response getMessageByCondition(Request req, App app);
	
}
