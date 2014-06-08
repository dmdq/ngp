package com.ngnsoft.ngp.protocol.user;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.SyncExecutor;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.service.UserManager;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author fcy
 */
@Controller
public class USFS extends SyncExecutor {

    public static final String ADD_FRIENDS_TYPE = "add";
    public static final String LIST_FRIENDS_TYPE = "get";
    public static final String DEL_FRIENDS_TYPE = "del";
    public static final String CONFIRM_FRIENDS_TYPE = "confirm";
    @Autowired
    private UserManager userManager;

    @Override
    public Response exec(Request req, App app) throws Exception {
        if (req.hasKey(ADD_FRIENDS_TYPE)) {
            userManager.addFriends(req.getUserId(), app, req.getJSONArray(ADD_FRIENDS_TYPE));
            return new Response(Response.YES);
        } else if (req.hasKey(LIST_FRIENDS_TYPE)) {
            Long userId = req.getUserId();
            String sns = req.getJSONObject(LIST_FRIENDS_TYPE).has("sns") ? req.getJSONObject(LIST_FRIENDS_TYPE).getString("sns") : "id";
            User user = userManager.get(userId, User.class);
            if (user == null) {
                return new Response(Response.NO, "NO_USER");
            } else {
                Object obj = getPage(LIST_FRIENDS_TYPE, req);
                Pagination page = null;
                if (obj instanceof Pagination) {
                    page = (Pagination) obj;
                } else {
                    return (Response) obj;
                }
                JSONObject frs = userManager.listFriends(req, sns, page);
                return new Response(Response.YES, frs);
            }
        } else if (req.hasKey(DEL_FRIENDS_TYPE)) {
            //del friends
            return userManager.delFriends(req.getUserId(), app, req.getJSONArray(DEL_FRIENDS_TYPE));
        } else {
            return new Response(Response.NO, "ERR_CMD");
        }
    }

    private Object getPage(String requestType, Request req) throws JSONException {
        Pagination page = new Pagination();
        if (req.getJSONObject(requestType).has("curPage")) {
            page.setCurrent_page(req.getJSONObject(requestType).getInt("curPage"));
        } else {
            return new Response(Response.NO, "NO_curPage");
        }
        if (req.getJSONObject(requestType).has("perSize")) {
            page.setItems_per_page(req.getJSONObject(requestType).getInt("perSize"));
        } else {
            return new Response(Response.NO, "NO_perSize");
        }
        return page;
    }
}
