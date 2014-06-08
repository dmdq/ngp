package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class GamecenterKey extends SnsKey {
    
    public static final String TYPE = "gamecenter";
    
    protected GamecenterKey() {
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
