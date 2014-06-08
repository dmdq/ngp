package com.ngnsoft.ngp.model;

import org.springframework.util.ClassUtils;

import com.ngnsoft.ngp.util.MiscUtil;


/**
 *
 * @author fcy
 */
public class UserActivityHistory extends UserActivity {
    
	@Override
	public String getModelName() {
		return ClassUtils.getShortName(this.getClass());
	}
	
	@Override
	public String getTableName() {
		return MiscUtil.classToDbName(this.getClass());
	}
    
}
