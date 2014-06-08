package com.ngnsoft.ngp.component.dragon;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.dragon.model.DragonUserActivity;
import com.ngnsoft.ngp.component.dragon.model.DragonUserActivityHistory;
import com.ngnsoft.ngp.component.dragon.service.DragonManager;
import com.ngnsoft.ngp.model.*;
import com.ngnsoft.ngp.service.AppMessageManager;
import com.ngnsoft.ngp.service.ClanManager;
import com.ngnsoft.ngp.service.UserAppDataManager;
import com.ngnsoft.ngp.service.UserManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author fcy
 */
public class DragonComponent extends AppComponent {
    
    private static final Logger logger = LoggerFactory.getLogger(DragonComponent.class);
    
    public static final String APP_BASE_ID = "100001";
    
    private static final String DB_NAME = "dragon";
    
    public static final String SEARCH_COMRADE_TYPE = "srh";
    
    public static final String REQ_SEND_ACTION = "send";
    
    public static final String REQ_GET_ACTION = "get";
    
    public static final String REQ_READ_ACTION = "read";
    
    public static final String REQ_DEL_ACTION = "del";
    
    public static final String REQ_RESULT_ACTION = "rst";
    
    public static final String REQ_TOP_ACTION = "top";
    
    public static final String REDIS_DRAGON_FRD_ID_PREFIX = "dragon_frd_";
    
    public static final String REDIS_DRAGON_HELP_ID_PREFIX = "dragon_help_";
    
    public static final long ROBOT_IDENTIFY_VAL = 1000000l;
    
    @Autowired
    private UserManager userManager;
    
    @Autowired
    @Qualifier("dragonDataManager")
    private UserAppDataManager uadm;
    
    @Autowired
    @Qualifier("dragonManager")
    private ClanManager clanManager;
    
    @Autowired
    @Qualifier("dragonManager")
    private DragonManager dragonManager;
    
    @Autowired
    @Qualifier("dragonManager")
    private AppMessageManager appMessageManager;
    
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
        return new DragonData(userId);
    }

    @Override
    public Response aprq(App app, Request req) {
    	JSONObject jo = req.getBizData();
    	Response resp = null;
    	try {
	    	if(jo.has(SEARCH_COMRADE_TYPE)) {
	    		resp = userManager.searchFacilitator(req);
	    	} else {
	    		resp = uadm.executeUserAppDataByAction(req, app);
	    	}
    	} catch (JSONException e) {
			resp = new Response(Response.NO, "EXCEPTION");
		}
    	
    	return resp ;
    }
    
    @Override
    public Response uscl(App app, Request req) {
    	JSONObject jo = req.getBizData();
    	Response resp = null;
    	try {
	    	if(jo.has(AppComponent.GET_OR_LIST_CLAN_ACTION)) {
	    		resp = clanManager.getClanByCondition(req, app);
	    	} else if(jo.has(AppComponent.ADD_CLAN_ACTION)) {
	    		try {
					resp = clanManager.createClan(req, app);
				} catch (Exception e) {
					resp = new Response(Response.NO, "EXCEPTION");
				}
	    	} else if(jo.has(AppComponent.MODIFY_CLAN_ACTION)) {
	    		try {
					resp = clanManager.modifyClan(req, app);
				} catch (Exception e) {
					resp = new Response(Response.NO, "EXCEPTION");
				}
	    	} else if(jo.has(AppComponent.JOIN_CLAN_ACTION)) {
	    		resp = clanManager.joinClan(req, app);
	    	} else if(jo.has(AppComponent.LIST_CLAN_ACTION)) {
	    		Object obj = getPage(AppComponent.LIST_CLAN_ACTION, req);
	            Pagination page = null;
	            if (obj instanceof Pagination) {
	                page = (Pagination) obj;
	            } else {
	                return (Response) obj;
	            }
	    		resp = clanManager.getClans(req, app, page);
	    	} else if(jo.has(AppComponent.EXIT_CLAN_ACTION)) {
	    		resp = clanManager.exitClan(req, app);
	    	} else if(jo.has(AppComponent.CONFER_CLAN_ACTION)) {
	    		resp = clanManager.conferClanUser(req, app);
	    	} else {
	    		//special protocol
	    		resp = new Response(Response.YES);
	    	}
    	} catch (JSONException e) {
			resp = new Response(Response.NO, "EXCEPTION");
		}
    	
    	return resp ;
    }
    
    @Override
    public Response apms(App app, Request req) {
    	JSONObject jo = req.getBizData();
    	Response resp = null;
    	try {
    		if(jo.has(AppComponent.REQ_GET_ACTION)) {
    			resp = appMessageManager.getMessageByCondition(req, app);
    		} else if(jo.has(AppComponent.REQ_READ_ACTION)) {
    			resp = appMessageManager.rdms(req);
    		} else if(jo.has(AppComponent.REQ_SEND_ACTION)) {
    			resp = appMessageManager.mtuf(req);
    		} else if(jo.has(AppComponent.REQ_DEL_ACTION)) {
    			resp = appMessageManager.dlms(req);
    		} else {
    			resp = new Response(Response.YES);
    		}
		} catch (JSONException e) {
			resp = new Response(Response.NO, "EXCEPTION");
		}
    	return resp ;
    }

    /*@Override
    public void onLogin(Request req, App app, Long userId, String deviceId) {
        String appId = app.getId();
        DragonUserActivity ua = new DragonUserActivity(null, UserActivity.ACTION_LOGIN, userId, appId, req.getAppVersion(),
                deviceId, Engine.getInstance().getId(), req.getIP());
        String updateSql = "update_ua_by_cpk";
        int num = dragonManager.update(updateSql, ua, DragonUserActivity.class);
        if (num == 0) {
            dragonManager.save(ua);
        }
        DragonUserActivityHistory uah = new DragonUserActivityHistory();
        BeanUtils.copyProperties(ua, uah);
        try {
            asyncOperManager.saveData(uah);
        } catch (Exception e) {
            logger.error("Operate DragonUserActivityHistory asynchronous error:", e);
        }
    }*/
    
    private Object getPage(String requestType, Request req) throws JSONException {
        Pagination page = new Pagination();
        if (req.getJSONObject(requestType).has("curPage")) {
            page.setCurrent_page(req.getJSONObject(requestType).getInt("curPage"));
        } else {
            return new Response(Response.NO, "NO_curPage");
        }
        if (req.getJSONObject(requestType).has("perSize")) {
            page.setItems_per_page(req.getJSONObject(requestType).getInt("perSize"));
        } else {
            return new Response(Response.NO, "NO_perSize");
        }
        return page;
    }
    
}
