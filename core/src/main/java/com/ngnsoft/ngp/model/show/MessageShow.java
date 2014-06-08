package com.ngnsoft.ngp.model.show;

import com.ngnsoft.ngp.model.Message;
import org.json.JSONException;
import org.json.JSONObject;

public class MessageShow extends Message {
	
	protected String fromNick;
	
	protected String fromAvatar;
	
	public String getFromNick() {
		return fromNick;
	}
	public void setFromNick(String fromNick) {
		this.fromNick = fromNick;
	}
	public String getFromAvatar() {
		return fromAvatar;
	}
	public void setFromAvatar(String fromAvatar) {
		this.fromAvatar = fromAvatar;
	}

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = super.toJSONObject();
        try {
//            jo.put("id", id);
//            jo.put("from", from);
//            jo.put("title", title);
//            jo.put("body", body);
//            jo.put("attach", attach);
//            jo.put("attachStatus", attachStatus);
//            jo.put("type", type);
//            jo.put("status", status);
            jo.put("createTime", createTime.getTime());
            jo.remove("to");
        } catch (JSONException ex) {
        }
        return jo;
    }
        
        
}
