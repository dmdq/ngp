package com.ngnsoft.ngp.protocol.misc;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserAppData;
import java.util.Date;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class HTBT extends SyncExecutor {
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        UserAppData uad = AppComponentFactory.getUadModel(req.getUserId(), app.getBaseId());
        uad.setActionTime(new Date());
        gm.update(uad);
        JSONObject jo = new JSONObject();
        return new Response(Response.YES, jo);
    }
    
}
