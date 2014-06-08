package com.ngnsoft.ngp.protocol.device;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Device;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class UPDV extends SyncExecutor {
    
    @Override
    public Response exec(Request req, App app) throws Exception {
        Device device = new Device();
        
        if (!req.hasKey("brand")) {
            return new Response(Response.NO, "NO_brand");
        }
        device.setBrand(req.getStr("brand"));
        if (!req.hasKey("model")) {
            return new Response(Response.NO, "NO_model");
        }
        device.setModel(req.getStr("model"));
        if (!req.hasKey("OS")) {
            return new Response(Response.NO, "NO_OS");
        }
        device.setOs(req.getStr("OS"));
        if (req.hasKey("mac")) {
            device.setMac(req.getStr("mac").toUpperCase());
        }
        if (req.getMacId() != null) {
            device.setMacId(req.getMacId());
        }
        if (req.hasKey("UDID")) {
            device.setUdid(req.getStr("UDID"));
        }
        if (req.hasKey("OpenUDID")) {
            device.setOpenUdid(req.getStr("OpenUDID"));
        }
        if (req.hasKey("IDFA")) {
            device.setIdfa(req.getStr("IDFA"));
        }
        device.setJsonAll(req.getBizData().toString());
        Device find = new Device();
        find.setMacId(device.getMacId());
        find.setUdid(device.getUdid());
        find.setOpenUdid(device.getOpenUdid());
        find.setIdfa(device.getIdfa());
        find.setBrand(device.getBrand());
        find.setModel(device.getModel());
        find.setOs(device.getOs());
        String updateSql = "update_device_for_updv";
        gm.update(updateSql, find, Device.class);
        return new Response(Response.YES);
    }
}
