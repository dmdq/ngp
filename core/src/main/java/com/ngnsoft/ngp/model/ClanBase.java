package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public abstract class ClanBase extends BaseModel {
    
    protected Long id;
    protected String name;
    protected String icon;
    protected String notice;
    protected Long creator;

    public Long getCreator() {
        return creator;
    }

    public void setCreator(Long creator) {
        this.creator = creator;
    }

    public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long) o;
    }
    
}
