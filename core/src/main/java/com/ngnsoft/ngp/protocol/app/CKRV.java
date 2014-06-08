package com.ngnsoft.ngp.protocol.app;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.ResUpdateManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class CKRV extends SyncExecutor {

    @Autowired
    private ResUpdateManager resUpdateManager;

    @Override
    public Response exec(Request req, App app) throws Exception {
        String appId = req.getAppId();
        if (!req.hasKey("resver")) {
            return new Response(Response.NO, "NO_resver");
        }
        Integer resVersion = req.getLong("resver").intValue();
        return resUpdateManager.checkResVersion(appId, resVersion.intValue());
    }
}
