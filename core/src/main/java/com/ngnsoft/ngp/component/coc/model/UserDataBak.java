package com.ngnsoft.ngp.component.coc.model;

import org.springframework.util.ClassUtils;


/**
 *
 * @author fcy
 */
public class UserDataBak extends UserData {
	
	public static final String TABLE_NAME = "user_data_bak";
	
	@Override
	public String getTableName() {
		return TABLE_NAME;
	}
	
	@Override
    public String getModelName() {
        return ClassUtils.getShortName(this.getClass().getSuperclass());
    }
}
