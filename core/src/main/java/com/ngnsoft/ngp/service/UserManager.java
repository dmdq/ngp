package com.ngnsoft.ngp.service;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.UserAppData;
import java.io.IOException;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author fcy
 */
public interface UserManager extends GenericManager {

    public Response login(Request req, App app);

    public Response swus(Request req, App app);

    public Response updateUser(Request req) throws JSONException, IOException;

    public void addFriends(Long userId, App app, JSONArray frds);

    public Response delFriends(Long userId, App app, JSONArray frds);

    public JSONObject listFriends(Request req, String sns, Pagination page) throws JSONException;

    public Response lgsn(Long userId, String appId, String sns, JSONObject data);

    public Response mtuf(Request req);

    public Response ubsn(Request req);

    public Response searchFacilitator(Request req) throws JSONException;

    public Response usdt(Request req);

    public JSONArray getUserData(String... userIds);

    public void setJsonDataFromAppData(JSONObject fromJson, Long userId, JSONObject friendJson, String baseId, UserAppData userData);

    public Response checkEpswd(Request req, App app);

    public Response checkDevice(Request req, App app);
    
    public Device getDevice(Request req);
    
    public String getDvid(Request req);
    
    public String checkSession(Long userId, String deviceId, App app, Request req);
    
}
