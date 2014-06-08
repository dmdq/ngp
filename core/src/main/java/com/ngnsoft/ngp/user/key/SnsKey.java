package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

import com.ngnsoft.ngp.model.UserKey;

/**
 *
 * @author fcy
 */
public abstract class SnsKey extends UserKey {
    
     public void mapKeys(JSONObject json) {
        try {
            userKey = getSnsId(json);
        } catch (JSONException ex) {
        }
    }
    
    public abstract String getSnsId(JSONObject json) throws JSONException;

    public boolean validate(String user, String token) {
        return true;
    }
    
}
