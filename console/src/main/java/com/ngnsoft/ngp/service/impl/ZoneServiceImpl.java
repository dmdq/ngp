package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.AppZone;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.model.ZoneEngine;
import com.ngnsoft.ngp.service.ZoneService;

/**
 * 
 * @author yjl
 *
 */
@Service
public class ZoneServiceImpl extends GenericManagerImpl implements ZoneService {

	@Override
	public void saveZone(Zone zone, String[] engineNames) {
		this.save(zone);

		for (String engineId : engineNames) {
			ZoneEngine zoneEngine = new ZoneEngine();
			zoneEngine.setCreateTime(new Date());
			zoneEngine.setEngineId(engineId);
			zoneEngine.setZoneId(zone.getId());
			this.save(zoneEngine);
		}
	}

	@Override
	public void updateZone(Zone zone, String[] engineNames) {
		zone.setUpdateTime(new Date());
		this.update(zone);

		List<ZoneEngine> zoneEngines = this.findMulti(new ZoneEngine());

		//remove all zoneEngine by engineId
		for (ZoneEngine zoneEngine : zoneEngines) {
			if(zone.getId().equals(zoneEngine.getZoneId())){
				remove(zoneEngine);
			}
		}


		for (String engineId : engineNames) {
			ZoneEngine zoneEngine = new ZoneEngine();
			zoneEngine.setEngineId(engineId);
			zoneEngine.setZoneId(zone.getId());
			this.save(zoneEngine);
		}
	}

	@Override
	public void delZone(Long id) {
		List<AppZone> appZones = this.findMulti(new AppZone());
		for (AppZone appZone : appZones) {
			if(appZone.getZoneId().equals(id)){
				remove(appZone);
			}
		}
		
		List<ZoneEngine> zoneEngines = this.findMulti(new ZoneEngine());
		for (ZoneEngine zoneEngine : zoneEngines) {
			if(id.equals(zoneEngine.getZoneId())){
				remove(zoneEngine);
			}
		}
		
		remove(id, Zone.class);
	}
	
}
