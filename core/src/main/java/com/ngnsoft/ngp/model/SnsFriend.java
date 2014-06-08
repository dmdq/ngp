package com.ngnsoft.ngp.model;

import java.io.Serializable;

/**
 *
 * @author fcy
 */
public class SnsFriend extends BaseModel {
	
    private Long id;
    
    private Long faUid;
    
    private String fbUk;
    
    private String fbKt;
    
    private Long fbUid;
    
    public SnsFriend() {
        
    }
    
    public SnsFriend(Long id, Long faUid, String fbUk, String fbKt, Long fbUid) {
        this.id = id;
        this.faUid = faUid;
        this.fbUk = fbUk;
        this.fbKt = fbKt;
        this.fbUid = fbUid;
    }
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFaUid() {
		return faUid;
	}

	public void setFaUid(Long faUid) {
		this.faUid = faUid;
	}

	public String getFbUk() {
		return fbUk;
	}

	public void setFbUk(String fbUk) {
		this.fbUk = fbUk;
	}

	public String getFbKt() {
		return fbKt;
	}

	public void setFbKt(String fbKt) {
		this.fbKt = fbKt;
	}

	public Long getFbUid() {
		return fbUid;
	}

	public void setFbUid(Long fbUid) {
		this.fbUid = fbUid;
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
