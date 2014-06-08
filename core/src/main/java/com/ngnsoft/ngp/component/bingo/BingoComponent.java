package com.ngnsoft.ngp.component.bingo;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.bingo.service.BingoManager;
import com.ngnsoft.ngp.component.model.AppData;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.UserAppDataManager;

/**
 *
 * @author fcy
 */
public class BingoComponent extends AppComponent {
    
    public static final String APP_BASE_ID = "100005";
    
    public static final String CMD_COUNT = "count";
    public static final String CMD_JOIN = "join";
    public static final String CMD_READY = "ready";
    public static final String CMD_BINGO = "bingo";
    public static final String CMD_QUIT = "quit";
    public static final String CMD_HTBT = "htbt";
    public static final String CMD_CHAT = "chat";
    public static final String CMD_OVER = "over";
    
    private static final String PARAM_GAME_ID = "gid";
    
    @Autowired
    private BingoManager bingoManager;
    
    @Autowired
    @Qualifier("userAppDataManager")
    private UserAppDataManager uadm;

    @Override
    public Response aprq(App app, Request req) throws Exception {
    	
    	
    	JSONObject jo = req.getBizData();
    	if(jo.has(CMD_COUNT)) {
    		return bingoManager.count(app, req);
    	} if(jo.has(REQ_GET_ACTION) || jo.has(REQ_UPDATE_ACTION) || jo.has(REQ_DEL_ACTION)) {
    		return uadm.executeUserAppDataByAction(req, app);
    	}
    	
        //请求参数，gid，不是名称（应对多语言版本）
        String gid = req.getStr(PARAM_GAME_ID);
        if (gid == null || gid.isEmpty()) {
            return new Response(Response.NO, "ERR_" + PARAM_GAME_ID);
        }
        bingoManager.checkGame(app, gid);
        if (jo.has(CMD_JOIN)) {
            return bingoManager.join(app, gid, req);
        } else if (jo.has(CMD_READY)) {
            return bingoManager.ready(app, gid, req);
        } else if (jo.has(CMD_BINGO)) {
            return bingoManager.bingo(app, gid, req);
        } else if (jo.has(CMD_QUIT)) {
            return bingoManager.quit(app, gid, req);
        } else if (jo.has(CMD_HTBT)) {
            return bingoManager.htbt(app, gid, req);
        } else if (jo.has(CMD_CHAT)) {
            return bingoManager.chat(app, gid, req);
        } else if (jo.has(CMD_OVER)) {
            return bingoManager.over(gid, req.getUserId());
        } else {
        	return new Response(Response.NO, "ERR_CMD");
        }
    }
    
    @Override
    public UserAppData getUadModel(Long userId) {
    	AppData appData = new AppData(userId);
    	appData.setDbName(getDbName());
        return new AppData(userId);
    }
    
    public String getDbName() {
        return "bingo";
    }
    
    public String getDbName(BaseModel baseModel) {
        
        return "bingo";
    }
    
}
