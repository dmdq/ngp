package com.ngnsoft.ngp.protocol.user;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.CoinManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class UPCN extends SyncExecutor {
    
    @Autowired
    CoinManager cm;
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        String incrmt = req.getStr("incrmt");
        if (incrmt.isEmpty()) {
            return new Response(Response.NO, "NO_incrmt");
        }
        String incrmtKey = req.getStr("incrmt_key");
        if (incrmtKey.isEmpty()) {
            return new Response(Response.NO, "NO_incrmt_key");
        }
        Long coin = cm.updateCoin(req.getUserId(), req.getAppId(),  Long.parseLong(incrmt), incrmtKey);
        JSONObject out = new JSONObject();
        out.put("coin", coin);
        return new Response(Response.YES, out);
    }
    
}
