package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class GoogleplayKey extends SnsKey {
    
    public static final String TYPE = "googleplay";
    
    protected GoogleplayKey() {
        keyType = TYPE;
    }

    @Override
    public void mapKeys(JSONObject json) {
        super.mapKeys(json);//todo
    }

    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("id");//todo
    }
    
}
