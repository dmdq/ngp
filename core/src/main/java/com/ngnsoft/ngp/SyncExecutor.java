package com.ngnsoft.ngp;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.service.GenericManager;

/**
 *
 * @author fcy
 */
public abstract class SyncExecutor implements Executor {
    
    @Autowired
    @Qualifier("genericManager")
    protected GenericManager gm;
    
    public void onAction(Request req, App app) {
    	AppComponent appComponent = AppComponentFactory.getComponent(app.getBaseId());
        UserAppData uad = AppComponentFactory.getUadModel(req.getUserId(), app.getBaseId());
        uad.setDbName(appComponent.getDbName());
        if(uad != null) {
        	uad.setActionTime(new Date());
            gm.update(uad);
        }
    }
    
}
