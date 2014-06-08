package com.ngnsoft.ngp.service;

import java.util.Iterator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.ngnsoft.ngp.model.UserProfile;

public class TestJson {

static final String JSON_KEY_REGEX = "\\.";
    
	static final String DEL_KEY = "del";
	static final String SAVE_OR_UPDATE_KEY = "update";
    
	static final String USER_PROFILE_JSONALL_KEY = "jsonAll";

	/**
	 * @param args
	 * @throws JSONException 
	 */
	public static void main(String[] args) throws JSONException {
		
		
		// TODO Auto-generated method stub
		UserProfile profile = new UserProfile();
		profile.setEmail("michael");
		profile.setJsonAll("{\"e\":3}");
		
		JSONObject jsonAll = new JSONObject(profile);
		
		String json = "{\"update\":{\"a.b.c\":[{\"id\":5}], \"a.b\":{\"email\":\"3\"}}, \"del\":[\"e\", \"a.x\"]}}";
		JSONObject requestObj = new JSONObject(json);
		execute(requestObj, jsonAll);
		requestObj.remove("123");
	
		profile = JSON.parseObject(jsonAll.toString(), UserProfile.class);
		
		System.out.println(jsonAll);
		
	}
	
	private static void execute(JSONObject jsonObj, JSONObject profileJson) throws JSONException {
    	JSONArray delArray = jsonObj.has(DEL_KEY) ? (JSONArray)jsonObj.getJSONArray(DEL_KEY) : null;
    	JSONObject updateObj = jsonObj.has(SAVE_OR_UPDATE_KEY) ? (JSONObject)jsonObj.get(SAVE_OR_UPDATE_KEY) : null;
    	JSONObject jsonAllObj = profileJson.has(USER_PROFILE_JSONALL_KEY) && profileJson.get(USER_PROFILE_JSONALL_KEY) != null ? new JSONObject((String)profileJson.get(USER_PROFILE_JSONALL_KEY)) : new JSONObject("{}");
    	if(delArray != null) {
    		for(int i = 0; i < delArray.length(); i++) {
    			String key = (String)delArray.get(i);
    			String[] subKeys = key.split(JSON_KEY_REGEX);
    			putOrRemove(subKeys, null, jsonAllObj, profileJson, 0, key, DEL_KEY);
    		}
    	}
    	if(updateObj != null) {
    		Iterator<?> it = updateObj.keys();
    		while(it.hasNext()) {
    			String key = (String)it.next();
    			String[] subKeys = key.split(JSON_KEY_REGEX);
    			putOrRemove(subKeys, updateObj, jsonAllObj, profileJson, 0, key, SAVE_OR_UPDATE_KEY);
    		}
    	}
    	if(jsonAllObj.keys().hasNext()) {
    		profileJson.put(USER_PROFILE_JSONALL_KEY, jsonAllObj);
    	}
	}
	
	private static void putOrRemove(String[] subKeys, JSONObject jsonObj, JSONObject jsonAllObj, JSONObject profileJson, int index, String key, String action) throws JSONException {
		if(index < subKeys.length - 1) {
			if(!jsonAllObj.has(subKeys[index])) {
				if(!action.equals(DEL_KEY)) {
					jsonAllObj.put(subKeys[index], new JSONObject("{}"));
				} else {
					return;
				}
			} 
			index ++;
			putOrRemove(subKeys, jsonObj, (JSONObject)jsonAllObj.get(subKeys[index - 1]), profileJson, index, key, action);
		} else {
			if(action.equals(SAVE_OR_UPDATE_KEY)) {
				Object subObj =  jsonObj.get(key);
				if(profileJson.has(subKeys[index])) {
					profileJson.put(subKeys[index], subObj);
				}
				jsonAllObj.put(subKeys[index], subObj);
			} else {
				if(profileJson.has(subKeys[index])) {
					profileJson.putOnce(subKeys[index], null);
				}
				jsonAllObj.remove(subKeys[index]);
			}
		}
	}

}
