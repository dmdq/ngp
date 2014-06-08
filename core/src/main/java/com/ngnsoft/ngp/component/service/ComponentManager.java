package com.ngnsoft.ngp.component.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.AppMessageManager;
import com.ngnsoft.ngp.service.ClanManager;
import com.ngnsoft.ngp.service.GenericManager;
import java.util.List;
import java.util.Map;

/**
 *
 * @author fcy
 */
public interface ComponentManager extends ClanManager, AppMessageManager, GenericManager {
    
    public Response lsenAll(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req);
    
    public Response lsenFav(App app, Map<Zone, List<EngineNode>> zoneEnginesMap, Request req);
    
}
