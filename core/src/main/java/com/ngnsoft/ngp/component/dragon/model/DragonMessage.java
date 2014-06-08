package com.ngnsoft.ngp.component.dragon.model;

import com.ngnsoft.ngp.model.Message;

/**
 *
 * @author fcy
 */
public class DragonMessage extends Message {

	public static final int TO_GLOBAL = 0;
    public static final int TO_CLAN = 1;
    public static final int TO_CLAN_MEMBER = 2;
    
    public static final int ATTACH_APPLY_JOIN_TYPE = 0;
    public static final int ATTACH_HAD_JOIN_TYPE = 1;
    public static final int ATTACH_EXIT_TYPE = 2; 
    public static final int ATTACH_CICKED_TYPE = 3;
    public static final int ATTACH_CLAN_LEVEL_TYPE = 4;
    public static final int ATTACH_ROLE_TYPE = 5;
    
    public static final int ATTACH_REFUSE_JOIN_TYPE = 6;
    
    private Integer active;
	
	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}
	
	@Override
	public void prepareForSave() {
		super.prepareForSave();
		if(active == null) {
			active = ATTACH_APPLY_JOIN_TYPE;
		}
	}
	
	@Override
	public String getDbName() {
		return "dragon";
	}

	@Override
	public String getTableName() {
		return "dragon_message";
	}
}
