package com.ngnsoft.ngp.protocol.app;

import org.json.JSONObject;
import org.springframework.stereotype.Controller;

import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppConfig;
import com.ngnsoft.ngp.model.AppConfigBase;
import com.ngnsoft.ngp.model.UserProfile;
import com.ngnsoft.ngp.util.JSONUtil;

/**
 *
 * @author fcy
 */
@Controller
public class UPNF extends SyncExecutor {

    @Override
    public Response exec(Request req, App app) throws Exception {
        String appId = req.getAppId();
        String baseId = app.getBaseId();
        Response result = new Response(Response.YES);
        AppConfig ac = gm.get(appId, AppConfig.class);
        JSONObject output;
        if (ac != null) {
            output = ac.toJSONObject();
        } else {
            output = new JSONObject();
        }
        AppConfig acb = gm.get(baseId, AppConfigBase.class);
        if (acb != null) {
            output = JSONUtil.mergeAndUpdate(acb.toJSONObject(), output);
        }
        result.setOutput(output);
        String country = null;
        UserProfile up = null;
        Long userId = req.getUserId();
        if (userId != null) {
            up = gm.get(userId, UserProfile.class);
            country = up.getCountry();
        }
        if (country == null || country.isEmpty()) {
            String ip = req.getIP();
            IP2Location loc = new IP2Location();
            IP2Location.IPDatabasePath = Engine.getInstance().getConfig().getIpDataFile();
            IP2Location.IPLicensePath = Engine.getInstance().getConfig().getIpLicenseFile();
            IPResult rst = loc.IPQuery(ip);
             if ("OK".equals(rst.getStatus())) {
                country = rst.getCountryShort();
                if (up != null) {
                    UserProfile upUserProfile = new UserProfile(up.getUserId());
                    upUserProfile.setCountry(country);
                    gm.update(upUserProfile);
                }
             } else {
                 country = "-";
             }
        }
        result.put("country", country);
        return result;
    }
    
}
