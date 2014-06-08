package com.ngnsoft.ngp.service;

import org.json.JSONException;
import org.json.JSONObject;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;

/**
 *
 * @author fcy
 */
public interface MessageManager extends GenericManager {
    
	public Response getMessageByPage(Request req, int currentPage, int itemsPerPage) throws JSONException;
    
	public Response rdms(Request req, JSONObject readParam) throws JSONException;
	
	public Response dlms(Request req, JSONObject delParam) throws JSONException;
	
	public Response mtuf(Request req, JSONObject sendParam) throws JSONException;
}
