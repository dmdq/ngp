package com.ngnsoft.ngp.service;

import org.json.JSONException;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;

/**
 *
 * @author fcy
 */
public interface DragonManager extends GenericManager {
    
	Response executeAppMessageByAction(Request req) throws JSONException;
    
}
