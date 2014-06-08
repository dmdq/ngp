package com.ngnsoft.ngp.service;

import org.json.JSONException;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;

public interface AppMessageManager {
	
	public Response getMessageByCondition(Request req, App app) throws JSONException;
	
	public Response rdms(Request req) throws JSONException;
	
	public Response mtuf(Request req) throws JSONException;

	public Response dlms(Request req) throws JSONException;
}
