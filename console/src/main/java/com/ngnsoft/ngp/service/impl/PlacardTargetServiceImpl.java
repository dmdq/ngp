package com.ngnsoft.ngp.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.AppVersion;
import com.ngnsoft.ngp.model.PlacardTarget;
import com.ngnsoft.ngp.service.PlacardTargetService;

@Service
public class PlacardTargetServiceImpl extends GenericManagerImpl implements
PlacardTargetService {

	@Override
	public int saveBatch(PlacardTarget placardTarget, String[] zoneIds) {
		if(placardTarget.getAppId() != null && !"".equals(placardTarget.getAppId())){
			if(zoneIds.length != 0){
				for (String zoneId : zoneIds) {
					placardTarget.setZoneId(Long.parseLong(zoneId));
					AppVersion appVersion = new AppVersion();
					appVersion.setAppId(placardTarget.getAppId());
					List<AppVersion> appVersions = this.findMulti(appVersion);

					List<PlacardTarget> placardTargets = this.findMulti(placardTarget);
					if(placardTargets.size() != 0){
						for (PlacardTarget placardTarget2 : placardTargets) {
							if(appVersions.size() != 0){
								for (AppVersion aVersion : appVersions) {
									if(!placardTarget2.getAppVersion().equals(aVersion.getVersion())){
										placardTarget2.setAppVersion(aVersion.getVersion());
										this.save(placardTarget2);
									}
								}
							} else {
								placardTarget.setAppVersion(null);
								placardTarget.setZoneId(Long.parseLong(zoneId));
								save(placardTarget);
							}
						}
					} else {
						if(appVersions.size() != 0){
							for (AppVersion aVersion : appVersions) {
								placardTarget.setAppVersion(aVersion.getVersion());
								placardTarget.setZoneId(Long.parseLong(zoneId));
								save(placardTarget);
							}
						} else {
							placardTarget.setAppVersion(null);
							placardTarget.setZoneId(Long.parseLong(zoneId));
							save(placardTarget);
						}
					}
				}
			} else {
				placardTarget.setZoneId(null);
				AppVersion appVersion = new AppVersion();
				appVersion.setAppId(placardTarget.getAppId());
				List<AppVersion> appVersions = this.findMulti(appVersion);

				List<PlacardTarget> placardTargets = this.findByNamedQuery("find_placard_target_by_zoneId", placardTarget, PlacardTarget.class);
				if(placardTargets.size() != 0){
					for (PlacardTarget placardTarget2 : placardTargets) {
						if(appVersions.size() != 0){
							for (AppVersion aVersion : appVersions) {
								if(!placardTarget2.getAppVersion().equals(aVersion.getVersion())){
									placardTarget2.setAppVersion(aVersion.getVersion());
									this.save(placardTarget2);
								}
							}
						} else {
							placardTarget.setAppVersion(null);
							placardTarget.setZoneId(Long.parseLong(null));
							save(placardTarget);
						}
					}
				} else {
					if(appVersions.size() != 0){
						for (AppVersion aVersion : appVersions) {
							placardTarget.setAppVersion(aVersion.getVersion());
							placardTarget.setZoneId(null);
							save(placardTarget);
						}
					} else {
						placardTarget.setAppVersion(null);
						placardTarget.setZoneId(Long.parseLong(null));
						save(placardTarget);
					}
				}
			}
		} else {
			placardTarget.setBaid(false);
			placardTarget.setAppId(null);
			placardTarget.setAppVersion(null);
			for (String zoneId : zoneIds) {
				placardTarget.setZoneId(Long.parseLong(zoneId));
			}
			save(placardTarget);
		}
		
		if(placardTarget.getId() != null){
			return 0;
		} else {
			return -1;
		}
		
	}
}
