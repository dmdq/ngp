package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.Zone;

/**
 * 
 * @author yjl
 *
 */
public interface ZoneService extends GenericManager {
	
	void saveZone(Zone zone, String[] engineNames);
	
	void updateZone(Zone zone, String[] engineNames);
	
	void delZone(Long id);
}
