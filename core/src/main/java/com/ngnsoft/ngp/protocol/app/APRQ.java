package com.ngnsoft.ngp.protocol.app;

import org.springframework.stereotype.Controller;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserAppData;

/**
 *
 * @author fcy
 */
@Controller
public class APRQ extends SyncExecutor {

    @Override
    public Response exec(Request req, App app) throws Exception {
        AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
        return component.aprq(app, req);
    }

    @Override
    public void onAction(Request req, App app) {
        //do nothing
    }
}
