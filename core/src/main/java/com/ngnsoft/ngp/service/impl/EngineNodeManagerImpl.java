package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.EngineNodeManager;

/**
 *
 * @author fcy
 */
@Service
public class EngineNodeManagerImpl extends GenericManagerImpl implements EngineNodeManager {

    @Override
    public int checkHealth(EngineNode en) {
        long nw = System.currentTimeMillis();
        if (en.getStatus() >= EngineNode.STATUS_LIVE && en.getStatusLock() == EngineNode.STATUS_LOCK_N) {
            if (nw - en.getTouchTime().getTime() > EngineNode.DOWN_TIME_SECOND * 1000) {
                en.setStatus(EngineNode.STATUS_DOWN);
                en.setStatusDesc(EngineNode.STATUS_DESC_DOWN);
                update(en);
            }
        }
        return en.getStatus();
    }

	@Override
	public Map<Zone, List<EngineNode>> getEngineNodeByApp(App app) {
		Map paramMap = new HashMap();
        paramMap.put("appId", app.getId());
        paramMap.put("baseId", app.getBaseId());
        List<Zone> zones = findByNamedQuery("find_zone_by_appId_or_baseId", paramMap, Zone.class);
        
        Map<Zone, List<EngineNode>> zoneEnginesMap = new HashMap<Zone, List<EngineNode>>();
        for(Zone zone : zones) {
        	if(zone.getFavEngineId() != null) {
        		List<EngineNode> engineNodes = zone.getEngineNodes();
        		List<EngineNode> singleEngineNodes = new ArrayList<EngineNode>();
        		for(EngineNode engine : engineNodes) {
        			if(engine.getId().equals(zone.getFavEngineId())) {
        				singleEngineNodes.add(engine);
        				break;
        			}
        		}
        		zoneEnginesMap.put(zone, singleEngineNodes);
        	} else {
        		zoneEnginesMap.put(zone, zone.getEngineNodes());
        	}
        	
        }
	        		
		return zoneEnginesMap;
	}
	
}
