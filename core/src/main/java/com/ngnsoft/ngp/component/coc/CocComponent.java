package com.ngnsoft.ngp.component.coc;

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
import com.ngnsoft.ngp.component.coc.model.CocIosOrder;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.coc.service.CocManager;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.UserDataManager;

/**
 *
 * @author fcy
 */
public class CocComponent extends AppComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(CocComponent.class);

    public static final String APP_BASE_ID = "100002";
    
    private static final String DB_NAME = "coc";
    
    public static final String REQ_SEARCH_ACTION = "srh";
    public static final String REQ_RESULT_ACTION = "rst";
    public static final String REQ_LOG_ACTION = "log";
    public static final String REQ_REPLAY_ACTION = "replay";
    public static final String REQ_REVENGE_ACTION = "revenge";
    public static final String SEARCH_DEDUCT_KEY = "deduct";
    public static final String REQ_RESULT_TM_KEY = "tm";
    public static final String BE_TEMP_SRH_TARGET_ID_PREFIX = "be_temp_srh_target_";
    public static final String BE_MARK_SRH_TARGET_ID_PREFIX = "be_mark_srh_target_";
    public static final String REDIS_COC_BASE_ID_TOP_KEY = "coc_base_id_top";
    
    @Autowired
    @Qualifier("userDataManager")
    private UserDataManager udm;
    
    @Autowired
    private CocManager cocManager;
    
    @Override
    public String getDbName() {
        return DB_NAME;
    }
    
    @Override
    public String getDbName(BaseModel baseModel) {
        return DB_NAME;
    }

    @Override
    public Response aprq(App app, Request req) {
        Response resp = null;
        try {
            resp = udm.executeUserAppDataByAction(req, app);
        } catch (JSONException e) {
            resp = new Response(Response.NO, "EXCEPTION");
        }
        return resp;
    }
    
    @Override
    public Response apsl(App app, Request req, JSONObject receipt) {
        String orderId = receipt.getString("transaction_id");
        CocIosOrder order = cocManager.findObject(new CocIosOrder(orderId));
        if (order != null) {
            return new Response(Response.NO, "DUPL");
        }
        order = new CocIosOrder(null, receipt, app.getId(), req.getUserId());
        if(cocManager.apsl(order)) {
        	return new Response(Response.YES, receipt);
        } else {
        	return new Response(Response.NO, "ERR");
        }
    }

    @Override
    public Response uscl(App app, Request req) {
        return new Response(Response.YES);
    }

    @Override
    public Response apms(App app, Request req) {
        JSONObject jo = req.getBizData();
        Response resp = null;
        try {
            if (jo.has(AppComponent.REQ_GET_ACTION)) {
                resp = cocManager.getMessageByCondition(req, app);
            } else if (jo.has(AppComponent.REQ_READ_ACTION)) {
                resp = cocManager.rdms(req);
            } else if (jo.has(AppComponent.REQ_SEND_ACTION)) {
                if (!jo.getJSONObject(AppComponent.REQ_SEND_ACTION).has("to")) {
                    return new Response(Response.NO, "NO_to");
                }
                resp = cocManager.mtuf(req);
            } else if (jo.has(AppComponent.REQ_DEL_ACTION)) {
                resp = cocManager.dlms(req);
            } else {
                resp = new Response(Response.YES);
            }
        } catch (JSONException e) {
            resp = new Response(Response.NO, "EXCEPTION");
        }
        return resp;
    }

    @Override
    public UserAppData getUadModel(Long userId) {
        return new UserData(userId);
    }

    @Override
    public Response login(Long userId, String deviceId, boolean withAppData, Request req) {
        Response resp = new Response(Response.YES);
        UserData ud = null;
        if (withAppData) {
            ud = cocManager.findObject(new UserData(userId));
        } else {
            String findSql = "find_coc_uad_status";
            UserData uadModel = new UserData(userId);
            List<UserData> uds = cocManager.findByNamedQuery(findSql, uadModel, UserData.class);
            if (uds != null && !uds.isEmpty()) {
                ud = uds.get(0);
            }
        }
        if (ud != null) {
            if (User.STATUS_FORBID == ud.getStatus()) {
                return new Response(Response.NO, "APP_FORBID", new JSONObject(ud.getStatusDetail()));
            }
            UserData upUserData = new UserData(ud.getUserId());
            upUserData.setActionTime(new Date());
            String ulld = req.getMacId();
            if (ulld == null || ulld.isEmpty()) {
                ulld = req.getIdfa();
            }
            upUserData.setUlld(ulld);
            if (udm.lockTimeout((UserData) ud)) {
                upUserData.setSearchLock(UserData.SEARCH_LOCK_NO);
                ud.setSearchLock(UserData.SEARCH_LOCK_NO);
            }
            udm.update(upUserData);
        }
        resp.setAttachObject(ud);
        return resp;
    }
    
}
