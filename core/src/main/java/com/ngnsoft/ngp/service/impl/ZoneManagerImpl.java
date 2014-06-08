package com.ngnsoft.ngp.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.ZoneManager;

/**
 * 
 * @author yjl
 *
 */
@Service
@SuppressWarnings({ "unchecked", "rawtypes" })
public class ZoneManagerImpl extends GenericManagerImpl implements ZoneManager {

	@Override
	public void saveAll(Zone zone) {
//		List<EngineNode> ens = zone.getEngineNodes();
//        for (EngineNode engineNode : ens) {
//        	EngineNode en = new EngineNode();
//        	save(zone);
//		}
//        save(zone);
	}

	@Override
	public List<Long> getZonesByAppAndEngine(String appId, String engineId, String baseId) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("appId", appId);
		paramMap.put("baseId", baseId);
		paramMap.put("engineId", engineId);
		List<Zone> zones = findByNamedQuery("find_zones_by_app_and_engine", paramMap, Zone.class);
		List<Long> zoneIds = new ArrayList<Long>();
		for(Zone zone : zones) {
			zoneIds.add(zone.getId());
		}
		return zoneIds;
	}
	
}
