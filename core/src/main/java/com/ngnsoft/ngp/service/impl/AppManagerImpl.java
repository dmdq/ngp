package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppZone;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.AppManager;

/**
 *
 * @author fcy
 */
@Service
public class AppManagerImpl extends GenericManagerImpl implements AppManager {

    @Override
    public void saveAll(App app) {
        List<Zone> ens = app.getZones();
        for (Zone zone : ens) {
        	AppZone az = new AppZone();
        	if(app.getId().equals(app.getBaseId())){
        		az.setIsBaid(true);
        	}
        	az.setAppId(app.getId());
        	az.setCreateTime(new Date());
        	az.setZoneId(zone.getId());
        	save(zone);
		}
        save(app);
    }

}
