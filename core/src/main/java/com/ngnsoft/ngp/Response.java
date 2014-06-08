package com.ngnsoft.ngp;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public class Response extends JSONData {

    public static final String YES = "yes";
    public static final String NO = "no";
    
    private Object attachObject;

    public Response() {
        put("tm", System.currentTimeMillis());
    }

    public Response(JSONObject m) {
        super(m);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret) {
        put("ret", ret);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret, String info) {
        this(ret);
        put("info", info);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret, JSONObject output) {
        this(ret);
        put("out", output);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret, String info, JSONObject output) {
        this(ret);
        put("info", info);
        put("out", output);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret, JSONArray output) {
        this(ret);
        put("out", output);
        put("tm", System.currentTimeMillis());
    }

    public Response(String ret, String info, JSONArray output) {
        this(ret);
        put("info", info);
        put("out", output);
        put("tm", System.currentTimeMillis());
    }

    public JSONObject getOutput() {
        return getJSONObject("out");
    }

    public void setOutput(JSONObject output) {
        put("out", output);
    }

    public JSONArray getArrayOutput() {
        return getJSONArray("out");
    }

    public void setArrayOutput(JSONArray output) {
        put("out", output);
    }

    public String getRet() {
        return getStr("ret");
    }

    public void setRet(String ret) {
        put("ret", ret);
    }

    public String getInfo() {
        return getStr("info");
    }

    public void setInfo(String info) {
        put("info", info);
    }

    public long getTM() {
        return getLong("tm");
    }

    public void setTM(long tm) {
        put("tm", tm);
    }

    public String getSid() {
        return getStr("sid");
    }

    public void setSid(String sid) {
        put("sid", sid);
    }

    public void setExtra(JSONObject extra) {
        put("extra", extra);
    }

    public boolean isSuccess() {
        return YES.equals(getRet());
    }

    public Object getAttachObject() {
        return attachObject;
    }

    public void setAttachObject(Object attachObject) {
        this.attachObject = attachObject;
    }

    @Override
    public String toString() {
        return data.toString();
    }
}
