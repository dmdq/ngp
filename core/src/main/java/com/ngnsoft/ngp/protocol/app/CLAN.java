package com.ngnsoft.ngp.protocol.app;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class CLAN extends SyncExecutor {

    @Autowired
    private UserManager userManager;

    @Override
    public Response exec(Request req, App app) throws Exception {
    	AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
    	return component.uscl(app, req);
    }

}
