package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class EmailKey extends SnsKey {
    
    public static final String TYPE = "email";
    
    protected EmailKey() {
        keyType = TYPE;
    }
    
    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("email");
    }
    
}
