package com.ngnsoft.ngp.protocol.sale;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.model.SaleHistory;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class RMSL extends SyncExecutor {
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        if(req.hasKey("uuids")){
            AppComponent appCompoment = AppComponentFactory.getComponent(app.getBaseId());
            String ida = req.getStr("uuids");
            String[] ids = ida.split(",");
            for(String id:ids) {
                Sale sale = new Sale(Long.parseLong(id));
                sale.setDbName(appCompoment.getDbName());
                sale = gm.findObject(sale);
                if (sale != null) {
                    SaleHistory saleHistory = new SaleHistory();
                    saleHistory.setDbName(appCompoment.getDbName(saleHistory));
                    saleHistory.setUserId(req.getUserId());
                    saleHistory.setAd(sale.getAd());
                    saleHistory.setAppId(sale.getAppId());
                    saleHistory.setDeviceId(sale.getDeviceId());
                    saleHistory.setIp(sale.getIp());
                    saleHistory.setPid(sale.getPid());
                    saleHistory.setJsonData(sale.getJsonData());
                    gm.save(saleHistory);
                    sale.setDbName(appCompoment.getDbName());
                    gm.remove(sale);
                }
            }
            return new Response(Response.YES);
        } else {
            return new Response(Response.NO,"NO_uuids");
        }
    }
}
