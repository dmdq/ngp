package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.slots.model.DataCount;
import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.component.slots.model.SlotsBuy;
import com.ngnsoft.ngp.component.slots.model.SlotsData;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.SlotsIntegralHistory;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.SlotsDataManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

@Service("slotsDataManager")
public class SlotsDataManagerImpl extends UserAppDataManagerImpl implements
SlotsDataManager {

	private static Logger LOGGER = Logger.getLogger(SlotsDataManagerImpl.class);

	public static final String USER_BASE_INFO_KEY = "userData";

	public static final String USER_ACTIVITY_INFO_KEY = "activityData";

	public static final String USER_INTEGRAL_KEY = "activityScore";

	public static final String USER_PUZZLES_KEY = "activityPuzzles";

	public static final String USER_REWARDS_KEY = "rewards";

	public static final String INCR_TM_KEY = "tm";

	public static final String USER_NAME_KEY = "userName";

	public static final String USER_FACK_BOOK_NAME_KEY = "facebookName";

	public static final String USER_INTEGRAL = "integral";

	public static final String USER_COLLECT_TIME = "collect_time";

	public static final String ACTIVITY_INIT_DATA = "{\"activityData\":{\"activityPuzzles\":\"0|0|0|0|0|0\",\"activityScore\":0,\"rewards\":0}}";

	@Autowired
	private DataManager dataManager;

	@Autowired
	private UserManager userManager;

	@Autowired
	private RedisImpl redisImpl;

	Map<Long, AtomicLong> userLock = new ConcurrentHashMap<Long, AtomicLong>();

	@Override
	public Response executeUserAppDataByAction(Request req, App app)
			throws JSONException {
		AtomicLong lock = getUserLock(req.getUserId());
		synchronized (lock) {
			Response res = null;
			JSONObject jo = req.getBizData();
			SlotsData searchObj = new SlotsData();
			searchObj.setUserId(req.getUserId());
			searchObj.setBaid(app.getBaseId());
			List<SlotsData> userAppDatas = findMulti(searchObj);
			SlotsData userData = userAppDatas == null || userAppDatas.size() == 0 ? null : userAppDatas.get(0);
			if(jo.has(AppComponent.REQ_DATA_ACTION)){
				//getDataCount
				res = getDataCount(req, jo);
			} else if(jo.has(AppComponent.REQ_TOP_ACTION)){
				res = getUserTop(req, userData);
			}  else {
				// del,get,upd
				JSONObject resultObj = getOrUpdateOrDel(jo, userData, req.getUserId(), app, req.getMacId());
				res = new Response(Response.YES, resultObj);
			}
			return res;
		}
	}

	private synchronized AtomicLong getUserLock(Long userId) {
		AtomicLong lock = userLock.get(userId);
		if (lock == null) {
			lock = new AtomicLong(1);
			userLock.put(userId, lock);
		} else {
			lock.incrementAndGet();
		}
		return lock;
	}

	/**
	 * getDataCount
	 * @param req
	 * @param jo
	 * @return
	 */
	protected Response getDataCount(Request req, JSONObject jo){
		DataCount dataCount = new DataCount();
		if(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).has(AppComponent.REQ_TYPE_ACTION)){
			String type = jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString(AppComponent.REQ_TYPE_ACTION);
			if(type.equals(DataCount.TYPE_BUY_DATA)){
				SlotsBuy slotsBuy = new SlotsBuy();
				slotsBuy.setCoins(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s1"));
				slotsBuy.setLevel(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s2"));
				slotsBuy.setNick(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s3"));
				slotsBuy.setRank(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s4"));
				slotsBuy.setProductId(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s5"));
				slotsBuy.setBundleId(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s6"));
				slotsBuy.setUserId(req.getUserId());
				slotsBuy.setAppId(req.getAppId());
				save(slotsBuy);
				return new Response(Response.YES);
			} else if(type.equals(DataCount.TYPE_BASIS_DATA)){
				dataCount.setI1(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getLong("i1"));
			} else if(type.equals(DataCount.TYPE_LOGIN_DATA)){
				if(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).has("s1")){
					dataCount.setS1(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s1"));
				}
			} else if(type.equals(DataCount.TYPE_EXPEND_MONEY_DATA)){
				dataCount.setI1(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getLong("i1"));
				dataCount.setI2(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getLong("i2"));
			} else if(type.equals(DataCount.TYPE_USER_GET_DATA)){
				dataCount.setS1(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getString("s1"));
				dataCount.setI1(jo.getJSONObject(AppComponent.REQ_DATA_ACTION).getLong("i1"));
			} else {
				return new Response(Response.NO, "NO_TYPE_NULLITY");
			}
			dataCount.setUserId(req.getUserId());
			dataCount.setType(type);
			save(dataCount);
			return new Response(Response.YES);
		}
		return new Response(Response.NO, "NO_TYPE");
	}
	
	protected JSONObject getOrUpdateOrDel(JSONObject jo,
			SlotsData userAppData, Long userId, App app, String deviceId) throws JSONException {

		JSONObject getJSON = new JSONObject();
		if (userAppData == null) {
			userAppData = new SlotsData();
		}

		JSONObject appDataJson = userAppData.toJSONObject() == null ? new JSONObject() : userAppData.toJSONObject();
		JSONObject activityJson = userAppData.getActivityObject();

		JSONObject requestWrapperAppDataJson = new JSONObject();
		JSONObject requestWrapperActivityJson = new JSONObject();

		separateJSON(jo, requestWrapperAppDataJson, requestWrapperActivityJson);

		// del
		if (jo.has(AppComponent.REQ_DEL_ACTION)) {
			dataManager.delJsonData(requestWrapperAppDataJson, appDataJson);
			dataManager.delJsonData(requestWrapperActivityJson, activityJson);
		}
		// upd
		if (jo.has(AppComponent.REQ_UPDATE_ACTION)) {
			dataManager.updateJsonData(requestWrapperAppDataJson, appDataJson);
			dataManager.updateJsonData(requestWrapperActivityJson, activityJson);
		}
		//incr
		if(jo.has(AppComponent.REQ_INCR_ACTION)) {
			//do something
			Long tm = jo.has(INCR_TM_KEY) ? jo.getLong(INCR_TM_KEY) : 0l;
			if(tm > 0) {
				Event event = (Event)findObject(new Event());
				if(tm <= event.getRefreshStartTime()) {
					//in same week
					resetActivityForAppData(activityJson);
				}
			}
			dataManager.incrJsonData(requestWrapperAppDataJson, appDataJson);
			dataManager.incrJsonData(requestWrapperActivityJson, activityJson);

		}
		// get
		if (jo.has(AppComponent.REQ_GET_ACTION)) {
			getJSON = dataManager.getDataFromJsonData(requestWrapperAppDataJson, appDataJson, AppComponent.REQ_GET_ACTION);
			JSONObject getActivityJSON = dataManager.getDataFromJsonData(requestWrapperActivityJson, activityJson, AppComponent.REQ_GET_ACTION);
			if(getActivityJSON.has(USER_ACTIVITY_INFO_KEY)) {
				getJSON.put(USER_ACTIVITY_INFO_KEY, getActivityJSON.get(USER_ACTIVITY_INFO_KEY));
			}
		}
		Date curDate = new Date();
		if (jo.has(AppComponent.REQ_UPDATE_ACTION) || jo.has(AppComponent.REQ_DEL_ACTION)) {
			SlotsData newUserAppData = new SlotsData();
			newUserAppData.setUlld(deviceId);
			newUserAppData.setUserId(userId);
			newUserAppData.setBaid(app.getBaseId());
			resetUserDataFieldFromJsonData(newUserAppData, userAppData, appDataJson, activityJson);
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
			newUserAppData.setActivityData(activityJson.toString());

			newUserAppData.setTouchTime(curDate);
			newUserAppData.setActionTime(curDate);
			saveOrUpdate(newUserAppData);
			getJSON.put(UserAppData.TOUCH_TIME_KEY, (newUserAppData.getTouchTime().getTime() / 1000) * 1000);
		} else {
			SlotsData newUserData = new SlotsData();
			newUserData.setUserId(userId);
			newUserData.setBaid(app.getBaseId());
			newUserData.setActionTime(curDate);
			update(newUserData);
			getJSON.put(DragonData.TOUCH_TIME_KEY, (userAppData.getTouchTime().getTime() / 1000) * 1000);
		}

		return getJSON;
	}

	public void resetActivityForAppData(JSONObject jsonData) {
		if (jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_INTEGRAL_KEY)) {
			jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).put(USER_INTEGRAL_KEY, 0);
		} 

		if (jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_PUZZLES_KEY)) {
			jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).put(USER_PUZZLES_KEY, "0|0|0|0|0|0");
		} 

		if (jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_REWARDS_KEY)) {
			jsonData.getJSONObject(USER_ACTIVITY_INFO_KEY).put(USER_REWARDS_KEY, 0);
		} 
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected Response getUserTop(Request req, UserAppData appData) {
		JSONObject resultObj = new JSONObject();
		int week = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("week") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getInt("week") : 0;
		//		String key = week == 0 ? SlotsComponent.REDIS_SLOTS_BASE_ID_TOP_KEY : SlotsComponent.REDIS_SLOTS_LAST_WEEK_BASE_ID_TOP_KEY;
		Event event = findObject(new Event());
		if ("668472206".equals(req.getAppId()) || "668475561".equals(req.getAppId()) 
				|| "728211655".equals(req.getAppId())
				 || "728217649".equals(req.getAppId())) {
			if (Integer.parseInt(req.getAppVersion()) <= 10) {
				event.setCurType(0);
				event.setLastType(0);
			}
		}
		String weekNum = (event.getPeriod() - 1) + "";
		try {
			setJsonData(resultObj, event, req, weekNum, week, appData);
			Map params = new HashMap();
			params.put("userId", appData.getUserId());
			if(week == 0) {
				if(((SlotsData)appData).getEligible() == 1) {
					Long ranking = null;
					if (event.getCurType() == 1) {
						if (((SlotsData)appData).getCollectStatus() == 1) { 
							params.put("collectStatus", 1);
							ranking = countByNameQuery("find_collect_ranking_by_slots_user", params, SlotsData.class);
						} else {
							ranking = countByNameQuery("find_ranking_by_slots_user", params, SlotsData.class);
						}
					} else {
						ranking = countByNameQuery("find_ranking_by_slots_user", params, SlotsData.class);
					}
					resultObj.put("ranking", ranking + 1);
				} else {
					resultObj.put("ranking", 0);
				}
			} else {
				SlotsIntegralHistory searchObj = new SlotsIntegralHistory();
				searchObj.setUserId(appData.getUserId());
				searchObj.setWeekNum(weekNum);
				List<SlotsIntegralHistory> objs = findMulti(searchObj);
				if(objs.size() > 0) {
					params.put("weekNum", weekNum);
					Long ranking = null;
					if (event.getLastType() == 1) {
						if(objs.get(0).getEligible() == 1) {
							if (objs.get(0).getCollectStatus() == 1) {
								params.put("collectStatus", 1);
								ranking = countByNameQuery("find_collect_last_week_ranking_by_slots_user", params, SlotsIntegralHistory.class);
							} else {
								ranking = countByNameQuery("find_last_week_ranking_by_slots_user", params, SlotsIntegralHistory.class);
							}
							resultObj.put("ranking", ranking + 1);
						} else {
							resultObj.put("ranking", 0);
						}
					} else {
						ranking = countByNameQuery("find_last_week_ranking_by_slots_user", params, SlotsIntegralHistory.class);
						if(objs.get(0).getEligible() == 1) {
							resultObj.put("ranking", ranking + 1);
						} else {
							resultObj.put("ranking", 0);
						}
					}
					if(req.getJSONObject(DragonComponent.REQ_TOP_ACTION).has("appData")) {
						SlotsIntegralHistory userAppData  = objs.get(0);

						JSONObject appJsonData = userAppData.toJSONObject();
						JSONObject activityData = userAppData.getActivityObject();

						if(activityData.has(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY)) {
							appJsonData.put(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY, activityData.get(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY));
						}
						if(userAppData != null && userAppData.getJsonData() != null && req.getJSONObject(DragonComponent.REQ_TOP_ACTION).getJSONArray("appData").length() > 0) {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(DragonComponent.REQ_TOP_ACTION), appJsonData, "appData");
							resultObj.put("privateData", jsonObj);
						}
					}
				}
			}
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
		return new Response(Response.YES, resultObj);
	}

	@SuppressWarnings("unchecked")
	private void setJsonData(JSONObject resultObj, Event event, Request req, String weekNum, int week, UserAppData appData) {
		// get trophy
		App app = (App)get(req.getAppId(), App.class);

		JSONArray ret = new JSONArray();
		Map getParam = new HashMap();

		Long count = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("count") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getLong("count") : 50l;
		String type = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("type") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getString("type") : USER_INTEGRAL;
		Long userData = req.getJSONObject(CocComponent.REQ_TOP_ACTION).has("userData") ? req.getJSONObject(CocComponent.REQ_TOP_ACTION).getLong("userData") : 0l;
		getParam.put("count", count);
		if (event.getCurType() == 0) {
			type = USER_INTEGRAL;
		} else if (event.getCurType() == 1) {
			getParam.put("collectStatus", 1);
			type = USER_COLLECT_TIME;
		}
		getParam.put("type", type);
		if(week == 0) {
			List<SlotsData> slotsDatas = findByNamedQuery("find_integral_by_slots_user", getParam, SlotsData.class);
			if (slotsDatas.size() < count) {
				getParam.put("collectStatus", 0);
				getParam.put("type", USER_INTEGRAL);
				getParam.put("count", count - slotsDatas.size());
				slotsDatas.addAll(findByNamedQuery("find_integral_by_slots_user", getParam, SlotsData.class));
			}
			if(slotsDatas.size() != 0) {
				for (int i = 0; i < slotsDatas.size(); i++) {
					JSONObject json = new JSONObject();

					if(userData > 0) {
						JSONArray ja = userManager.getUserData(String.valueOf(slotsDatas.get(i).getUserId()));
						JSONObject ud = ja.getJSONObject(0);
						ud.put("userId", ud.getLong("userId"));
						json.put("userData", ud);
					}
					if(req.getJSONObject(DragonComponent.REQ_TOP_ACTION).has("appData")) {
						SlotsData userAppData  = (SlotsData) slotsDatas.get(i);
						JSONObject appJsonData = userAppData.toJSONObject();
						JSONObject activityData = userAppData.getActivityObject();

						if(activityData.has(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY)) {
							appJsonData.put(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY, activityData.get(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY));
						}
						if(userAppData != null && userAppData.getJsonData() != null && req.getJSONObject(DragonComponent.REQ_TOP_ACTION).getJSONArray("appData").length() > 0) {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(DragonComponent.REQ_TOP_ACTION), appJsonData, "appData");
							if (event.getCurType() == 1 && userAppData.getCollectStatus() == 1) {
								jsonObj.put("collectTime", userAppData.getCollectTime().getTime());
							}
							json.put("appData", jsonObj);
						}
					}
					json.put("userId", slotsDatas.get(i).getUserId());
					ret.put(json);
				}
			}
		} else {
			if(event.getLastType() == 1){
				getParam.put("collectStatus", 1);
				type = USER_COLLECT_TIME;
				getParam.put("type", type);
			} else {
				getParam.put("collectStatus", null);
				getParam.put("type", USER_INTEGRAL);
			}
			// get last week data
			getParam.put("weekNum", weekNum);
			List<SlotsIntegralHistory> slotsDatas  = findByNamedQuery("find_last_week_by_slots_user", getParam, SlotsIntegralHistory.class);
			if (slotsDatas.size() < count) {
				getParam.put("collectStatus", 0);
				getParam.put("count", count - slotsDatas.size());
				getParam.put("type", USER_INTEGRAL);
				slotsDatas.addAll(findByNamedQuery("find_last_week_by_slots_user", getParam, SlotsIntegralHistory.class));
			}
			if(slotsDatas.size() != 0) {
				for (int i = 0; i < slotsDatas.size(); i++) {
					JSONObject json = new JSONObject();

					if(userData > 0) {
						JSONArray ja = userManager.getUserData(String.valueOf(slotsDatas.get(i).getUserId()));
						JSONObject ud = ja.getJSONObject(0);
						ud.put("userId", ud.getLong("userId"));
						json.put("userData", ud);
					}
					if(req.getJSONObject(DragonComponent.REQ_TOP_ACTION).has("appData")) {
						SlotsIntegralHistory userAppData  = slotsDatas.get(i);

						JSONObject appJsonData = userAppData.toJSONObject();
						JSONObject activityData = userAppData.getActivityObject();

						if(activityData.has(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY)) {
							appJsonData.put(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY, activityData.get(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY));
						}

						if(userAppData != null && userAppData.getJsonData() != null && req.getJSONObject(DragonComponent.REQ_TOP_ACTION).getJSONArray("appData").length() > 0) {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(DragonComponent.REQ_TOP_ACTION), appJsonData, "appData");
							if (event.getLastType() == 1 && userAppData.getCollectStatus() == 1) {
								jsonObj.put("collectTime", userAppData.getCollectTime().getTime());
							}
							json.put("appData", jsonObj);
						}
					}
					json.put("userId", slotsDatas.get(i).getUserId());
					ret.put(json);
				}
			}
		}

		//update appData's actionTime
		appData.setActionTime(new Date());
		update(appData);
		resultObj.put(UserData.TOUCH_TIME_KEY, (appData.getTouchTime().getTime()/1000) * 1000);
		resultObj.put("data", ret);

	}

	public void resetUserDataFieldFromJsonData(SlotsData userData, SlotsData userAppData,JSONObject jsonData, JSONObject activityData) throws JSONException {
		if (activityData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_INTEGRAL_KEY)) {
			userData.setIntegral(activityData.getJSONObject(USER_ACTIVITY_INFO_KEY)
					.getInt(USER_INTEGRAL_KEY));
		} else {
			userData.setIntegral(0);
		}
		if (activityData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_REWARDS_KEY)) {
			userData.setRewards(activityData.getJSONObject(USER_ACTIVITY_INFO_KEY)
					.getInt(USER_REWARDS_KEY));
		} else {
			userData.setRewards(1);
		}

		boolean flag = true;
		if (activityData.getJSONObject(USER_ACTIVITY_INFO_KEY).has(USER_PUZZLES_KEY)) {
			String puzzle = activityData.getJSONObject(USER_ACTIVITY_INFO_KEY).getString(USER_PUZZLES_KEY);
			String[] puzzles = puzzle.split("\\|");
			for (String p : puzzles) {
				if (Integer.parseInt(p) < 1) {
					flag = false;
					break;
				}
			}
			if(userAppData.getCollectStatus() != null){
				if (flag && userAppData.getCollectStatus() != 1) {
					userData.setCollectStatus(1);
					userData.setCollectTime(new Date());
				} 
			}
		}
		userData.setEligible(1);
	}

	private void separateJSON(JSONObject jo, JSONObject requestWrapperAppDataJson, JSONObject requestWrapperActivityJson) {
		if(jo.has(AppComponent.REQ_UPDATE_ACTION)) {
			JSONObject updAppJson = new JSONObject();
			JSONObject updActivityJson = new JSONObject();
			Iterator<?> it = jo.getJSONObject(AppComponent.REQ_UPDATE_ACTION).keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				if(key.equals(USER_ACTIVITY_INFO_KEY) || key.startsWith(USER_ACTIVITY_INFO_KEY + ".")) {
					updActivityJson.put(key, jo.getJSONObject(AppComponent.REQ_UPDATE_ACTION).get(key));
				} else {
					updAppJson.put(key, jo.getJSONObject(AppComponent.REQ_UPDATE_ACTION).get(key));
				}
			}
			requestWrapperAppDataJson.put(AppComponent.REQ_UPDATE_ACTION, updAppJson);
			requestWrapperActivityJson.put(AppComponent.REQ_UPDATE_ACTION, updActivityJson);
		}

		if(jo.has(AppComponent.REQ_INCR_ACTION)) {
			JSONObject updAppJson = new JSONObject();
			JSONObject updActivityJson = new JSONObject();
			Iterator<?> it = jo.getJSONObject(AppComponent.REQ_INCR_ACTION).keys();
			while (it.hasNext()) {
				String key = (String) it.next();
				if(key.equals(USER_ACTIVITY_INFO_KEY) || key.startsWith(USER_ACTIVITY_INFO_KEY + ".")) {
					updActivityJson.put(key, jo.getJSONObject(AppComponent.REQ_INCR_ACTION).get(key));
				} else {
					updAppJson.put(key, jo.getJSONObject(AppComponent.REQ_INCR_ACTION).get(key));
				}
			}
			requestWrapperAppDataJson.put(AppComponent.REQ_INCR_ACTION, updAppJson);
			requestWrapperActivityJson.put(AppComponent.REQ_INCR_ACTION, updActivityJson);
		}

		if(jo.has(AppComponent.REQ_GET_ACTION)) {
			JSONObject updAppJson = new JSONObject();
			JSONObject updActivityJson = new JSONObject();
			JSONArray delArray = jo.getJSONArray(AppComponent.REQ_GET_ACTION);
			for (int i = 0; i < delArray.length(); i++) {
				String key = (String) delArray.get(i);
				if(key.equals(USER_ACTIVITY_INFO_KEY) || key.startsWith(USER_ACTIVITY_INFO_KEY + ".")) {
					updActivityJson.put(key, jo.getJSONObject(AppComponent.REQ_GET_ACTION).get(key));
				} else {
					updAppJson.put(key, jo.getJSONObject(AppComponent.REQ_GET_ACTION).get(key));
				}
			}
			requestWrapperAppDataJson.put(AppComponent.REQ_GET_ACTION, updAppJson);
			requestWrapperActivityJson.put(AppComponent.REQ_GET_ACTION, updActivityJson);
		}

		if(jo.has(AppComponent.REQ_DEL_ACTION)) {
			JSONObject updAppJson = new JSONObject();
			JSONObject updActivityJson = new JSONObject();
			JSONArray delArray = jo.getJSONArray(AppComponent.REQ_DEL_ACTION);
			for (int i = 0; i < delArray.length(); i++) {
				String key = (String) delArray.get(i);
				if(key.equals(USER_ACTIVITY_INFO_KEY) || key.startsWith(USER_ACTIVITY_INFO_KEY + ".")) {
					updActivityJson.put(key, jo.getJSONObject(AppComponent.REQ_DEL_ACTION).get(key));
				} else {
					updAppJson.put(key, jo.getJSONObject(AppComponent.REQ_DEL_ACTION).get(key));
				}
			}
			requestWrapperAppDataJson.put(AppComponent.REQ_DEL_ACTION, updAppJson);
			requestWrapperActivityJson.put(AppComponent.REQ_DEL_ACTION, updActivityJson);
		}
	}

}
