package com.ngnsoft.ngp.protocol.engine;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.EngineNodeManager;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * List zone by app. Choice low-loading engine as zone fav_engine
 * 
 * @author fcy
 */
@Controller
public class LSEN extends SyncExecutor {
    
    @Autowired
    private EngineNodeManager enm;

    @Override
    public Response exec(Request req, App app) throws Exception {
        Map<Zone, List<EngineNode>> zoneEnginesMap = enm.getEngineNodeByApp(app);
        if (zoneEnginesMap.isEmpty()) {
            return new Response(Response.NO, "NO_ZONE");
        } else {
            AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
            return component.lsen(app, zoneEnginesMap, req);
        }
    }
    
}
