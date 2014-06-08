package com.ngnsoft.ngp.service.impl;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.model.*;
import com.ngnsoft.ngp.model.show.MessageShow;
import com.ngnsoft.ngp.service.MessageManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import java.util.List;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 *
 * @author fcy
 */
@Service
public class MessageManagerImpl extends GenericManagerImpl implements MessageManager {
	
	private static Logger LOGGER = Logger.getLogger(MessageManagerImpl.class);

    @Autowired
    private UserSessionManager usm;
    @Autowired
    @Qualifier("redisImpl")
    private RedisImpl redis;

    @Override
    public Response getMessageByPage(Request req, int curPage, int perSize) throws JSONException {
        JSONObject jo = new JSONObject();
        Pagination page = new Pagination();
        page.setCurrent_page(curPage);
        page.setItems_per_page(perSize);

        Message message = new Message();
        message.setTo(req.getUserId());

        List<Message> messages = this.findMulti(message, page);
        Long pageCount = this.countTotalNum(message);
        JSONArray ja = new JSONArray();
        for (Message mes : messages) {
            MessageShow messageShow = new MessageShow();
            BeanUtils.copyProperties(mes, messageShow);
            User fromUser = (User) get(mes.getFrom(), User.class);
            UserProfile fromUp = (UserProfile) get(mes.getFrom(), UserProfile.class);
            if (fromUser != null) {
            	if (fromUser.getNick() != null) {
            		messageShow.setFromNick(fromUser.getNick());
            	}
            }
            if (fromUp != null) {
                String avatar = null;
                if (fromUp.getAvatar() != null) {
                	avatar = fromUp.getAvatar();
                    if (!avatar.startsWith("http://")) {
                        avatar = Engine.getInstance().getConfig().getFileServer() + avatar;
                    }
                }
                messageShow.setFromAvatar(avatar);
            }
            ja.put(messageShow.toJSONObject());
        }
        jo.put("count", pageCount);
        jo.put("data", ja);
        return new Response(Response.YES, jo);
    }

    @Override
    public Response mtuf(Request req, JSONObject sendParam) throws JSONException {
        Long to = sendParam.getLong("to");
        Message msg = new Message();
        msg.setFrom(req.getUserId());
        msg.setTo(to);
        if (sendParam.has("title")) {
            msg.setTitle(sendParam.getString("title"));
        }
        if (sendParam.has("body")) {
            msg.setBody(sendParam.getString("body"));
        }
        if (sendParam.has("attach")) {
            msg.setAttach(sendParam.getString("attach"));
        }
        /*
         * Map<String, Object> paramsMap = new HashMap<String, Object>();
         * paramsMap.put("ua", msg.getFrom()); paramsMap.put("ub", msg.getTo());
         * List<UserFriend> userFriends =
         * findByNamedQuery("find_user_friend_by_ua_and_ub", paramsMap,
         * UserFriend.class); if (userFriends == null || userFriends.isEmpty())
         * { return new Response(Response.NO, "NOT_FRIEND"); }
         *
         */
        save(msg);
        UserSession us = usm.matchValidSession(to);
        if (us != null) {
        	try {
        		redis.incr(Message.redisUnreadNumKey(to));
			} catch (Exception e) {
				LOGGER.error("Redis operate ERROR:", e);
			}
        }
        return new Response(Response.YES);
    }

    @Override
    public Response rdms(Request req, JSONObject readParam) throws JSONException {
        if (!readParam.has("msgid")) {
            return new Response(Response.NO, "NO_msgid");
        }
        String[] msgids = readParam.getString("msgid").split(",");
        for (String msgid : msgids) {
            Message message = (Message) this.get(Long.parseLong(msgid), Message.class);
            message.setStatus(1);
            this.update(message);
        }
        try {
        	redis.incr(Message.redisUnreadNumKey(req.getUserId()), msgids.length * -1);
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
        return new Response(Response.YES);
    }

    @Override
    public Response dlms(Request req, JSONObject delParam) throws JSONException {
        if (!delParam.has("msgid")) {
            return new Response(Response.NO, "NO_msgid");
        }
        String[] msgids = delParam.getString("msgid").split(",");
        for (String msgid : msgids) {
            Long messageId = Long.parseLong(msgid);
            this.remove(get(messageId, Message.class));
        }
        return new Response(Response.YES);
    }
}
