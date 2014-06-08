package com.ngnsoft.ngp.service;

import java.util.List;

import com.ngnsoft.ngp.model.Zone;

/**
 * 
 * @author yjl
 *
 */
public interface ZoneManager extends GenericManager {
	
	void saveAll(Zone zone);
	
	List<Long> getZonesByAppAndEngine(String appId, String engineId, String baseId);
	
}
