package com.ngnsoft.ngp.component.bingo.model;

import java.io.Serializable;

public class Activity implements Serializable {

    public static final int TYPE_JOIN = 1;
    public static final int TYPE_EXIT = 0;
    public static final int TYPE_CHAT = 2;
    private Integer type;
    private Long time;
    private String data;
    private Long rid;
    

    public Integer getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public Long getRid() {
        return rid;
    }

    public void setRid(Long rid) {
        this.rid = rid;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
    
    @Override
    public boolean equals(Object obj) {
    	if(obj instanceof Activity) {
    		Activity a = (Activity) obj;
    		if(this.type == a.type && this.rid.intValue() == a.getRid().intValue()) {
    			return true;
    		}
    	}
    	return false;
    }
    
}
