package com.ngnsoft.ngp.service;

import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.ngnsoft.ngp.enums.DataType;
import com.ngnsoft.ngp.misc.PushNotify;
import com.ngnsoft.ngp.model.App;


public interface DataManager extends GenericManager {
	
	public final String JSON_KEY_REGEX = "\\.";
	
	boolean push(Long userId, App app, PushNotify pn) throws Exception;

	Object getDataByType(DataType type, Map<String, Object> queryMap);
	
	JSONObject getDataFromJsonData(JSONObject fromJsonObj, JSONObject appDataJson, String cmdType) throws JSONException;
	
	void updateJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException;
	
	void incrJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException;
	
	void delJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException;
        
    JSONObject fetchUserData(JSONObject appDataJson, JSONArray appDataKeys);
}
