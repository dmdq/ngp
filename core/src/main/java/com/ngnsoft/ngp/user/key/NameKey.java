package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Not Used
 * @author fcy
 */
public class NameKey extends SnsKey {
    
    public static final String TYPE = "name";
    
    protected NameKey() {
        keyType = TYPE;
    }
    
    @Override
    public boolean validate(String user, String token) {
        return true;
    }

    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("name");
    }
    
}
