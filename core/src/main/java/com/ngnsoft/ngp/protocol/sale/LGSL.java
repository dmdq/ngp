package com.ngnsoft.ngp.protocol.sale;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.SaleHistory;
import com.ngnsoft.ngp.service.UserManager;
import java.util.List;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 
 * @author fcy
 */
@Controller
public class LGSL extends SyncExecutor {
    
    @Autowired
    private UserManager um;
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        String pid = null;
        if (req.hasKey("pid")) {
            pid = req.getStr("pid");
        }
        String deviceId = um.getDvid(req);
        if (deviceId != null) {
            JSONObject biz = req.getBizData();
            String bizStr = null;
            if (biz != null) {
                bizStr = biz.toString();
            }
            SaleHistory saleHistory = new SaleHistory(null, "iap", req.getAppId(), deviceId, pid, bizStr, req.getUserId(), req.getIP());
            saleHistory.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName(saleHistory));
            gm.save(saleHistory);
        }
        return new Response(Response.YES);
    }

}
