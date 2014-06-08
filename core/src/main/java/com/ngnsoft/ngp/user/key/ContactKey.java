package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class ContactKey extends SnsKey {
    
    public static final String TYPE = "contact";
    
    protected ContactKey() {
        keyType = TYPE;
    }

    @Override
    public void mapKeys(JSONObject json) {
        super.mapKeys(json);
        try {
            json.put("avatar", json.getString("photoid"));
        } catch (JSONException ex) {
        }
    }

    @Override
    public String getSnsId(JSONObject json) throws JSONException  {
        return json.getString("mobile");
    }
    
}
