package com.ngnsoft.ngp.component.coc.service.impl;

import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.CocClanUser;
import com.ngnsoft.ngp.component.coc.model.CocIosOrder;
import com.ngnsoft.ngp.component.coc.model.CocMessage;
import com.ngnsoft.ngp.component.coc.model.CocProduct;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.coc.service.CocManager;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.service.impl.ComponentManagerImpl;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Message;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.service.AppMessageManager;
import com.ngnsoft.ngp.service.ClanManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.service.impl.UserDataManagerImpl;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

public class CocManagerImpl extends ComponentManagerImpl implements CocManager, ClanManager, AppMessageManager {
	
	private Logger LOGGER = LoggerFactory.getLogger(CocManagerImpl.class);

	@Autowired
	private UserSessionManager usm;
	@Autowired
	@Qualifier("redisImpl")
	private RedisImpl redis;
	@Autowired
	private UserManager userManager;

	@Override
	public Response getMessageByCondition(Request req, App app)
			throws JSONException {
		Long userData = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("userData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getLong("userData") : 0l;
		JSONArray appDataKeys = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("appData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getJSONArray("appData") : new JSONArray();

		Long userId = req.getUserId();
		JSONObject jo = req.getBizData();

		int type = jo.getJSONObject(AppComponent.REQ_GET_ACTION).has("type") ? jo.getJSONObject(AppComponent.REQ_GET_ACTION).getInt("type") : 1;
		Pagination page = new Pagination();
		Long id = null;
		if (jo.getJSONObject(AppComponent.REQ_GET_ACTION).has("msgId")) {
			id = jo.getJSONObject(AppComponent.REQ_GET_ACTION).getLong("msgId");
		}
		page.setItems_per_page(200);
		JSONObject ret = new JSONObject();
		JSONArray ja = new JSONArray();
		if (type == CocMessage.TO_CLAN) {
			CocClanUser cocClanUser = new CocClanUser();
			cocClanUser.setUserId(userId);
			CocClanUser cocClanUsers = findObject(cocClanUser);
			if (cocClanUsers != null) {
				CocClanUser clanUser = new CocClanUser();
				clanUser.setClanId(cocClanUsers.getClanId());
				CocClanUser clanUsers = findObject(clanUser);
				if (clanUsers != null) {
					CocMessage cm = new CocMessage();
					if (id != null) {
						cm.setId(id);
					}
					cm.setType(type);
					cm.setTo(cocClanUsers.getClanId());
					List<CocMessage> messages = findByNamedQuery("find_message_by_userId", cm, page, CocMessage.class);
					for (int i = 0; i < messages.size(); i++) {
						JSONObject messageObj = messages.get(i).toJSONObject();
						Long uId = messages.get(i).getFrom();
						if (messages.get(i).getFrom() < 100000) {
							JSONObject jsonObj = new JSONObject(messages.get(i).getAttach());
							uId = jsonObj.getLong("userId");
						}
						if (userData > 0) {
							JSONArray jsonArray = userManager.getUserData(String.valueOf(uId));
							if (jsonArray.length() > 0) {
								messageObj.put("userData", jsonArray.getJSONObject(0));
							}
						}
						if (appDataKeys.length() > 0) {
							userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), uId, messageObj, app.getBaseId(), new DragonData());
						}
						clanUser = new CocClanUser();
						clanUser.setUserId(messages.get(i).getFrom());
						CocClanUser cus = findObject(clanUser);
						if (cus != null) {
							JSONObject memberData = new JSONObject();
							memberData.put("role", cus.getRole());
							messageObj.put("cmData", memberData);
						}
						ja.put(messageObj);
					}
				}
			}
			return new Response(Response.YES, ja);
		} else if (type == CocMessage.TO_GLOBAL) {
			CocMessage cocMessage = new CocMessage();
			cocMessage.setType(type);
			cocMessage.setTo(0L);
			if (id != null) {
				cocMessage.setId(id);
			}
			List<CocMessage> cocMessages = findByNamedQuery("find_dragonMessage_by_type", cocMessage, CocMessage.class);
			for (int i = 0; i < cocMessages.size(); i++) {
				JSONObject messageObj = cocMessages.get(i).toJSONObject();
				messageObj.put("createTime", cocMessages.get(i).getCreateTime().getTime());
				if (userData > 0) {
					JSONArray jsonArray = userManager.getUserData(String.valueOf(cocMessages.get(i).getFrom()));
					if (jsonArray.length() > 0) {
						messageObj.put("userData", jsonArray.getJSONObject(0));
					}
				}
				if (appDataKeys.length() > 0) {
					userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), cocMessages.get(i).getFrom(), messageObj, app.getBaseId(), new UserData());
				}
				CocClanUser clanUser = new CocClanUser();
				clanUser.setUserId(cocMessages.get(i).getFrom());
				CocClanUser cus = findObject(clanUser);
				if (cus != null) {
					JSONObject memberData = new JSONObject();
					memberData.put("clanId", cus.getClanId());
					messageObj.put("cmData", memberData);
				}
				ja.put(messageObj);
			}
			ret.put("data", ja);
			return new Response(Response.YES, ret);
		} else if (type == CocMessage.TO_CLAN_MEMBER) {
			CocMessage message = new CocMessage();
			message.setTo(req.getUserId());
			message.setType(type);
			List<CocMessage> messages = findMulti(message, page);
			Long pageCount = this.countTotalNum(message);
			for (CocMessage cocMessage : messages) {
				JSONObject messageObj = cocMessage.toJSONObject();
				if (userData > 0) {
					JSONArray jsonArray = userManager.getUserData(String.valueOf(cocMessage.getFrom()));
					if (jsonArray.length() > 0) {
						messageObj.put("userData", jsonArray.getJSONObject(0));
					}
				}
				if (appDataKeys.length() > 0) {
					userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), cocMessage.getFrom(), messageObj, app.getBaseId(), new UserData());
				}
				ja.put(messageObj);
			}
			ret.put("count", pageCount);
			ret.put("data", ja);
			return new Response(Response.YES, ret);
		} else {
			return new Response(Response.NO, "ERR_type");
		}
	}

	@Override
	public Response rdms(Request req) throws JSONException {
		JSONObject readParam = req.getBizData();
		if (!readParam.getJSONObject(AppComponent.REQ_READ_ACTION).has("msgid")) {
			return new Response(Response.NO, "NO_msgid");
		}
		String[] msgids = readParam.getJSONObject(AppComponent.REQ_READ_ACTION).getString("msgid").split(",");
		for (String msgid : msgids) {
			CocMessage message = get(Long.parseLong(msgid), CocMessage.class);
			if (message.getAttachStatus() == 1) {
				if (readParam.getJSONObject(AppComponent.REQ_READ_ACTION).has("attach")) {
					message.setAttachStatus(1);
				}
			}
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
	public Response mtuf(Request req) throws JSONException {
		JSONObject sendParam = req.getBizData();
		Long to = sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getLong("to");

		Long type = sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).has("type") ? sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getLong("type") : 0L;
		CocMessage cocMessage = new CocMessage();
		if (type == CocMessage.TO_CLAN) {
			CocClanUser cocClanUser = new CocClanUser();
			cocClanUser.setUserId(req.getUserId());
			cocClanUser = findObject(cocClanUser);
			if (cocClanUser != null) {
				cocMessage.setTo(cocClanUser.getClanId());
			} else {
				return new Response(Response.NO, "NO_clan");
			}
		} else if (type == CocMessage.TO_GLOBAL) {
			cocMessage.setTo(0L);
		} else if (type == CocMessage.TO_CLAN_MEMBER) {
			cocMessage.setTo(to);
		} else {
			return new Response(Response.NO, "ERR_type");
		}
		cocMessage.setType(type.intValue());
		cocMessage.setFrom(req.getUserId());
		if (sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).has("title")) {
			cocMessage.setTitle(sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).getString("title"));
		}
		if (sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).has("body")) {
			cocMessage.setBody(sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).getString("body"));
		}
		if (sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).has("attach")) {
			cocMessage.setAttach(sendParam.getJSONObject(CocComponent.REQ_SEND_ACTION).getString("attach"));
		}
		save(cocMessage);
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
	public Response exitClan(Request req, App app) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getClanByCondition(Request req, App app) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response createClan(Request req, App app) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response joinClan(Request req, App app) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response conferClanUser(Request req, App app) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response modifyClan(Request req, App app) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response getClans(Request req, App app, Pagination page) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Response dlms(Request req) throws JSONException {
		JSONObject delParam = req.getBizData();
		if (!delParam.has("msgid")) {
			return new Response(Response.NO, "NO_msgid");
		}
		String[] msgids = delParam.getString("msgid").split(",");
		for (String msgid : msgids) {
			Long messageId = Long.parseLong(msgid);
			remove(get(messageId, CocMessage.class));
		}
		return new Response(Response.YES);
	}

    @Override
    @Transactional(value = "cocTransactionManager")
    public boolean apsl(CocIosOrder order) {
    	boolean flag = false;
    	save(order);
    	CocProduct product = userManager.get(order.getProductId(), CocProduct.class);
    	if(product != null) {
    		UserData userData = userManager.get(order.getUserId(), UserData.class);
    		if(userData != null) {
    			JSONObject userJson = userData.toJSONObject();
    			if(userJson.has(UserDataManagerImpl.USER_INFO_KEY)) {
    				Long gemCount = userJson.getJSONObject(UserDataManagerImpl.USER_INFO_KEY).has(UserDataManagerImpl.USER_GEM_KEY) ? userJson.getJSONObject(UserDataManagerImpl.USER_INFO_KEY).getLong(UserDataManagerImpl.USER_GEM_KEY) : 0l;
    				gemCount += product.getQuantity();
    				userJson.getJSONObject(UserDataManagerImpl.USER_INFO_KEY).put(UserDataManagerImpl.USER_INFO_KEY, gemCount);
    				userData.setActionTime(new Date());
    				userData.setJsonData(userJson.toString());
    				update(userData);
    				flag = true;
    			}
    		}
    	}
    	return flag;
    }
}
