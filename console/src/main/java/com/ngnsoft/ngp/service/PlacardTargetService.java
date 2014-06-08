package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.model.PlacardTarget;


public interface PlacardTargetService extends GenericManager {
	
	int saveBatch(PlacardTarget placardTarget, String[] zoneIds);
	
}
