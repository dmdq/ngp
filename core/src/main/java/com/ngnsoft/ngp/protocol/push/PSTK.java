package com.ngnsoft.ngp.protocol.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.PushToken;
import com.ngnsoft.ngp.service.UserManager;


/**
 *
 * @author fcy
 */
@Controller
public class PSTK extends SyncExecutor {
	
	@Autowired
    private UserManager um;

    @Override
    public Response exec(Request req, App app) throws Exception {
        if (!req.hasKey("token")) {
            return new Response(Response.NO, "NO_token");
        }
        PushToken pt = new PushToken(null, req.getAppId(), um.getDvid(req), req.getStr("token"), req.getStr("OS"));
        pt.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName());
        gm.saveIgnoreDke(pt);
        return new Response(Response.YES);
    }
    
}
