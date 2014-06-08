package com.ngnsoft.ngp.protocol.app;

import org.springframework.stereotype.Controller;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;

/**
 *
 * @author fcy
 */
@Controller
public class APMS extends SyncExecutor {

    @Override
    public Response exec(Request req, App app) throws Exception {
    	AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
        return component.apms(app, req);
    }
}
