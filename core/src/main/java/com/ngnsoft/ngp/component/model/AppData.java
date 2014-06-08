package com.ngnsoft.ngp.component.model;

import java.io.Serializable;

import com.ngnsoft.ngp.model.UserAppData;

public class AppData extends UserAppData {

	private Long coin;

    private Integer level;

    public Long getCoin() {
		return coin;
	}

	public void setCoin(Long coin) {
		this.coin = coin;
	}

	public AppData() {
    }

    public AppData(Long userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public Serializable getPrimaryKey() {
        return userId;
    }

    @Override
    public void setPrimaryKey(Object o) {
        this.userId = (Long) o;
    }
    
    @Override
    public void prepareForSave() {
    	super.prepareForSave();
    	if(coin == null) coin = 0l;
    	if(level == null) level = 0;
    	
    }
}
