package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class FacebookKey extends SnsKey {
    
    public static final String TYPE = "facebook";
    
    protected FacebookKey() {
        keyType = TYPE;
    }

    @Override
    public void mapKeys(JSONObject json) {
        super.mapKeys(json);
    }
    
    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("id");
    }
    
}
