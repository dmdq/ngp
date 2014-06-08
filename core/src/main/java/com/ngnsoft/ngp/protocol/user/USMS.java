package com.ngnsoft.ngp.protocol.user;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.MessageManager;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class USMS extends SyncExecutor {

    private static final String CMD_SEND = "send";
    private static final String CMD_GET = "get";
    private static final String CMD_READ = "read";
    private static final String CMD_DELETE = "del";
    @Autowired
    private MessageManager mm;

    @Override
    public Response exec(Request req, App app) throws Exception {
        if (req.hasKey(CMD_SEND)) {
            JSONObject getParam = req.getJSONObject(CMD_SEND);
            if (!getParam.has("to")) {
                return new Response(Response.NO, "NO_to");
            }
            return mm.mtuf(req, getParam);
        } else if (req.hasKey(CMD_GET)) {
            JSONObject getParam = req.getJSONObject(CMD_GET);
            if (!getParam.has("curPage")) {
                return new Response(Response.NO, "NO_curPage");
            }
            if (!getParam.has("perSize")) {
                return new Response(Response.NO, "NO_perSize");
            }
            return mm.getMessageByPage(req, getParam.getInt("curPage"), getParam.getInt("perSize"));
        } else if (req.hasKey(CMD_READ)) {
            return mm.rdms(req, req.getJSONObject(CMD_READ));
        } else if (req.hasKey(CMD_DELETE)) {
            return mm.dlms(req, req.getJSONObject(CMD_DELETE));
        } else {
            return new Response(Response.NO, "ERR_CMD");
        }
    }
}
