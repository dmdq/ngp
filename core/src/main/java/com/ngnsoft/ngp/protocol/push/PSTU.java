package com.ngnsoft.ngp.protocol.push;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.misc.PushNotify;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.DataManager;

/**
 *
 * @author fcy
 */
@Controller
public class PSTU extends SyncExecutor {
	
	@Autowired
	private DataManager dataManager;

    @Override
    public Response exec(Request req, App app) throws Exception {
        String to = req.getStr("to");
        if(to == null || to.isEmpty()){
            return new Response(Response.NO, "NO_TO");
        }
        String[] tos = to.split(";");
        String content = req.getStr("content");
        if(content == null || content.isEmpty()){
            return new Response(Response.NO, "NO_CONTENT");
        }
        String push = req.getStr("push");
        if(push != null && !push.isEmpty()){
            PushNotify pn = new PushNotify();
            pn.setMessage(content);
            pn.setBadge(1);
            for (int i = 0; i < tos.length; i++) {
                String userId = tos[i];
                dataManager.push(Long.valueOf(userId), app, pn);
            }
        }
        return new Response(Response.YES);
    }
    
    
}
