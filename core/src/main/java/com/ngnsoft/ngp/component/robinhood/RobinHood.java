package com.ngnsoft.ngp.component.robinhood;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.robinhood.service.RobinManager;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 *
 * @author fcy
 */
public class RobinHood extends AppComponent {
    
    public static final String APP_BASE_ID = "553777703";
    
    @Autowired
    @Qualifier
    private RobinManager rm;

    @Override
    public Response lsen(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req) {
        return rm.lsenAll(app, zoneEnginesMap, req);
    }
    
}
