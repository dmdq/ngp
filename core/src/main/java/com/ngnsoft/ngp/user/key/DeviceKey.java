package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class DeviceKey extends SnsKey {
    
    public static final String TYPE = "device";
    
    protected DeviceKey() {
        keyType = TYPE;
    }
    
    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("deviceId");
    }
    
}
