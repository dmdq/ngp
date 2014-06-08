package com.ngnsoft.ngp.component.coc.model;

import com.ngnsoft.ngp.model.RoleData;
import java.util.Date;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class CocRoleData extends RoleData {
    
    public static final String LOCK_TIME_KEY = "lockTime";
    public static final String LOCK_STATUS_KEY = "lock";
    
    public static final int SEARCH_LOCK_YES = 1;
    public static final int SEARCH_LOCK_NO = 0;
    
    public static final int LOCK_TIMEOUT_MINUTE = 4;
    public static final int LOCK_TIMEOUT_MILLIS = 240000; 

    private Integer trophy;
    private Date shieldTime;
    private Date lockTime;
    private Integer searchLock;
    private Integer townLevel;
    private Integer gem;

    public Integer getGem() {
		return gem;
	}

	public void setGem(Integer gem) {
		this.gem = gem;
	}

	public Date getLockTime() {
        return lockTime;
    }

    public void setLockTime(Date lockTime) {
        this.lockTime = lockTime;
    }
    
    public Integer getTownLevel() {
        return townLevel;
    }

    public void setTownLevel(Integer townLevel) {
        this.townLevel = townLevel;
    }

    public Integer getSearchLock() {
        return searchLock;
    }

    public void setSearchLock(Integer searchLock) {
        this.searchLock = searchLock;
    }

    public Date getShieldTime() {
        return shieldTime;
    }

    public void setShieldTime(Date shieldTime) {
        this.shieldTime = shieldTime;
    }

    public Integer getTrophy() {
        return trophy;
    }

    public void setTrophy(Integer trophy) {
        this.trophy = trophy;
    }

    @Override
    public String getDbName() {
        return "coc";
    }
    
    @Override
    public String getTableName() {
        return "role_data";
    }
    
    @Override
    public String getModelName() {
        return "roleData";
    }
    
    @Override
    public JSONObject toJSONObject() {
    	JSONObject jo = super.toJSONObject();
    	if(lockTime != null) {
    		jo.put(LOCK_TIME_KEY, (lockTime.getTime() / 1000) * 1000);
    	}
        jo.put(LOCK_STATUS_KEY, searchLock);
    	return jo;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        searchLock = 0;
    }
    
}
