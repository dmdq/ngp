package com.ngnsoft.ngp.component.dragon.model;

import java.io.Serializable;

import com.ngnsoft.ngp.model.UserAppData;

/**
 *
 * @author fcy
 */
public class DragonData extends UserAppData {
	
	private Long coin;

    private Integer level;

    public Long getCoin() {
		return coin;
	}

	public void setCoin(Long coin) {
		this.coin = coin;
	}

	public DragonData() {
    }

    public DragonData(Long userId) {
        this.userId = userId;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    @Override
    public String getDbName() {
        return "dragon";
    }

    @Override
    public Serializable getPrimaryKey() {
        return userId;
    }

    @Override
    public void setPrimaryKey(Object o) {
        this.userId = (Long) o;
    }
}
