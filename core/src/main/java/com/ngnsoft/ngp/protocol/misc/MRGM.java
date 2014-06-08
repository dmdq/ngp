package com.ngnsoft.ngp.protocol.misc;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.enums.DataType;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.DateUtil;
import java.util.Date;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class MRGM extends SyncExecutor {

    private static String PROMO_KEY_PREFIX = "PROMO_";
    private static String NULL_PROMO_ID = "0";
    @Autowired
    private RedisImpl redisImpl;
    @Autowired
    private DataManager dataManager;

    @Override
    public Response exec(Request req, App app) throws Exception {
        String appId = req.getAppId();
        Response resp;
        if (req.hasKey("click")) {
            String uuid = req.getStr("click");
            //TODO
            redisImpl.incr(PROMO_KEY_PREFIX + DateUtil.formatDate(new Date()) + "_" + uuid);
            resp = new Response(Response.YES);
        } else {
            //TODO
            redisImpl.incr(PROMO_KEY_PREFIX + DateUtil.formatDate(new Date()) + "_" + NULL_PROMO_ID);
            List<JSONObject> resultArray = (List<JSONObject>) dataManager.getDataByType(DataType.PROMOTION, null);
            resp = new Response(Response.YES, new JSONArray(resultArray));
        }
        return resp;
    }
}
