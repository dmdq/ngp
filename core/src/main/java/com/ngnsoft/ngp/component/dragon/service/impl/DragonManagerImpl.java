package com.ngnsoft.ngp.component.dragon.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import sun.misc.BASE64Decoder;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonClan;
import com.ngnsoft.ngp.component.dragon.model.DragonClanUser;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.dragon.model.DragonMessage;
import com.ngnsoft.ngp.component.dragon.service.DragonManager;
import com.ngnsoft.ngp.component.service.impl.ComponentManagerImpl;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.ClanUser;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Message;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.service.impl.DragonDataManagerImpl;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

public class DragonManagerImpl extends ComponentManagerImpl implements DragonManager {

	public static final long CREATE_CLAN_COIN_LIMIT = 5;

	public static final String CREATE_DEDUCT_KEY = "deduct";
	
	private static Logger LOGGER = Logger.getLogger(DragonManagerImpl.class);

	@Autowired
	private UserSessionManager usm;
	@Autowired
	@Qualifier("redisImpl")
	private RedisImpl redis;
	@Autowired
	private UserManager userManager;

	@Override
	public Response getClanByCondition(Request req, App app) {
		JSONObject jo = req.getBizData();
		//GET user information if userData is 1
		Long userData = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("userData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getLong("userData") : 0l;
		JSONArray appDataKeys = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("appData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getJSONArray("appData") : new JSONArray();
		Response res = null;
		if (!jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has(GET_CLAN_ID_KEY)) {
			res = new Response(Response.NO, "NO_clanId");
		} else {
			int cmData = jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has(GET_CLAN_MEM_KEY) ? jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getInt(GET_CLAN_MEM_KEY) : 0;
			int clanData = jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has(GET_CLAN_DATA_KEY) ? jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getInt(GET_CLAN_DATA_KEY) : 0;
			Long clanId = jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getLong(GET_CLAN_ID_KEY);
			DragonClan clan = get(clanId, DragonClan.class);
			if (clan != null) {
				JSONObject resultObj = new JSONObject();
				JSONArray userArray = new JSONArray();
				DragonClanUser searchObj = new DragonClanUser();
				searchObj.setClanId(clanId);
				List<DragonClanUser> clanUsers = findMulti(searchObj);
				for (DragonClanUser clanUser : clanUsers) {
					JSONObject userObj = new JSONObject();
					if (userData > 0) {
						JSONArray jsonArray = userManager.getUserData(String.valueOf(clanUser.getUserId()));
						if (jsonArray.length() > 0) {
							userObj.put("userData", jsonArray.getJSONObject(0));
						}
					}
					if (appDataKeys.length() > 0) {
						userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), clanUser.getUserId(), userObj, app.getBaseId(), new DragonData());
					}
					if (cmData > 0) {
						JSONObject memberData = new JSONObject();
						memberData.put("role", clanUser.getRole());
						userObj.put("cmData", memberData);
					}
					if(userObj.length() > 0) {
						userArray.put(userObj);
					}
					
					JSONObject clanObject = clan.toJSONObject();
					DragonData dragonData = get(clan.getCreator(), DragonData.class);
					//TODO replace app user's name with app nick
					if(dragonData != null && dragonData.getNick() != null) {
						clanObject.put("creatorName", dragonData.getNick());
					} else {
						User creator = get(clan.getCreator(), User.class);
						clanObject.put("creatorName", creator.getNick());
					}
					if (clanData > 0) {
						resultObj.put("clan", clanObject);
					}
				}
				resultObj.put("members", userArray);
				res = new Response(Response.YES, resultObj);
			} else {
				res = new Response(Response.NO, "NO_CLAN");
			}
		}

		return res;
	}
	
	@Override
	public Response createClan(Request req, App app) throws Exception {
		JSONObject jo = req.getBizData();
		Response res = null;
		DragonData searchObj = new DragonData();
		searchObj.setUserId(req.getUserId());
		searchObj.setBaid(app.getBaseId());
		List<DragonData> userAppDatas = findMulti(searchObj);
		DragonData userData = userAppDatas == null || userAppDatas.size() == 0 ? null : userAppDatas.get(0);

		DragonClanUser searchClanUser = new DragonClanUser();
		searchClanUser.setUserId(req.getUserId());
		List<DragonClanUser> clanUsers = findMulti(searchClanUser);
		if(clanUsers.size() > 0) {
			res = new Response(Response.NO, "DUPL_CLAN");
		} else {
			if (userData == null) {
				res = new Response(Response.NO, "NO_DATA");
			} else {
				Integer deduct = 0;
				try {
					deduct = jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getInt(CREATE_DEDUCT_KEY);
				} catch (Exception e) {
					//get deduct ERROR
					return new Response(Response.NO, "NO_deduct");
				}
				if (userData.getCoin() >= CREATE_CLAN_COIN_LIMIT && userData.getCoin() >= CREATE_CLAN_COIN_LIMIT) {
					String avatarType = jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getString("iconType");
					String avatarCode = jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getString("icon");
					DragonClan clan = new DragonClan();
					if ("http".equals(avatarType)) {
						clan.setIcon(avatarCode);
					} else if ("file".equals(avatarType)) {
						String uuid = Integer.toHexString(avatarCode.hashCode());
						String avatar = FileStorage.genUrn(FileStorage.SHORT_NAME_AVATAR) + uuid;
						clan.setIcon(avatar);
					} else if ("local".equals(avatarType)) {
						clan.setIcon(FileStorage.LOCAL_PATH + avatarCode);
					} else if (avatarType.startsWith("base64")) {
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] byteData = decoder.decodeBuffer(avatarCode);
						for (int i = 0; i < byteData.length; i++) {
							if (byteData[i] < 0) {
								byteData[i] += 256;
							}
						}
						FileStorage fs = new FileStorage("avatar", null);
						fs.setData(byteData);
						fs.setName(avatarType);
						save(fs);
						clan.setIcon(fs.getUrn());
						clan.setUpdateTime(new Date());
					}
					if (jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).has("notice")) {
						clan.setNotice(jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getString("notice"));
					}
					if (jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).has("limit")) {
						clan.setLimit(jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getJSONObject("limit").toString());
					}
					clan.setName(jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getString("name"));
					clan.setType(jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getInt("type"));
					clan.setLevel(1);
					if (jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).has("maxNum")) {
						clan.setMaxMember(jo.getJSONObject(AppComponent.ADD_CLAN_ACTION).getInt("maxNum"));
					} else {
						clan.setMaxMember(20);
					}
					clan.setMemberNum(1);
					clan.setCreator(req.getUserId());
					save(clan);

					//create dragon_clan_user record
					DragonClanUser clanUser = new DragonClanUser();
					clanUser.setClanId(clan.getId());
					clanUser.setRole(DragonClanUser.ROLE_PRESIDENT);
					clanUser.setUserId(req.getUserId());
					save(clanUser);

					//deduct user's coin
					restUserCoin(userData, deduct);
					update(userData);

					JSONObject result = new JSONObject();
					result.put("id", clan.getId());

					res = new Response(Response.YES, result);
				} else {
					res = new Response(Response.NO, "COIN_LOCK");
				}
			}
		}

		return res;
	}

	@Override
	public Response getClans(Request req, App app, Pagination page) {
		JSONObject resultObj = new JSONObject();
		JSONArray array = new JSONArray();
		Map paramMap = new HashMap();
		paramMap.put("status", DragonClan.STATUS_NORMAL);
		Long count = countByNameQuery("count_clans_order_by_level", paramMap, DragonClan.class);
		resultObj.put("count", count);
		if (count > 0) {
			List<DragonClan> clans = findByNamedQuery("find_clans_order_by_level", paramMap, page, DragonClan.class);
			for (DragonClan clan : clans) {
				JSONObject clanObj = clan.toJSONObject();
				DragonClanUser searchUser = new DragonClanUser();
				searchUser.setClanId(clan.getId());
				array.put(clanObj);
			}
		}
		resultObj.put("data", array);
		return new Response(Response.YES, resultObj);
	}

	@Override
	public Response modifyClan(Request req, App app) throws Exception {
		JSONObject jo = req.getBizData();
		Response res = null;
		DragonData searchObj = new DragonData();
		searchObj.setUserId(req.getUserId());
		searchObj.setBaid(app.getBaseId());
		List<DragonData> userAppDatas = findMulti(searchObj);
		DragonData userData = userAppDatas == null || userAppDatas.size() == 0 ? null : userAppDatas.get(0);
		if (userData == null) {
			res = new Response(Response.NO, "NO_DATA");
		} else {
			DragonClanUser searchClanUser = new DragonClanUser();
			searchClanUser.setUserId(req.getUserId());
			DragonClanUser clanUser = findObject(searchClanUser);

			if (clanUser != null && clanUser.getRole() == ClanUser.ROLE_PRESIDENT) {
				DragonClan clan = get(clanUser.getClanId(), DragonClan.class);
				boolean levelUpFlag = false;
				int deduct = 0;
				int oldLevel = clan.getLevel();

				if(jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("level")) {
					int level = jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getInt("level");
					if(level > clan.getLevel()) {
						if(!jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("deduct")) {
							return new Response(Response.NO, "NO_deduct");
						}
						if(!jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("maxNum")) {
							return new Response(Response.NO, "NO_maxNum");
						}
						int maxNum = jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getInt("maxNum");
						deduct = jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getInt("deduct");
						if(userData.getCoin() < deduct) {
							return new Response(Response.NO, "COIN_LOCK");
						}
						//clan level up
						clan.setLevel(level);
						clan.setMaxMember(maxNum);
						levelUpFlag = true;
					}
				}

				if (jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("iconType")
						&& jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("icon")) {
					String avatarType = jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getString("iconType");
					String avatarCode = jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getString("icon");
					if ("http".equals(avatarType)) {
						clan.setIcon(avatarCode);
					} else if ("file".equals(avatarType)) {
						String uuid = Integer.toHexString(avatarCode.hashCode());
						String avatar = FileStorage.genUrn(FileStorage.SHORT_NAME_AVATAR) + uuid;
						clan.setIcon(avatar);
					} else if ("local".equals(avatarType)) {
						clan.setIcon(FileStorage.LOCAL_PATH + avatarCode);
					} else if (avatarType.startsWith("base64")) {
						BASE64Decoder decoder = new BASE64Decoder();
						byte[] byteData = decoder.decodeBuffer(avatarCode);
						for (int i = 0; i < byteData.length; i++) {
							if (byteData[i] < 0) {
								byteData[i] += 256;
							}
						}
						String urn = clan.getIcon();
						String id = urn.substring(urn.lastIndexOf("/") + 1);

						FileStorage fs = new FileStorage("avatar", id);

						FileStorage modifyFs = findObject(fs);
						if (modifyFs != null) {
							modifyFs.setData(byteData);
							modifyFs.setName(avatarType);
							update(fs);

							clan.setIcon(modifyFs.getUrn());
							clan.setUpdateTime(new Date());
						}
					}
				}
				if (jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("notice")) {
					clan.setNotice(jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getString("notice"));
				}
				if (jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("limit")) {
					clan.setLimit(jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getJSONObject("limit").toString());
				}
				if (jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("name")
						&& !jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).isNull("name")) {
					clan.setName(jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getString("name"));
				}
				if (jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).has("type")
						&& !jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).isNull("type")) {
					clan.setType(jo.getJSONObject(AppComponent.MODIFY_CLAN_ACTION).getInt("type"));
				}
				update(clan);

				if(levelUpFlag) {
					//deduct user's coin
					restUserCoin(userData, deduct);
					update(userData);

					//send message
					DragonMessage dragonMessage = new DragonMessage();
					dragonMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
					dragonMessage.setTo(clan.getId());
					JSONObject attachObj = new JSONObject();
					attachObj.put("type", DragonMessage.ATTACH_CLAN_LEVEL_TYPE);
					attachObj.put("from", oldLevel);
					attachObj.put("to", clan.getLevel());
					dragonMessage.setActive(DragonMessage.ATTACH_CLAN_LEVEL_TYPE);
					dragonMessage.setAttach(attachObj.toString());
					dragonMessage.setType(DragonMessage.TO_CLAN);
					save(dragonMessage);
				}

				res = new Response(Response.YES);
			} else {
				res = new Response(Response.NO, "PERMISS_DENIED");
			}
		}
		return res;
	}

	@Override
	public Response joinClan(Request req, App app) {
		JSONObject jo = req.getBizData();
		Response res = null;

		if (!jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).has("clanId")) {
			res = new Response(Response.NO, "NO_clanId");
		} else {
			Long userId = jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).has("userId") ? jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).getLong("userId") : req.getUserId();
			int paramType = jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).has("type") ? jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).getInt("type") : 1;
			boolean isAgree = paramType == 1 ? true : false;
			int type = userId.intValue() == req.getUserId().intValue() ? 0 : 1;
			String message = jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).has("message") ? jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).getString("message") : null;
			Long clanId = jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).getLong("clanId");

			DragonClanUser searchClanUser = new DragonClanUser();
			searchClanUser.setUserId(userId);

			List<DragonClanUser> clanUsers = findMulti(searchClanUser);
			if (clanUsers.size() == 0) {
				DragonClan clan = get(clanId, DragonClan.class);
				if (clan == null) {
					res = new Response(Response.NO, "NO_CLAN");
				} else {
					if (type == 0) {
						//user apply for joining in clan
						//find this message whether is exist
						DragonMessage queryObj = new DragonMessage();
						queryObj.setActive(DragonMessage.ATTACH_APPLY_JOIN_TYPE);
						queryObj.setAttachStatus(DragonMessage.ATTACH_STATUS_UNDEALED);
						queryObj.setType(DragonMessage.TO_CLAN);
						queryObj.setFrom(userId);
						queryObj.setTo(clanId);
						Long count = countTotalNum(queryObj);
						
						if(count == 0) {
							DragonMessage dragonMessage = new DragonMessage();
							dragonMessage.setFrom(userId);
							dragonMessage.setTo(clanId);
							dragonMessage.setBody(message);
							JSONObject attachObj = new JSONObject();
							attachObj.put("type", DragonMessage.ATTACH_APPLY_JOIN_TYPE);
							attachObj.put("userId", req.getUserId());
							dragonMessage.setAttach(attachObj.toString());
							dragonMessage.setType(DragonMessage.TO_CLAN);
							dragonMessage.setActive(DragonMessage.ATTACH_APPLY_JOIN_TYPE);
							save(dragonMessage);
							
							res = new Response(Response.YES);
						} else {
							res = new Response(Response.NO, "HAD_APPLY");
						}
						
					} else {
						if (jo.getJSONObject(AppComponent.JOIN_CLAN_ACTION).has("userId")) {
							//Clan confirm the apply for user
							DragonClanUser joinClanUser = new DragonClanUser();
							joinClanUser.setUserId(req.getUserId());
							joinClanUser.setClanId(clanId);

							DragonClanUser clanUser = findObject(joinClanUser);
							if (clanUser.getRole() > 0) {
								//update unread attach status applying for joining clan
								Map map = new HashMap();
								map.put("active", DragonMessage.ATTACH_APPLY_JOIN_TYPE);
								map.put("type", DragonMessage.TO_CLAN);
								map.put("userId", userId);
								map.put("to", clanId);
								update("updateUnReadAttachForApply", map, DragonMessage.class);
								if(isAgree) {
									//send clan message that the user had joined in clan successfully
									DragonMessage dragonMessage = new DragonMessage();
									dragonMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
									dragonMessage.setTo(clanId);
									dragonMessage.setBody(message);
									JSONObject attachObj = new JSONObject();
									attachObj.put("operator", req.getUserId());
									DragonData dragonData = get(req.getUserId(), DragonData.class);
									attachObj.put("operatorName", dragonData.getNick());
									attachObj.put("type", DragonMessage.ATTACH_HAD_JOIN_TYPE);
									attachObj.put("userId", userId);
									dragonMessage.setAttach(attachObj.toString());
									dragonMessage.setActive(DragonMessage.ATTACH_HAD_JOIN_TYPE);
									dragonMessage.setType(DragonMessage.TO_CLAN);
									save(dragonMessage);

									DragonClanUser newClanUser = new DragonClanUser();
									newClanUser.setClanId(clanId);
									newClanUser.setUserId(userId);
									save(newClanUser);
									
									refreshClanMemNum(clanId);
								} else {
									//send refuse message that the user apply for joining in clan
									DragonMessage dragonMessage = new DragonMessage();
									dragonMessage.setFrom(req.getUserId());
									dragonMessage.setTo(userId);
									JSONObject attachObj = new JSONObject();
									attachObj.put("operator", req.getUserId());
									DragonData dragonData = get(req.getUserId(), DragonData.class);
									attachObj.put("operatorName", dragonData.getNick());
									attachObj.put("type", DragonMessage.ATTACH_REFUSE_JOIN_TYPE);
									attachObj.put("userId", userId);
									
									dragonMessage.setActive(DragonMessage.ATTACH_REFUSE_JOIN_TYPE);
									dragonMessage.setType(DragonMessage.TO_CLAN_MEMBER);
									save(dragonMessage);
								}
								res = new Response(Response.YES);
							} else {
								//the permission of user denied
								res = new Response(Response.NO, "PERMISS_DENIED");
							}
						} else {
							res = new Response(Response.NO, "NO_userId");
						}
					}
				}
			} else {
				res = new Response(Response.NO, "HAD_JOIN");
			}
		}

		return res;
	}
	
	private void refreshClanMemNum(Long clanId) {
		update("refreshClanMemNum", clanId, DragonClan.class);
	}

	@Override
	public Response exitClan(Request req, App app) {
		JSONObject jo = req.getBizData();
		Response res = null;

		Long userId = jo.getJSONObject(AppComponent.EXIT_CLAN_ACTION).has("userId") ? jo.getJSONObject(AppComponent.EXIT_CLAN_ACTION).getLong("userId") : req.getUserId();
		int type = userId.intValue() == req.getUserId().intValue() ? 0 : 1;
		DragonClanUser searchClanUser = new DragonClanUser();
		searchClanUser.setUserId(userId);
		DragonClanUser clanUser = findObject(searchClanUser);
		if (type == 0) {
			//user exit clan
			if (clanUser != null) {
				if(clanUser.getRole() < 2) {
					//send message
					DragonMessage dragonMessage = new DragonMessage();
					dragonMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
					dragonMessage.setTo(clanUser.getClanId());
					JSONObject attachObj = new JSONObject();
					attachObj.put("type", DragonMessage.ATTACH_EXIT_TYPE);
					attachObj.put("userId", userId);
					dragonMessage.setActive(DragonMessage.ATTACH_EXIT_TYPE);
					dragonMessage.setAttach(attachObj.toString());
					dragonMessage.setType(DragonMessage.TO_CLAN);
					save(dragonMessage);

					//exit clan
					remove(clanUser.getId(), DragonClanUser.class);
					
					refreshClanMemNum(clanUser.getClanId());
				} else {
					//dissolve clan
					DragonClanUser queryClanUser = new DragonClanUser();
					queryClanUser.setClanId(clanUser.getClanId());
					remove(queryClanUser);
					
					DragonClan clan = get(clanUser.getClanId(), DragonClan.class);
					clan.setStatus(DragonClan.STATUS_EXPIRED);
					update(clan);
					
					refreshClanMemNum(clanUser.getClanId());
				}
				res = new Response(Response.YES);
			} else {
				res = new Response(Response.NO, "NO_CLAN");
			}
		} else {
			DragonClanUser queryClanUser = new DragonClanUser();
			queryClanUser.setUserId(req.getUserId());
			DragonClanUser leader = findObject(queryClanUser);
			if(leader.getRole() > 0) {
				if(clanUser != null) {
					//kick user from clan
					DragonMessage dragonMessage = new DragonMessage();
					dragonMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
					dragonMessage.setTo(clanUser.getClanId());
					JSONObject attachObj = new JSONObject();
					attachObj.put("operator", req.getUserId());
					DragonData dragonData = get(req.getUserId(), DragonData.class);
					attachObj.put("operatorName", dragonData.getNick());
					attachObj.put("type", DragonMessage.ATTACH_CICKED_TYPE);
					attachObj.put("userId", userId);
					dragonMessage.setActive(DragonMessage.ATTACH_CICKED_TYPE);
					dragonMessage.setAttach(attachObj.toString());
					dragonMessage.setType(DragonMessage.TO_CLAN);
					save(dragonMessage);

					//exit clan
					remove(clanUser.getId(), DragonClanUser.class);
					
					refreshClanMemNum(clanUser.getClanId());
				}
				res = new Response(Response.YES);
			} else {
				//the permission of user denied
				res = new Response(Response.NO, "PERMISS_DENIED");
			}
		}

		return res;
	}

	@Override
	public Response conferClanUser(Request req, App app) {
		JSONObject jo = req.getBizData();
		Response res = null;

		if (!jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).has("id")) {
			res = new Response(Response.NO, "NO_id");
		} else if (!jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).has("targetRole")) {
			res = new Response(Response.NO, "NO_targetRole");
		} else {
			Long userId = jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).getLong("id");
			int targetRole = jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).getInt("targetRole");
			int ownRole = jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).has("ownRole") ? jo.getJSONObject(AppComponent.CONFER_CLAN_ACTION).getInt("ownRole") : -1;

			DragonClanUser searchClanGrantor = new DragonClanUser();
			searchClanGrantor.setUserId(req.getUserId());
			DragonClanUser clanGrantor = findObject(searchClanGrantor);

			DragonClanUser searchAuthorizedUser = new DragonClanUser();
			searchAuthorizedUser.setUserId(userId);
			DragonClanUser clanAuthorizedUserUser = findObject(searchAuthorizedUser);
			if (clanGrantor != null && clanAuthorizedUserUser != null
					&& clanAuthorizedUserUser.getClanId().intValue() == clanGrantor.getClanId().intValue()) {
				if (targetRole <= 1) {
					if (clanGrantor.getRole() >= 1) {
						//modify the role for authorizedUser
						clanAuthorizedUserUser.setRole(targetRole);
						update(clanAuthorizedUserUser);

						//send message
						DragonMessage anthrizedMessage = new DragonMessage();
						anthrizedMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
						anthrizedMessage.setTo(clanAuthorizedUserUser.getClanId());
						JSONObject attachObj = new JSONObject();
						attachObj.put("type", DragonMessage.ATTACH_ROLE_TYPE);
						attachObj.put("role", clanAuthorizedUserUser.getRole());
						attachObj.put("userId", clanAuthorizedUserUser.getUserId());
						anthrizedMessage.setAttach(attachObj.toString());
						anthrizedMessage.setType(DragonMessage.TO_CLAN);
						anthrizedMessage.setActive(DragonMessage.ATTACH_ROLE_TYPE);
						save(anthrizedMessage);

						res = new Response(Response.YES);
					} else {
						//the permission of user denied
						res = new Response(Response.NO, "PERMISS_DENIED");
					}
				} else {
					//the grantor is an elder.
					if (clanGrantor.getRole() == 2) {
						if (ownRole != -1 && ownRole <= 1) {
							//replace the grantor who was am elder with authorizedUser.
							clanAuthorizedUserUser.setRole(targetRole);
							update(clanAuthorizedUserUser);

							clanGrantor.setRole(ownRole);
							update(clanGrantor);

							//send message
							DragonMessage anthrizedMessage = new DragonMessage();
							anthrizedMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
							anthrizedMessage.setTo(clanAuthorizedUserUser.getClanId());
							JSONObject attachObj = new JSONObject();
							attachObj.put("type", DragonMessage.ATTACH_ROLE_TYPE);
							attachObj.put("role", clanAuthorizedUserUser.getRole());
							attachObj.put("userId", clanAuthorizedUserUser.getUserId());
							anthrizedMessage.setAttach(attachObj.toString());
							anthrizedMessage.setType(DragonMessage.TO_CLAN);
							anthrizedMessage.setActive(DragonMessage.ATTACH_ROLE_TYPE);
							save(anthrizedMessage);

							//send message
							DragonMessage grantorMessage = new DragonMessage();
							grantorMessage.setFrom(Long.valueOf(DragonMessage.FROM_SYSTEM));
							grantorMessage.setTo(clanAuthorizedUserUser.getClanId());
							JSONObject attachJson = new JSONObject();
							attachJson.put("type", DragonMessage.ATTACH_ROLE_TYPE);
							attachJson.put("role", clanGrantor.getRole());
							attachObj.put("userId", clanGrantor.getUserId());
							grantorMessage.setAttach(attachJson.toString());
							grantorMessage.setType(DragonMessage.TO_CLAN);
							grantorMessage.setActive(DragonMessage.ATTACH_ROLE_TYPE);
							save(grantorMessage);

							res = new Response(Response.YES);
						} else {
							res = new Response(Response.NO, "NO_ownRole");
						}
					} else {
						//the permission of user denied
						res = new Response(Response.NO, "PERMISS_DENIED");
					}
				}
			}
		}
		return res;
	}

	@Override
	public Response getMessageByCondition(Request req, App app)
			throws JSONException {
		Long userData = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("userData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getLong("userData") : 0l;
		Long perSize = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("perSize") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getLong("perSize") : 20l;
		JSONArray appDataKeys = req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).has("appData") ? req.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION).getJSONArray("appData") : new JSONArray();
		JSONObject jo = req.getBizData();
		int type = jo.getJSONObject(AppComponent.REQ_GET_ACTION).has("type") ? jo.getJSONObject(AppComponent.REQ_GET_ACTION).getInt("type") : 1;
		Pagination page = new Pagination();
		Long id = null;
		if (jo.getJSONObject(AppComponent.REQ_GET_ACTION).has("msgId")) {
			id = jo.getJSONObject(AppComponent.REQ_GET_ACTION).getLong("msgId");
		}
		page.setItems_per_page(perSize.intValue());
		JSONArray ja = new JSONArray();
		if (type == DragonMessage.TO_CLAN) {
			DragonClanUser dragonClanUser = new DragonClanUser();
			dragonClanUser.setUserId(req.getUserId());
			DragonClanUser dragonClanUsers = findObject(dragonClanUser);
			if (dragonClanUsers != null) {
				DragonMessage dm = new DragonMessage();
				if (id != null) {
					dm.setId(id);
				}
				dm.setType(type);
				dm.setTo(dragonClanUsers.getClanId());
				List<DragonMessage> messages = findByNamedQuery("find_message_by_userId", dm, page, DragonMessage.class);
				for (int i = 0; i < messages.size(); i++) {
					JSONObject messageObj = messages.get(i).toJSONObject();
					Long uId = messages.get(i).getFrom();
					if (messages.get(i).getFrom() < 100000) {
						JSONObject jsonObj = new JSONObject(messages.get(i).getAttach());
						if (jsonObj.getInt(AppComponent.REQ_TYPE_ACTION) != DragonMessage.ATTACH_CLAN_LEVEL_TYPE) {
							uId = jsonObj.getLong("userId");
						}
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
					DragonClanUser clanUser = new DragonClanUser();
					clanUser.setUserId(messages.get(i).getFrom());
					List<DragonClanUser> cus = findMulti(clanUser);
					if (cus.size() != 0) {
						JSONObject memberData = new JSONObject();
						memberData.put("role", cus.get(0).getRole());
						messageObj.put("cmData", memberData);
					}
					ja.put(messageObj);
				}
			}
			JSONObject ret = new JSONObject();
			ret.put("data", ja);
			return new Response(Response.YES, ret);
		} else if (type == DragonMessage.TO_GLOBAL) {
			DragonMessage dragonMessage = new DragonMessage();
			dragonMessage.setType(type);
			dragonMessage.setTo(0L);
			if (id != null) {
				dragonMessage.setId(id);
			}
			List<DragonMessage> dragonMessages = findByNamedQuery("find_dragonMessage_by_type", dragonMessage, page, DragonMessage.class);
			for (int i = 0; i < dragonMessages.size(); i++) {
				JSONObject messageObj = dragonMessages.get(i).toJSONObject();
				if (userData > 0) {
					JSONArray jsonArray = userManager.getUserData(String.valueOf(dragonMessages.get(i).getFrom()));
					if (jsonArray.length() > 0) {
						messageObj.put("userData", jsonArray.getJSONObject(0));
					}
				}
				if (appDataKeys.length() > 0) {
					userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), dragonMessages.get(i).getFrom(), messageObj, app.getBaseId(), new DragonData());
				}
				DragonClanUser clanUser = new DragonClanUser();
				clanUser.setUserId(dragonMessages.get(i).getFrom());
				List<DragonClanUser> cus = findMulti(clanUser);
				if (cus.size() != 0) {
					JSONObject memberData = new JSONObject();
					memberData.put("clanId", cus.get(0).getClanId());
					messageObj.put("cmData", memberData);
				}
				ja.put(messageObj);
			}
			JSONObject ret = new JSONObject();
			ret.put("data", ja);
			return new Response(Response.YES, ret);
		} else if (type == DragonMessage.TO_CLAN_MEMBER) {
			DragonMessage message = new DragonMessage();
			message.setTo(req.getUserId());
			message.setType(type);
			List<DragonMessage> messages = findMulti(message, page);
			Long pageCount = this.countTotalNum(message);
			for (DragonMessage dragonMessage : messages) {
				JSONObject messageObj = dragonMessage.toJSONObject();
				if (userData > 0) {
					JSONArray jsonArray = userManager.getUserData(String.valueOf(dragonMessage.getFrom()));
					if (jsonArray.length() > 0) {
						messageObj.put("userData", jsonArray.getJSONObject(0));
					}
				}
				if (appDataKeys.length() > 0) {
					userManager.setJsonDataFromAppData(jo.getJSONObject(AppComponent.GET_OR_LIST_CLAN_ACTION), dragonMessage.getFrom(), messageObj, app.getBaseId(), new DragonData());
				}
				ja.put(messageObj);
			}

			JSONObject ret = new JSONObject();
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
			DragonMessage message = get(Long.parseLong(msgid), DragonMessage.class);
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
		
		Long type = sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).has("type") ? sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getLong("type") : 0L;
		DragonMessage dragonMessage = new DragonMessage();
		if (type == DragonMessage.TO_CLAN) {
			DragonClanUser dragonClanUser = new DragonClanUser();
			dragonClanUser.setUserId(req.getUserId());
			dragonClanUser = findObject(dragonClanUser);
			if (dragonClanUser != null) {
				dragonMessage.setTo(dragonClanUser.getClanId());
			} else {
				return new Response(Response.NO, "NO_clan");
			}
		} else if (type == DragonMessage.TO_GLOBAL) {
			dragonMessage.setTo(0L);
		} else if (type == DragonMessage.TO_CLAN_MEMBER) {
			if (!req.getJSONObject(AppComponent.REQ_SEND_ACTION).has("to")) {
                return new Response(Response.NO, "NO_to");
            }
			Long to = sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getLong("to");
			dragonMessage.setTo(to);
			UserSession us = usm.matchValidSession(to);
			if (us != null) {
				try {
					redis.incr(Message.redisUnreadNumKey(to));
				} catch (Exception e) {
					LOGGER.error("Redis operate ERROR:", e);
				}
			}
		} else {
			return new Response(Response.NO, "ERR_type");
		}
		dragonMessage.setType(type.intValue());
		dragonMessage.setFrom(req.getUserId());
		if (sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).has("title")) {
			dragonMessage.setTitle(sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getString("title"));
		}
		if (sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).has("body")) {
			dragonMessage.setBody(sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getString("body"));
		}
		if (sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).has("attach")) {
			dragonMessage.setAttach(sendParam.getJSONObject(DragonComponent.REQ_SEND_ACTION).getString("attach"));
		}
		save(dragonMessage);
		return new Response(Response.YES);
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
			remove(get(messageId, DragonMessage.class));
		}
		return new Response(Response.YES);
	}

	private void restUserCoin(DragonData userData, Integer deduct) throws JSONException {
		userData.setCoin(userData.getCoin() - deduct);
		JSONObject jsonData = userData.toJSONObject();
		if(jsonData.has(DragonDataManagerImpl.DRAGON_BASE_INFO_KEY)) {
			jsonData.getJSONObject(DragonDataManagerImpl.DRAGON_BASE_INFO_KEY).put(DragonDataManagerImpl.DRAGON_COIN_KEY, userData.getCoin());
		}
		if(jsonData.has(UserAppData.TOUCH_TIME_KEY)) {
			jsonData.remove(UserAppData.TOUCH_TIME_KEY);
		}
		userData.setJsonData(jsonData.toString());
	}

}
