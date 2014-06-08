package com.ngnsoft.ngp.protocol.sale;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.service.UserManager;
import java.util.List;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class LSSL extends SyncExecutor {
    
    @Autowired
    private UserManager um;
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        JSONArray ja = new JSONArray();
        String deviceId = um.getDvid(req);
        if (deviceId != null) {
            Sale sale = new Sale();
            sale.setAppId(req.getAppId());
            sale.setDeviceId(deviceId);
            sale.setDbName(AppComponentFactory.getComponent(app.getBaseId()).getDbName(sale));
            List<Sale> l = gm.findMulti(sale);
            for (Sale s : l) {
                ja.put(s.toJSONObject());
            }
        }
        return new Response(Response.YES, ja);
    }
}
