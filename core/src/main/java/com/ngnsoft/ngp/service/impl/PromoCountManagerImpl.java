package com.ngnsoft.ngp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.model.PromoCount;
import com.ngnsoft.ngp.service.PromoCountManager;

@Service
public class PromoCountManagerImpl extends GenericManagerImpl implements
		PromoCountManager {

	@Override
	public Date getLastCountDate() {
		List<PromoCount> promoCounts = findByNamedQuery("find_last_count_date", null, PromoCount.class);
		return promoCounts.size() == 0 ? null : promoCounts.get(0).getCountDate();
	}


}
