package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javapns.Push;
import javapns.notification.PushNotificationPayload;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.enums.DataType;
import com.ngnsoft.ngp.misc.PushNotify;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.model.Promo;
import com.ngnsoft.ngp.model.UserActivity;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.PlacardManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

@Service
public class DataManagerImpl extends GenericManagerImpl implements
        DataManager {
	
	private static Logger LOGGER = Logger.getLogger(DataManagerImpl.class);

    @Autowired
    private PlacardManager placardManager;
    
    @Autowired
    private RedisImpl redisImpl;
    
    public boolean push(Long userId, App app, PushNotify pn) throws Exception {
        String queryName = "findLatestLoginUserActivityByUserApp";
        
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("userId", userId);
        paramMap.put("action", UserActivity.ACTION_LOGIN);
        paramMap.put("appId", app.getId());
        
        List<String> appIds = placardManager.findByNamedQuery("getAppByAppIds", app.getId(), App.class, String.class);
        if(appIds.size() == 0) {
        	return false;
        } else {
        	paramMap.put("rangeList", appIds);
        }
        UserActivity searchObj = new UserActivity();
        searchObj.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName());
        List<Map>  l = placardManager.findByNamedQuery(queryName, paramMap, searchObj, Map.class);
        String token = null;
        String os = null;
        if (l == null || l.isEmpty()) {
           return false;
        } else {
        	token = l.get(0).get("token").toString();
        	os = l.get(0).get("os").toString();
        }
        if (token == null) return false;
        if (!"iOS".equalsIgnoreCase(os)) {
            return false;
        }
        String certff = ".p12";
        String passwd = "115131";
        String cert = Engine.getInstance().getConfig().getCertPath() + app.getId() + Engine.getInstance().getConfig().getCertSuffix() + certff;

        PushNotificationPayload payload = PushNotificationPayload.complex();
        if (pn.getMessage() != null) {
            payload.addAlert(pn.getMessage());
        }
        if (pn.getBadge() > 0) {
            payload.addBadge(pn.getBadge());
        }
        if (pn.getSound() != null) {
            payload.addSound(pn.getSound());
        }
        if (pn.getPushKey() != null) {
            payload.addCustomDictionary(pn.getPushKey(), pn.getPushLink());
        }
        Push.payload(payload, cert, passwd, Engine.getInstance().getConfig().isProdStage(), token);
        return true;
    }

    @Override
    public Object getDataByType(DataType type, Map<String, Object> queryMap) {
        Object resultObj = null;
        switch (type) {
            case PLACARD:
                resultObj = getPlacards(queryMap);
                break;
            case PROMOTION:
                resultObj = getPromotions(queryMap);
                break;
        }

        return resultObj;
    }

    private Object getPlacards(Map<String, Object> queryMap) {
        Map<String, Object> baseIdCacheMap = Engine.getInstance().getBaseIdCacheMap();
        Map<String, Object> appIdCacheMap = Engine.getInstance().getAppIdCacheMap();

        Set<Long> placardIds = new HashSet<Long>();

        //get placard by base_id and app_version
        if (baseIdCacheMap.get(queryMap.get("baseId")) != null) {
            //get data from cache
            List<Long> baseIdCacheList = (List<Long>) baseIdCacheMap.get(queryMap.get("baseId"));
            placardIds.addAll(baseIdCacheList);
        } else {
            //cache didn't loaded the data, and get the data from DB
        	queryMap.put("sysDate", new Date());
            List<Long> baseIdCacheList = placardManager.findSingleColumnByNameQuery("find_placard_by_base_id", queryMap, Placard.class, Long.class);
            if (baseIdCacheList != null) {
                placardIds.addAll(baseIdCacheList);
                baseIdCacheMap.put(queryMap.get("baseId").toString(), baseIdCacheList);
            } else {
                //insert null List to cache
                baseIdCacheMap.put(queryMap.get("baseId").toString(), new ArrayList<Long>());
            }
        }

        //get placard by app_id and app_version
        if (appIdCacheMap.get(queryMap.get("appId")) != null) {
            //get data from cache
            List<Long> appIdCacheList = (List<Long>) appIdCacheMap.get(queryMap.get("appId"));
            placardIds.addAll(appIdCacheList);
        } else {
            //cache didn't loaded the data, and get the data from DB
        	queryMap.put("sysDate", new Date());
            List<Long> appIdCacheList = placardManager.findSingleColumnByNameQuery("find_placard_by_app_id", queryMap, Placard.class, Long.class);
            if (appIdCacheList != null) {
                placardIds.addAll(appIdCacheList);
                appIdCacheMap.put(queryMap.get("appId").toString(), appIdCacheList);
            } else {
                //insert null List to cache
            	appIdCacheMap.put(queryMap.get("appId").toString(), new ArrayList<Long>());
            }
        }

        List<JSONObject> jsonObjs = new ArrayList<JSONObject>();
        boolean flag = true;
        
        Date curDate = new Date();

        for (Long placardId : placardIds) {
        	Placard placard = null;
        	try {
        		placard = redisImpl.get(Engine.PLACARD_REDIS_PREFIX + placardId) == null ? null : (Placard) redisImpl.get(Engine.PLACARD_REDIS_PREFIX + placardId);
			} catch (Exception e) {
				LOGGER.error("Redis operate ERROR:", e);
			}
            if (placard == null) {
                //get data from DB
                placard = placardManager.get(placardId, Placard.class);
                if (placard != null) {
                	try {
                		redisImpl.set(Engine.PLACARD_REDIS_PREFIX + placardId, placard);
        			} catch (Exception e) {
        				LOGGER.error("Redis operate ERROR:", e);
        			}
                    jsonObjs.add(placard.toJSONObject());
                }
            } else {
            	jsonObjs.add(placard.toJSONObject());
            }
            if(placard != null && flag) {
            	baseIdCacheMap.clear();
                appIdCacheMap.clear();
            	flag = isValidPlacard(placard, curDate);
            } 
            if(!flag) break;
        }
        if(!flag) jsonObjs = (List<JSONObject>)getPlacards(queryMap);
        
        return jsonObjs;
    }
    
    private boolean isValidPlacard(Placard placard, Date curDate) {
    	if(placard.getStartTime().getTime() <= curDate.getTime() && placard.getEndTime().getTime() > curDate.getTime()) {
    		return true;
    	}
    	return false;
    }

    private Object getPromotions(Map<String, Object> queryMap) {
        List<Promo> promotions = findMulti(new Promo());
        List<JSONObject> jsonObjs = new ArrayList<JSONObject>();
        for (Promo promo : promotions) {
            if (promo.getIcon() != null && !promo.getIcon().startsWith("http")) {
                promo.setIcon(Engine.getInstance().getConfig().getFileServer() + promo.getIcon());
            }
            jsonObjs.add(promo.toJSONObject());
        }
        return jsonObjs;
    }

    private String assemblyKey(Map<String, Object> queryMap) {
        StringBuilder sb = new StringBuilder();
        for (String key : queryMap.keySet()) {
            sb.append(key);
            sb.append("_");
        }
        if (sb.length() > 0) {
            sb.delete(0, sb.length() - 1);
        }
        return sb.toString();
    }

    @Override
    public JSONObject getDataFromJsonData(JSONObject fromJsonObj, JSONObject appDataJson, String cmdType) throws JSONException {
        JSONArray getArray = fromJsonObj.has(cmdType) && !fromJsonObj.isNull(cmdType) ? fromJsonObj.getJSONArray(cmdType) : null;
        return fetchUserData(appDataJson, getArray);
    }
    
    @Override
    public JSONObject fetchUserData(JSONObject appDataJson, JSONArray appDataKeys) {
        JSONObject resultObj = new JSONObject();
        if (appDataKeys != null) {
            if (appDataKeys.length() == 1 && AppComponent.GET_ALL_DATA_KEY.equals(appDataKeys.get(0).toString())) {
                //get all data
                resultObj = appDataJson;
            } else if (appDataKeys.length() == 1 && AppComponent.GET_NONE_DATA_KEY.equals(appDataKeys.get(0).toString())) {
            	
            } else {
                for (int i = 0; i < appDataKeys.length(); i++) {
                    String key = (String) appDataKeys.get(i);
                    String[] subKeys = key.split(JSON_KEY_REGEX);
                    getDataFromJson(subKeys, resultObj, appDataJson, 0, key);
                }
            }
        } else {
            resultObj = null;
        }
        return resultObj;
    }

    @Override
    public void updateJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException {
        JSONObject updateObj = fromJsonObj.has(AppComponent.REQ_UPDATE_ACTION) && !fromJsonObj.isNull(AppComponent.REQ_UPDATE_ACTION) ? fromJsonObj.getJSONObject(AppComponent.REQ_UPDATE_ACTION) : null;
        if (updateObj != null) {
            Iterator<?> it = updateObj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String[] subKeys = key.split(JSON_KEY_REGEX);
                updateJson(subKeys, updateObj, appDataJson, 0, key);
            }
        }
    }
    
    @Override
    public void incrJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException {
        JSONObject updateObj = fromJsonObj.has(AppComponent.REQ_INCR_ACTION) && !fromJsonObj.isNull(AppComponent.REQ_INCR_ACTION) ? fromJsonObj.getJSONObject(AppComponent.REQ_INCR_ACTION) : null;
        if (updateObj != null) {
            Iterator<?> it = updateObj.keys();
            while (it.hasNext()) {
                String key = (String) it.next();
                String[] subKeys = key.split(JSON_KEY_REGEX);
                incrJson(subKeys, updateObj, appDataJson, 0, key);
            }
            fromJsonObj.put(AppComponent.REQ_INCR_ACTION, updateObj);
        }
    }

    @Override
    public void delJsonData(JSONObject fromJsonObj, JSONObject appDataJson) throws JSONException {
        JSONArray delArray = fromJsonObj.has(AppComponent.REQ_DEL_ACTION) && !fromJsonObj.isNull(AppComponent.REQ_DEL_ACTION) ? fromJsonObj.getJSONArray(AppComponent.REQ_DEL_ACTION) : null;
        if (delArray != null) {
            for (int i = 0; i < delArray.length(); i++) {
                String key = (String) delArray.get(i);
                String[] subKeys = key.split(JSON_KEY_REGEX);
                delDataFromJson(subKeys, null, appDataJson, 0, key);
            }
        }
    }

    protected void getDataFromJson(String[] subKeys, JSONObject jsonObj, JSONObject jsonAllObj, int index, String key) throws JSONException {
        if (index < subKeys.length - 1) {
            if (!jsonAllObj.has(subKeys[index])) {
                return;
            }
            index++;
            getDataFromJson(subKeys, jsonObj, (JSONObject) jsonAllObj.get(subKeys[index - 1]), index, key);
        } else {
            if (jsonAllObj.has(subKeys[index])) {
                Object subObj = jsonAllObj.get(subKeys[index]);
                jsonObj.put(key, subObj);
            }
        }
    }
    
    protected void incrJson(String[] subKeys, JSONObject jsonObj, JSONObject jsonAllObj, int index, String key) throws JSONException {
        if (index < subKeys.length - 1) {
            if (!jsonAllObj.has(subKeys[index])) {
                jsonAllObj.put(subKeys[index], new JSONObject());
            }
            index++;
            incrJson(subKeys, jsonObj, (JSONObject) jsonAllObj.get(subKeys[index - 1]), index, key);
        } else {
            Object subObj = jsonObj.get(key);
            if(subObj instanceof Integer || subObj instanceof Long) {
            	Long incrVal = Long.valueOf(subObj.toString());
            	if(jsonAllObj.has(subKeys[index])) {
            		Object oldObj = jsonAllObj.get(subKeys[index]);
            		try {
						Long newObj = Long.parseLong(oldObj.toString());
            			Long oldVal = newObj;
            			jsonAllObj.put(subKeys[index], getPositiveInteger(oldVal + incrVal));
            			jsonObj.put(key, getPositiveInteger(oldVal + incrVal));
					} catch (Exception e) {
						
					}
            	} else {
            		jsonAllObj.put(subKeys[index], getPositiveInteger(incrVal));
            		jsonObj.put(key, getPositiveInteger(incrVal));
            	}
            }
        }
    }

    protected void updateJson(String[] subKeys, JSONObject jsonObj, JSONObject jsonAllObj, int index, String key) throws JSONException {
        if (index < subKeys.length - 1) {
            if (!jsonAllObj.has(subKeys[index])) {
                jsonAllObj.put(subKeys[index], new JSONObject());
            }
            index++;
            updateJson(subKeys, jsonObj, (JSONObject) jsonAllObj.get(subKeys[index - 1]), index, key);
        } else {
            Object subObj = jsonObj.get(key);
            jsonAllObj.put(subKeys[index], subObj);
        }
    }

    protected void delDataFromJson(String[] subKeys, JSONObject jsonObj, JSONObject jsonAllObj, int index, String key) throws JSONException {
        if (index < subKeys.length - 1) {
            if (!jsonAllObj.has(subKeys[index])) {
                return;
            }
            index++;
            delDataFromJson(subKeys, jsonObj, (JSONObject) jsonAllObj.get(subKeys[index - 1]), index, key);
        } else {
            jsonAllObj.remove(subKeys[index]);
        }
    }
    
    private Long getPositiveInteger(Long val) {
    	return val > 0 ? val : 0l;
    }
    
    public static void main(String[] args) {
		Integer s= -3;
		System.out.println((int)s);
	}
    
}
