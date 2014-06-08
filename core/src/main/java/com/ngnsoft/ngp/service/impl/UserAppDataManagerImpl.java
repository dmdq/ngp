package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.slots.model.SlotsData;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.UserAppDataManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

@Service("userAppDataManager")
public class UserAppDataManagerImpl extends GenericManagerImpl implements
		UserAppDataManager {
	
	private Logger LOGGER = Logger.getLogger(UserAppDataManagerImpl.class);
	
	@Autowired
	private DataManager dataManager;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private RedisImpl redisImpl;
	
	protected static final int REDIS_COC_TOP_TIME_OUT = 30 * 60;
	
	protected static final int REDIS_SLOTS_TOP_TIME_OUT = 60;
	
	@Override
	public Response executeUserAppDataByAction(Request req, App app)
			throws JSONException {
		JSONObject jo = req.getBizData();
        UserAppData searchObj = generateUserAppData(req.getUserId(), app);
        List<UserAppData> userAppDatas = findMulti(searchObj);
        UserAppData userData = userAppDatas == null || userAppDatas.size() == 0 ? null : userAppDatas.get(0);
        
        //del,get,upd
    	JSONObject resultObj = getOrUpdateOrDel(jo, userData, req.getUserId(), app, req.getMacId());
    	Response res = new Response(Response.YES, resultObj);
        return res;
	}
	
	protected JSONObject getOrUpdateOrDel(JSONObject jo, UserAppData userAppData, Long userId, App app, String deviceId) throws JSONException {
    	JSONObject getJSON = new JSONObject();
    	boolean flag = true;
        if (userAppData == null) {
        	flag = false;
        	userAppData = generateUserAppData(userId, app);
        }
        JSONObject appDataJson = userAppData.toJSONObject() == null ? new JSONObject() : userAppData.toJSONObject();
        //del
        if(jo.has(AppComponent.REQ_DEL_ACTION)) {
        	dataManager.delJsonData(jo, appDataJson);
        }
        //upd
        if(jo.has(AppComponent.REQ_UPDATE_ACTION)) {
        	dataManager.updateJsonData(jo, appDataJson);
        }
        //get
        if(jo.has(AppComponent.REQ_GET_ACTION)) {
        	getJSON = dataManager.getDataFromJsonData(jo, appDataJson, AppComponent.REQ_GET_ACTION);
        }
        Date curDate = new Date();
        if(jo.has(AppComponent.REQ_UPDATE_ACTION) || jo.has(AppComponent.REQ_DEL_ACTION)) {
        	UserAppData newUserAppData = generateUserAppData(userId, app);
            newUserAppData.setId(userAppData.getId());
            newUserAppData.setUserId(userId);
            newUserAppData.setUlld(deviceId);
            newUserAppData.setBaid(app.getBaseId());
            if(appDataJson.has(UserAppData.TOUCH_TIME_KEY)) {
            	appDataJson.remove(UserAppData.TOUCH_TIME_KEY);
        	}
            if(appDataJson.has(UserAppData.ACTION_TIME_KEY)) {
				appDataJson.remove(UserAppData.ACTION_TIME_KEY);
			}
            if(userAppData.getUserId() != null) {
            	newUserAppData.setJsonData(appDataJson.toString());
            } else if (appDataJson.keys().hasNext()) {
            	newUserAppData.setJsonData(appDataJson.toString());
            }
            newUserAppData.setTouchTime(curDate);
            newUserAppData.setActionTime(curDate);
            saveOrUpdate(newUserAppData);
            getJSON.put(UserAppData.TOUCH_TIME_KEY, (newUserAppData.getTouchTime().getTime()/1000) * 1000);
        } else {
        	if(flag) {
        		UserAppData newUserData = generateUserAppData(userId, app);
	   	         newUserData.setUserId(userId);
	   	         newUserData.setBaid(app.getBaseId());
	   	         newUserData.setActionTime(curDate);
	   	         update(newUserData);
	   	         getJSON.put(UserAppData.TOUCH_TIME_KEY, (userAppData.getTouchTime().getTime() / 1000) * 1000);
        	}
	    }
        
        return getJSON;
    }
	
	private static UserAppData generateUserAppData(Long userId, App app) {
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		UserAppData appData = component.getUadModel(userId);
		appData.setDbName(component.getDbName());
    	return appData;
	}
	
	protected Response getUserTop(Request req, UserAppData appData){
		JSONObject resultObj = new JSONObject();
		String key = "";
		if(appData instanceof UserData) {
			key = CocComponent.REDIS_COC_BASE_ID_TOP_KEY;
		}
		try {
			if(redisImpl.exist(key)) {
				resultObj = new JSONObject((String)redisImpl.get(key));
				//update appData's actionTime
				appData.setActionTime(new Date());
				update(appData);
				resultObj.put(UserData.TOUCH_TIME_KEY, (appData.getTouchTime().getTime()/1000) * 1000);
			} else {
				// get trophy
				App app = (App)get(req.getAppId(), App.class);

				JSONArray ret = new JSONArray();
				Map getParam = new HashMap();
				Long count = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("count") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getLong("count") : 100l;
				String type = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("type") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getString("type") : "trophy";
				Long userData = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("userData") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getLong("userData") : 0l;
				
				getParam.put("count", count);
				getParam.put("type", type);
				if(appData instanceof UserData) {
					List<UserData> userDatas  = findByNamedQuery("find_trophy_by_coc_user", getParam, UserData.class);
					if(userDatas.size() != 0) {
						for (int i = 0; i < userDatas.size(); i++) {
							JSONObject json = new JSONObject();

							if(userData > 0) {
								JSONArray ja = userManager.getUserData(String.valueOf(userDatas.get(i).getUserId()));
								JSONObject ud = ja.getJSONObject(0);
								ud.put("userId", ud.getLong("userId"));
								json.put("userData", ud);
							}
							if(req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("appData")) {
								UserAppData userAppData  = (UserData)get(userDatas.get(i).getUserId(), UserData.class);
								if(userAppData != null && userAppData.getJsonData() != null && req.getJSONObject(CocComponent.REQ_TOP_ACTION).getJSONArray("appData").length() > 0) {
									JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(CocComponent.REQ_TOP_ACTION), new JSONObject(userAppData.getJsonData()), "appData");
									json.put("appData", jsonObj);
								}
							}
							if(type.equals("trophy")) {
								json.put("amount", userDatas.get(i).getTrophy());
							}
							if(type.equals("coin")) {
								json.put("amount", userDatas.get(i).getCoin());
							}
							if(type.equals("level")) {
								json.put("amount", userDatas.get(i).getLevel());
							}
							if(type.equals("town_level")) {
								json.put("amount", userDatas.get(i).getTownLevel());
							}
							json.put("userId", userDatas.get(i).getUserId());
							ret.put(json);
						}
					}
				} else if(appData instanceof DragonData) {
					List<DragonData> dragonDatas  = findByNamedQuery("find_trophy_by_dragon_user", getParam, DragonData.class);
					if(dragonDatas.size() != 0) {
						for (int i = 0; i < dragonDatas.size(); i++) {
							JSONObject json = new JSONObject();

							if(userData > 0) {
								JSONArray ja = userManager.getUserData(String.valueOf(dragonDatas.get(i).getUserId()));
								JSONObject ud = ja.getJSONObject(0);
								ud.put("userId", ud.getLong("userId"));
								json.put("userData", ud);
							}
							if(req.getJSONObject(DragonComponent.REQ_TOP_ACTION).has("appData")) {
								UserAppData userAppData  = (DragonData)get(dragonDatas.get(i).getUserId(), DragonData.class);
								if(userAppData != null && userAppData.getJsonData() != null && req.getJSONObject(DragonComponent.REQ_TOP_ACTION).getJSONArray("appData").length() > 0) {
									JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(DragonComponent.REQ_TOP_ACTION), new JSONObject(userAppData.getJsonData()), "appData");
									json.put("appData", jsonObj);
								}
							}
							if(type.equals("coin")) {
								json.put("amount", dragonDatas.get(i).getCoin());
							}
							if(type.equals("level")) {
								json.put("amount", dragonDatas.get(i).getLevel());
							}
							json.put("userId", dragonDatas.get(i).getUserId());
							ret.put(json);
						}
					}
				} else if(appData instanceof SlotsData) {
					
				}
				
				//update appData's actionTime
				appData.setActionTime(new Date());
				update(appData);
				resultObj.put(UserData.TOUCH_TIME_KEY, (appData.getTouchTime().getTime()/1000) * 1000);
				resultObj.put("data", ret);
				
				//put data to redis
				if(resultObj.length() > 0)
					redisImpl.set(key, REDIS_COC_TOP_TIME_OUT, resultObj.toString());
			}
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
		return new Response(Response.YES, resultObj);
	}
	
}
