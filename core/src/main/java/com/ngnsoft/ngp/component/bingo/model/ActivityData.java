package com.ngnsoft.ngp.component.bingo.model;

import java.io.Serializable;

import org.json.JSONObject;

public class ActivityData implements Serializable {

    public static final int TYPE_JOIN = 1;
    public static final int TYPE_EXIT = 0;
    public static final int TYPE_CHAT = 2;
    private Integer type;
    private Long time;
    private JSONObject data;
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

    public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	@Override
    public boolean equals(Object obj) {
    	if(obj instanceof ActivityData) {
    		ActivityData a = (ActivityData) obj;
    		if(this.type == a.type && this.rid.intValue() == a.getRid().intValue()) {
    			return true;
    		}
    	}
    	return false;
    }
    
    public static void main(String[] args) {
		Activity entity = new Activity();
		entity.setRid(123123l);
		entity.setType(1);
		entity.setTime(123123l);
		entity.setData("{\"data\":\"123123123\"}");
		ActivityData jsonData = new ActivityData();
		jsonData.setRid(entity.getRid());
		jsonData.setTime(entity.getTime());
		jsonData.setType(entity.getType());
		jsonData.setData(new JSONObject(entity.getData()));
		System.out.println(new JSONObject(jsonData));
	}
    
}
