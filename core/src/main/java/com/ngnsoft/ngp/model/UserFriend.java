package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class UserFriend extends BaseModel {
	
	public static final int UNCONFIRMED = 0;
	
	public static final int CONFIRMED = 1;
    
    private Long id;
    
    private Long fa;
    
    private Long fb;
    
    private Integer fs;
    
    private String msg;
    
    public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFa() {
		return fa;
	}

	public void setFa(Long fa) {
		this.fa = fa;
	}

	public Long getFb() {
		return fb;
	}

	public void setFb(Long fb) {
		this.fb = fb;
	}

	public Integer getFs() {
		return fs;
	}

	public void setFs(Integer fs) {
		this.fs = fs;
	}

	@Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (Long)o;
    }
    
}
