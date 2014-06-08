package com.ngnsoft.ngp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.model.PlacardTarget;
import com.ngnsoft.ngp.service.PlacardService;

/**
 * 
 * @author yjl
 */
@Service
public class PlacardServiceImpl extends GenericManagerImpl implements PlacardService {

	@Override
	public void removePlacard(Long placardId) {
		remove(placardId, Placard.class);
		
		//remove all placardApp info by placardId
		List<PlacardTarget> placardApps = this.findMulti(new PlacardTarget());
		for (PlacardTarget placardApp : placardApps) {
			if(placardApp.getPlacardId().equals(placardId)){
				remove(placardApp);
			}
		}
	}
}
