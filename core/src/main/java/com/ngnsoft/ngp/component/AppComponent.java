package com.ngnsoft.ngp.component;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.service.ComponentManager;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.IosOrder;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserActivity;
import com.ngnsoft.ngp.model.UserActivityHistory;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.AsyncOperManager;
import com.ngnsoft.ngp.service.UserAppDataManager;

/**
 *
 * @author fcy
 */
public class AppComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(AppComponent.class);

    public static final Random r = new Random();
    public static final String REQ_GET_ACTION = "get";
    public static final String REQ_ADD_ACTION = "add";
    public static final String REQ_DEL_ACTION = "del";
    public static final String REQ_UPDATE_ACTION = "upd";
    public static final String GET_ALL_DATA_KEY = "all";
    public static final String GET_NONE_DATA_KEY = "none";
    public static final String ADD_CLAN_ACTION = "add";
    public static final String MODIFY_CLAN_ACTION = "upd";
    public static final String REQ_INCR_ACTION = "incr";
    public static final String LIST_CLAN_ACTION = "list";
    public static final String GET_OR_LIST_CLAN_ACTION = "get";
    public static final String JOIN_CLAN_ACTION = "join";
    public static final String EXIT_CLAN_ACTION = "exit";
    public static final String CONFER_CLAN_ACTION = "confer";
    public static final String DEL_CLAN_ACTION = "del";
    public static final String REQ_SEND_ACTION = "send";
    public static final String REQ_READ_ACTION = "read";
    public static final String REQ_TYPE_ACTION = "type";
    public static final String REQ_TOP_ACTION = "top";
    public static final String REQ_DATA_ACTION = "data";
    
    private String baid;
    
    @Autowired
    @Qualifier("userAppDataManager")
    private UserAppDataManager uadm;
    
    @Autowired
    @Qualifier("componentManager")
    private ComponentManager cm;
    
    @Autowired
    protected AsyncOperManager asyncOperManager;
    
    public String getDbName() {
        return BaseModel.NGP_DB_NAME;
    }
    
    public String getDbName(BaseModel baseModel) {
        
        return BaseModel.NGP_DB_NAME;
    }

    public Response lsen(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req) {
        return cm.lsenFav(app, zoneEnginesMap, req);
    }

    public Response aprq(App app, Request req) throws Exception {
        Response resp = uadm.executeUserAppDataByAction(req, app);
        return resp;
    }

    public Response uscl(App app, Request req) {
        return null;
    }

    public Response apms(App app, Request req) {
        return null;
    }

    public String getBaid() {
        return baid;
    }

    public void setBaid(String baid) {
        this.baid = baid;
    }

    public UserAppData getUadModel(Long userId) {
        return new UserAppData(null, userId, baid, null);
    }

    public Response apsl(App app, Request req, JSONObject receipt) {
    	String orderId = receipt.getString("transaction_id");
        IosOrder so = new IosOrder(orderId);
        so.setDbName(getDbName());
        IosOrder order = cm.findObject(so);
        if (order != null) {
            return new Response(Response.NO, "DUPL");
        }
        order = new IosOrder(null, receipt, app.getId(), req.getUserId());
        order.setDbName(getDbName());
        cm.save(order);
        return new Response(Response.YES, receipt);
    }

    public Response login(Long userId, String deviceId, boolean withAppData, Request req) {
        Response resp = new Response(Response.YES);
        UserAppData uad = null;
        if (withAppData) {
        	UserAppData appData = getUadModel(userId);
        	appData.setDbName(getDbName());
            uad = cm.findObject(appData);
        } else {
            String findSql = "find_uad_status";
            UserAppData uadModel = getUadModel(userId);
            uadModel.setDbName(getDbName());
            List<UserAppData> uads = cm.findByNamedQuery(findSql, uadModel, uadModel, UserAppData.class);
            if (uads != null && !uads.isEmpty()) {
                uad = uads.get(0);
            }
        }
        if (uad != null) {
            if (User.STATUS_FORBID == uad.getStatus()) {
                return new Response(Response.NO, "APP_FORBID", new JSONObject(uad.getStatusDetail()));
            }
            UserAppData upUserAppData = getUadModel(uad.getUserId());
            upUserAppData.setDbName(getDbName());
            upUserAppData.setActionTime(new Date());
            upUserAppData.setUlld(deviceId);
            cm.update(upUserAppData);
        }
        resp.setAttachObject(uad);
        return resp;
    }

    public void onLogin(Request req, App app, Long userId, String deviceId) {
        String appId = app.getId();
        UserActivity ua = new UserActivity(null, UserActivity.ACTION_LOGIN, userId, appId, req.getAppVersion(),
                deviceId, Engine.getInstance().getId(), req.getIP());
        String updateSql = "update_ua_by_cpk";
        ua.setDbName(getDbName());
        int num = cm.update(updateSql, ua, ua);
        if (num == 0) {
            cm.save(ua);
        }
        UserActivityHistory uah = new UserActivityHistory();
        BeanUtils.copyProperties(ua, uah);
        try {
            uah.setDbName(getDbName());
            asyncOperManager.saveData(uah);
        } catch (Exception e) {
            logger.error("Operate UserActivityHistory asynchronous error:", e);
        }
    }
}
