package com.ngnsoft.ngp.model;

import com.ngnsoft.ngp.util.JSONUtil;
import java.io.Serializable;
import java.util.UUID;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Device extends BaseModel {
    
    private String id;
    
    private String mac;
    
    private String brand;
    
    private String model;
    
    private String os;
    
    private String macId;
    
    private String udid;
    
    private String openUdid;
    
    private String idfa;
    
    private String jsonAll;//json string of all
    
    public Device() {
        
    }
    
    public Device(String id) {
        
    }
    
    public Device(String id, String mac, String brand, String model, String os, String macId, String udid, String openUdid, String idfa, String jsonAll) {
        this.id = id;
        this.mac = mac;
        this.brand = brand;
        this.model = model;
        this.os = os;
        this.udid = udid;
        this.openUdid = openUdid;
        this.idfa = idfa;
        this.macId = macId;
        this.jsonAll = jsonAll;
    }

    
    public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getMacId() {
        return macId;
    }

    public void setMacId(String macId) {
        this.macId = macId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJsonAll() {
        return jsonAll;
    }

    public void setJsonAll(String jsonAll) {
        this.jsonAll = jsonAll;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }

    public String getOpenUdid() {
        return openUdid;
    }

    public void setOpenUdid(String openUdid) {
        this.openUdid = openUdid;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    @Override
    public Serializable getPrimaryKey() {
        return id;
    }

    @Override
    public void setPrimaryKey(Object o) {
        id = (String)o;
    }

    @Override
    protected JSONObject doJSONObject() {
        JSONObject jo = super.doJSONObject();
        jo.remove("jsonAll");
        return jo;
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject jo = null;
        try {
            jo = new JSONObject(jsonAll);
        } catch (Exception ex) {
        }
        return jo;
    }

    @Override
    public void prepareForSave() {
        super.prepareForSave();
        this.id = UUID.randomUUID().toString();
        if (macId == null) {
        	macId = "macId";
        }
        if (idfa == null) {
        	idfa = "idfa";
        }
        if (udid == null) {
        	udid = "udid";
        }
        if (openUdid == null) {
        	openUdid = "openUdid";
        }
        if (jsonAll == null) {
            JSONObject jo = doJSONObject();
            jsonAll = jo.toString();
        }
    }

    @Override
    public void prepareForUpdate() {
        super.prepareForUpdate();
        try {
        	JSONObject old =  new JSONObject();
        	if(jsonAll != null) {
        		old = new JSONObject(jsonAll);
        	}
            JSONObject up = doJSONObject();
            JSONObject jo = JSONUtil.mergeAndUpdate(old, up);
            jsonAll = jo.toString();
        } catch (JSONException ex) {
        }
    }
    
    public static void main(String[] args) {
    	String s = null;
    	JSONObject old =  new JSONObject(s);
    	System.out.println(old);
	}
    
}
