package com.ngnsoft.ngp;

import com.ngnsoft.ngp.util.BouncyCastleUtil;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Request extends JSONData {
    
    public Request() {
        
    }
    
    public Request(JSONObject jo) {
        super(jo);
    }
    
    public boolean hasKey(String key) {
        if (getBizData() == null) {
            return false;
        }
        return getBizData().has(key) && getBizData().get(key) != null;
    }
    
    public String getKey() {
        return super.getStr("key");
    }
    
    public void setKey(String key) {
        put("key", key);
    }
    
    public String getSession() {
        return super.getStr("sid");
    }
    
    public void setSession(String session) {
        put("sid", session);
    }
    
    public String getDvid() {
        return super.getStr("dvid");
    }
    
    public void setDvid(String dvid) {
        put("dvid", dvid);
    }
    
    public String getMacId() {
        String deviceId = super.getStr("deviceId");
        if (deviceId != null) {
            deviceId = deviceId.toLowerCase();
        }
        if (deviceId != null && (deviceId.isEmpty() || deviceId.equalsIgnoreCase("e3f5536a141811db40efd6400f1d0a4e"))) {
            deviceId = null;
        }
        return deviceId;
    }
    
    public void setMacId(String deviceId) {
        if (deviceId != null) {
            deviceId = deviceId.toLowerCase();
        }
        put("deviceId", deviceId);
    }
    
//    public void setDeviceId(String deviceId) {
//        
//    }
    
    public String getIdfa() {
        String idfa = super.getStr("idfa");
        if (idfa != null) {
            idfa = idfa.toLowerCase();
        }
        if (idfa != null && idfa.isEmpty()) {
            idfa = null;
        }
        return idfa;
    }
    
    public void setIdfa(String idfa) {
        if (idfa != null) {
            idfa = idfa.toLowerCase();
        }
        put("idfa", idfa);
    }
    
    public Long getUserId() {
        return super.getLong("userId");
    }
    
    public void setUserId(Long userId) {
        put("userId", userId);
    }
    
    public String getPswd() {
        return super.getStr("pswd");
    }
    
    public void setPswd(String pswd) {
        put("pswd", pswd);
    }
    
    public String getEpswd() {
        return super.getStr("epswd");
    }
    
    public void setEpswd(String epswd) {
        put("epswd", epswd);
    }
    
    public String getAppVersion() {
        return super.getStr("appVersion");
    }
    
    public void setAppVersion(String appVersion) {
        put("appVersion", appVersion);
    }
    public String getAppId() {
        return super.getStr("appId");
    }
    
    public void setAppId(String appId) {
        put("appId", appId);
    }
    
    public String getIP() {
        return super.getStr("ip");
    }
    
    public void setIP(String ip) {
        put("ip", ip);
    }
    
    private Long getEnc() {
        return super.getLong("enc");
    }
    
    public boolean isEnc() {
        boolean ret = false;
        Long enc = getEnc();
        if (enc != null && enc == 1) {
            ret = true;
        }
        return ret;
    }
    
    private Long getDebug() {
        return super.getLong("debug");
    }
    
    public boolean isDebug() {
        boolean ret = false;
        Long debug = getDebug();
        if (debug != null && debug == 1) {
            ret = true;
        }
        return ret;
    }

    @Override
    public String getStr(String k) {
        String v = null;
        try {
            v = getBizData().getString(k);
        } catch (Exception ex) {
        }
        return v;
    }

    @Override
    public Long getLong(String k) {
        Long v = null;
        try {
            v = getBizData().getLong(k);
        } catch (Exception ex) {
        }
        return v;
    }

    @Override
    public JSONArray getJSONArray(String k) {
        JSONArray v = null;
        try {
            v = getBizData().getJSONArray(k);
        } catch (Exception ex) {
        }
        return v;
    }

    @Override
    public JSONObject getJSONObject(String k) {
        JSONObject v = null;
        try {
            v = getBizData().getJSONObject(k);
        } catch (Exception ex) {
        }
        return v;
    }
    
    public JSONObject getBizData() {
        JSONObject ret = null;
        boolean isEnc = false;
        if ("APRQ".equalsIgnoreCase(getKey())) {
            Long enc = getEnc();
            if (enc != null) {
                if (enc == 1) {
                    isEnc = true;
                }
            } else {
                isEnc = true;
            }
        }
        if (isEnc) {
            String encBiz = super.getStr("biz");
            String secretKey = BouncyCastleUtil.AES_KEY_PRE + getSession().substring(0, 8);
            String biz = BouncyCastleUtil.aesDecode(secretKey, encBiz);
            if (biz != null) {
                ret = new JSONObject(biz);
            }
        } else {
            ret = super.getJSONObject("biz");
        }
        return ret;
    }
    
}
