package com.ngnsoft.ngp.component.slots;

import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.service.ComponentManager;
import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.component.slots.model.SlotsData;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.UserAppDataManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.service.impl.SlotsDataManagerImpl;

public class SlotsComponent extends AppComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(SlotsComponent.class);
    
    public static final String APP_BASE_ID = "100004";
    
    private static final String DB_NAME = "slots";
    
    public static final String REQ_BOARD_ACTION = "top";
    
    public static final String REQ_BOARD_DATA_ACTION = "data";
    
    public static final String SLOTS_REDIS_TOP_KEY = "slots_integral_top";
    
    public static final String REDIS_SLOTS_BASE_ID_TOP_KEY = "slots_base_id_top";
    
    public static final String REDIS_SLOTS_LAST_WEEK_BASE_ID_TOP_KEY = "slots_last_week_base_id_top";
    
    @Autowired
    @Qualifier("componentManager")
    private ComponentManager cs;
    
    @Autowired
    private UserManager userManager;
    
    @Autowired
    @Qualifier("slotsDataManager")
    private UserAppDataManager uadm;
    
    @Override
    public String getDbName() {
        return DB_NAME;
    }
    
    @Override
    public String getDbName(BaseModel baseModel) {
        return DB_NAME;
    }
    
    @Override
    public UserAppData getUadModel(Long userId) {
        return new SlotsData(userId);
    }

    @Override
    public Response aprq(App app, Request req) {
    	Response resp = null;
    	try {
    		resp = uadm.executeUserAppDataByAction(req, app);
    	} catch (JSONException e) {
			resp = new Response(Response.NO, "EXCEPTION");
		}
    	
    	return resp ;
    }
    
    @Override
    public Response login(Long userId, String deviceId, boolean withAppData,
    		Request req) {
    	Response resp = new Response(Response.YES);
    	Event event = userManager.findObject(new Event());
    	JSONObject eventObj = new JSONObject();
    	if(event != null) {
    		eventObj.put("type", event.getCurType());
    		eventObj.put("startTime", event.getRefreshStartTime());
    		eventObj.put("unit", event.getCurUnit());
    	}
    	resp.put("eventData", eventObj);
        UserAppData uad = null;
        if (withAppData) {
            uad = cs.findObject(getUadModel(userId));
            if(uad != null ) {
            	mergeSlotsData(uad);
            }
        } else {
            String findSql = "find_uad_status";
            UserAppData uadModel = getUadModel(userId);
            List<UserAppData> uads = cs.findByNamedQuery(findSql, uadModel, uadModel.getClass(), UserAppData.class);
            if (uads != null && !uads.isEmpty()) {
                uad = uads.get(0);
            }
        }
        if (uad != null) {
            if (User.STATUS_FORBID == uad.getStatus()) {
                return new Response(Response.NO, "APP_FORBID", new JSONObject(uad.getStatusDetail()));
            }
            UserAppData upUserAppData = getUadModel(uad.getUserId());
            upUserAppData.setActionTime(new Date());
            upUserAppData.setUlld(deviceId);
            cs.update(upUserAppData);
        }
        resp.setAttachObject(uad);
        return resp;
    }
    
    private static void mergeSlotsData(UserAppData uad) {
    	SlotsData slots = (SlotsData)uad;
    	JSONObject jsonData = slots.getJsonData() == null ? new JSONObject() : new JSONObject(slots.getJsonData());
    	JSONObject activityData = slots.getActivityObject();
    	
    	if(activityData.has(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY)) {
    		jsonData.put(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY, activityData.get(SlotsDataManagerImpl.USER_ACTIVITY_INFO_KEY));
		}
    	uad.setJsonData(jsonData.toString());
    }
    
}
