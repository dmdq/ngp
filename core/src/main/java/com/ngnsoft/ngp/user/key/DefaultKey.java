package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Not Used
 * @author fcy
 */
public class DefaultKey extends SnsKey {
    
    public static final String TYPE = "id";
    
    protected DefaultKey() {
        keyType = TYPE;
    }
    
    @Override
    public boolean validate(String user, String token) {
        return true;
    }

    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return String.valueOf(json.getLong("userId"));
    }
    
}
