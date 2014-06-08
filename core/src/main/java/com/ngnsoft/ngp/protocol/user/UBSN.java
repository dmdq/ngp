package com.ngnsoft.ngp.protocol.user;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class UBSN extends SyncExecutor {
    
    @Autowired
    private UserManager um;

    @Override
    public Response exec(Request req, App app) throws Exception {
        if (!req.hasKey("sns")) {
            return new Response(Response.NO, "NO_sns");
        }
        if (!req.hasKey("snsId")) {
            return new Response(Response.NO, "NO_snsId");
        }
        return um.ubsn(req);
    }
    
}
