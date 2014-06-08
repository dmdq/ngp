package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

@Service("dragonDataManager")
public class DragonDataManagerImpl extends UserAppDataManagerImpl {
	
	private static Logger LOGGER = Logger.getLogger(DragonDataManagerImpl.class);

	public static final String DRAGON_BASE_INFO_KEY = "MyBaseInfor";
	
	public static final String USER_MAIN_SOLDIER_KEY = "MainSoldier";
	
	public static final String DRAGON_HELP_POINT_KEY = "helpspoint"; 

	public static final String DRAGON_COIN_KEY = "goldnum";

	public static final String DRAGON_LEVEL_KEY = "rolelev";
	
	public static final String DRAGON_NICK_KEY = "rolename";
	
	public static final String UPLOAD_BATTLE_USER_DATA_KEY = "userData";
	
	public static final String UPLOAD_BATTLE_FRD_DATA_KEY = "frdData";
	
	public static final String UPLOAD_BATTLE_IS_RFD_KEY = "isFrd";
	
	public static final String UPLOAD_BATTLE_FRD_ID_KEY = "id";
	
	public static final int RISKER_HELP_POINT = 5;
	
	public static final int FRIEND_HELP_POINT = 10;
	
	@Value("${search.facilitator.interval}")
	private Integer validInterval; 

	@Autowired
	private DataManager dataManager;
	
	@Autowired
	private RedisImpl redisImpl;

	@Override
	public Response executeUserAppDataByAction(Request req, App app)
			throws JSONException {
		Response res = null;
		JSONObject jo = req.getBizData();
		DragonData searchObj = new DragonData();
		searchObj.setUserId(req.getUserId());
		searchObj.setBaid(app.getBaseId());
		List<DragonData> userAppDatas = findMulti(searchObj);
		DragonData userData = userAppDatas == null || userAppDatas.size() == 0 ? null : userAppDatas.get(0);
		if(jo.has(DragonComponent.REQ_RESULT_ACTION)) {
			if(userData != null) {
				if(!jo.getJSONObject(DragonComponent.REQ_RESULT_ACTION).has(UPLOAD_BATTLE_USER_DATA_KEY)) {
					res = new Response(Response.NO, "NO_userData");
				} else if(!jo.getJSONObject(DragonComponent.REQ_RESULT_ACTION).has(UPLOAD_BATTLE_FRD_DATA_KEY)) {
					res = new Response(Response.NO, "NO_frdData");
				} else {
					JSONObject userJsonData = jo.getJSONObject(DragonComponent.REQ_RESULT_ACTION).getJSONObject(UPLOAD_BATTLE_USER_DATA_KEY);
					JSONObject frdData = jo.getJSONObject(DragonComponent.REQ_RESULT_ACTION).getJSONObject(UPLOAD_BATTLE_FRD_DATA_KEY);
					JSONObject resultObj = uploadBattle(userData, userJsonData, frdData);
					res = new Response(Response.YES, resultObj);
				}
			} else {
				res = new Response(Response.NO, "NO_USER");
			}
		} else if(jo.has(CocComponent.REQ_TOP_ACTION)) {
			// get trophy
			res = getUserTop(req, userData);
		} else {
			// del,get,upd
			JSONObject resultObj = getOrUpdateOrDel(jo, userData, req.getUserId(), app, req.getMacId());
			res = new Response(Response.YES, resultObj);
		}
		return res;
	}
	
	private JSONObject uploadBattle(DragonData userAppData, JSONObject userJsonData, JSONObject frdJsonData) {
		int isFrd = frdJsonData.getInt(UPLOAD_BATTLE_IS_RFD_KEY);
		int point = isFrd > 0 ? FRIEND_HELP_POINT : RISKER_HELP_POINT;
		Long frdId = frdJsonData.getLong(UPLOAD_BATTLE_FRD_ID_KEY);
		String key = DragonComponent.REDIS_DRAGON_HELP_ID_PREFIX + userAppData.getUserId() + "_" + frdId; 
		if(isFrd > 0) {
			try {
				if(redisImpl.exist(key)) {
					point = 0;
				}
			} catch (Exception e) {
				LOGGER.error("Redis operate ERROR:", e);
			}
		}
		
		JSONObject updObj = new JSONObject();
		JSONObject updParamObj = new JSONObject();
		updObj.put(AppComponent.REQ_UPDATE_ACTION, updParamObj);
		
		JSONObject appDataJson = userAppData.toJSONObject() == null ? new JSONObject() : userAppData.toJSONObject();
		dataManager.updateJsonData(userJsonData, appDataJson);
		
		dataManager.delJsonData(userJsonData, appDataJson);
		appDataJson.remove(DragonData.TOUCH_TIME_KEY);
		appDataJson.remove(DragonData.ACTION_TIME_KEY);
		userAppData.setJsonData(appDataJson.toString());
		resetUserDataFieldFromJsonData(userAppData, appDataJson);
		Date curDate = new Date();
		userAppData.setTouchTime(curDate);
		userAppData.setActionTime(curDate);
		update(userAppData);
		
		if(isFrd > 0) {
			try {
				String limitRepeatSrhKey = DragonComponent.REDIS_DRAGON_FRD_ID_PREFIX + userAppData.getUserId() + "_" + frdId;
				redisImpl.set(limitRepeatSrhKey, validInterval, frdId);
			} catch (Exception e1) {
				LOGGER.error("Redis operate ERROR:", e1);
			}
		}
		
		if(point > 0) {
			int userPoint = getUserHelpspoint(appDataJson) + point;
			updParamObj.put(DRAGON_BASE_INFO_KEY + "." + DRAGON_HELP_POINT_KEY, userPoint);
			dataManager.updateJsonData(updObj, appDataJson);
			
			if(frdId > DragonComponent.ROBOT_IDENTIFY_VAL) {
				DragonData frdData = get(frdId, DragonData.class);
				updParamObj.put(DRAGON_BASE_INFO_KEY + "." + DRAGON_HELP_POINT_KEY, getUserHelpspoint(frdData.toJSONObject()) + point);
				JSONObject frdDataJson = frdData.toJSONObject() == null ? new JSONObject() : frdData.toJSONObject();
				dataManager.updateJsonData(updObj, frdDataJson);
				
				frdDataJson.remove(DragonData.TOUCH_TIME_KEY);
				frdDataJson.remove(DragonData.ACTION_TIME_KEY);
				frdData.setJsonData(frdDataJson.toString());
				update(frdData);
			}
		}
		if(point > 0 && isFrd > 0) {
			try {
				redisImpl.set(key, frdId);
			} catch (Exception e) {
				LOGGER.error("Redis operate ERROR:", e);
			}
		}
		
		JSONObject resultObj = new JSONObject();
		resultObj.put(UserData.TOUCH_TIME_KEY, (userAppData.getTouchTime().getTime()/1000) * 1000);
		return resultObj;
	}
	
	private int getUserHelpspoint(JSONObject dragonData) {
		if (dragonData.getJSONObject(DRAGON_BASE_INFO_KEY).has(DRAGON_HELP_POINT_KEY)) {
			int point = dragonData.getJSONObject(DRAGON_BASE_INFO_KEY).getInt(
					DRAGON_HELP_POINT_KEY);
			return point;
		} else {
			return 0;
		}
	}

	protected JSONObject getOrUpdateOrDel(JSONObject jo,
			DragonData userAppData, Long userId, App app, String deviceId) throws JSONException {
		JSONObject getJSON = new JSONObject();
		boolean flag = true;
		if (userAppData == null) {
			userAppData = new DragonData();
			flag = false;
		}
		JSONObject appDataJson = userAppData.toJSONObject() == null ? new JSONObject()
				: userAppData.toJSONObject();
		// del
		if (jo.has(AppComponent.REQ_DEL_ACTION)) {
			dataManager.delJsonData(jo, appDataJson);
		}
		// upd
		if (jo.has(AppComponent.REQ_UPDATE_ACTION)) {
			dataManager.updateJsonData(jo, appDataJson);
		}
		// get
		if (jo.has(AppComponent.REQ_GET_ACTION)) {
			getJSON = dataManager.getDataFromJsonData(jo, appDataJson,
					AppComponent.REQ_GET_ACTION);
		}
		Date curDate = new Date();
		if (jo.has(AppComponent.REQ_UPDATE_ACTION) || jo.has(AppComponent.REQ_DEL_ACTION)) {
			DragonData newUserAppData = new DragonData();
			newUserAppData.setUlld(deviceId);
			newUserAppData.setId(userAppData.getId());
			newUserAppData.setUserId(userId);
			newUserAppData.setBaid(app.getBaseId());
			resetUserDataFieldFromJsonData(newUserAppData, appDataJson);
			if (appDataJson.has(UserAppData.TOUCH_TIME_KEY)) {
				appDataJson.remove(UserAppData.TOUCH_TIME_KEY);
			}
			if(appDataJson.has(UserAppData.ACTION_TIME_KEY)) {
				appDataJson.remove(UserAppData.ACTION_TIME_KEY);
			}
			if (userAppData.getUserId() != null) {
				newUserAppData.setJsonData(appDataJson.toString());
			} else if (appDataJson.keys().hasNext()) {
				newUserAppData.setJsonData(appDataJson.toString());
			}
			newUserAppData.setTouchTime(curDate);
			newUserAppData.setActionTime(curDate);
			saveOrUpdate(newUserAppData);
			getJSON.put(UserAppData.TOUCH_TIME_KEY, (newUserAppData.getTouchTime().getTime() / 1000) * 1000);
		} else {
			if(flag) {
				DragonData newUserData = new DragonData();
				newUserData.setUserId(userId);
				newUserData.setBaid(app.getBaseId());
				newUserData.setActionTime(curDate);
				update(newUserData);
				getJSON.put(DragonData.TOUCH_TIME_KEY, (userAppData.getTouchTime()
						.getTime() / 1000) * 1000);
			}
		}

		return getJSON;
	}

	public void resetUserDataFieldFromJsonData(DragonData userData,
			JSONObject jsonData) throws JSONException {
		if (userData.getUserId() < 100000) {
			if (jsonData.getJSONObject(USER_MAIN_SOLDIER_KEY).has(DRAGON_LEVEL_KEY)) {
				userData.setLevel(jsonData.getJSONObject(USER_MAIN_SOLDIER_KEY)
						.getInt(DRAGON_LEVEL_KEY));
			} else {
				userData.setLevel(0);
			}
			if (jsonData.getJSONObject(USER_MAIN_SOLDIER_KEY).has(DRAGON_NICK_KEY)) {
				userData.setNick(jsonData.getJSONObject(USER_MAIN_SOLDIER_KEY)
						.getString(DRAGON_NICK_KEY));
			} else {
				userData.setNick(null);
			}
		} else {
			if (jsonData.getJSONObject(DRAGON_BASE_INFO_KEY).has(DRAGON_COIN_KEY)) {
				Long coin = jsonData.getJSONObject(DRAGON_BASE_INFO_KEY).getLong(
						DRAGON_COIN_KEY);
				userData.setCoin(coin);
			} else {
				userData.setCoin(0l);
			}
			if (jsonData.getJSONObject(DRAGON_BASE_INFO_KEY).has(DRAGON_LEVEL_KEY)) {
				userData.setLevel(jsonData.getJSONObject(DRAGON_BASE_INFO_KEY)
						.getInt(DRAGON_LEVEL_KEY));
			} else {
				userData.setLevel(0);
			}
			if (jsonData.getJSONObject(DRAGON_BASE_INFO_KEY).has(DRAGON_NICK_KEY)) {
				userData.setNick(jsonData.getJSONObject(DRAGON_BASE_INFO_KEY)
						.getString(DRAGON_NICK_KEY));
			} else {
				userData.setNick(null);
			}
		}
	}
	
}
