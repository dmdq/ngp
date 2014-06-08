package com.ngnsoft.ngp.util;

import java.util.Iterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class JSONUtil {
    
    public static JSONObject mergeAndUpdate(JSONObject old, JSONObject up) {
        if (old == null || old.length() == 0) {
            return up;
        }
        if (up != null && up.length() > 0) {
            Iterator it = up.keys();
            while (it.hasNext()) {
                String sk = (String)it.next();
                try {
                    old.put(sk, up.get(sk));
                } catch (JSONException ex) {
                }
            }
        }
        return old;
    }
    
}
