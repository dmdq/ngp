package com.ngnsoft.ngp.user.key;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class WeiboKey extends SnsKey {
    
    public static final String TYPE = "weibo";
    
    protected WeiboKey() {
        keyType = TYPE;
    }
    
    @Override
    public void mapKeys(JSONObject json){
        super.mapKeys(json);
        try {
            json.put("nick", json.getString("screen_name"));
            json.put("avatar", json.getString("profile_image_url"));
        } catch (JSONException ex) {
        }
    }

    @Override
    public String getSnsId(JSONObject json) throws JSONException {
        return json.getString("id");
    }
    
}
