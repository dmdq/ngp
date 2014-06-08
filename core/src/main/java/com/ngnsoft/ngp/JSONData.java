package com.ngnsoft.ngp;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class JSONData {
    
    protected JSONObject data = null;
    
    public JSONData() {
        data = new JSONObject();
    }
    
    public JSONData(JSONObject jo) {
        data = jo;
    }

    public JSONObject getData() {
        return data;
    }

    public void setData(JSONObject data) {
        this.data = data;
    }
    
    public String getStr(String k) {
        String v = null;
        try {
            v = data.getString(k);
        } catch (Exception ex) {
        }
        return v;
    }
    
    public Long getLong(String k) {
        Long v = null;
        try {
            v = data.getLong(k);
        } catch (Exception ex) {
        }
        return v;
    }
    
    public JSONObject getJSONObject(String k) {
        JSONObject v = null;
        try {
            v = data.getJSONObject(k);
        } catch (Exception ex) {
        }
        return v;
    }
    
    public JSONArray getJSONArray(String k) {
        JSONArray v = null;
        try {
            v = data.getJSONArray(k);
        } catch (Exception ex) {
        }
        return v;
    }
    
    
    public void put(String sk, Object ov) {
        try {
            data.put(sk, ov);
        } catch (Exception ex) {
        }
    }
    
    public void remove(String sk) {
        try {
			data.remove(sk);
		} catch (Exception e) {
		}
    }
    
}
