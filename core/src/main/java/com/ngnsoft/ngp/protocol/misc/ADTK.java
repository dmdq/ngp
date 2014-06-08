package com.ngnsoft.ngp.protocol.misc;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.AdTrack;
import com.ngnsoft.ngp.model.App;

/**
 *
 * @author fcy
 */
public class ADTK extends SyncExecutor  {
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        String appid = req.getAppId();
        String dvid  = req.getDvid();
        String adAction = req.getStr("action");
        String adAppid = req.getStr("appid");
        AdTrack acTrack = new AdTrack(null, adAppid, adAction, appid, dvid);
        Response resp = null;
        return resp;
    }
    
}
