package com.ngnsoft.ngp.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.stereotype.Service;

import sun.misc.BASE64Decoder;

import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.Response;
import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.component.coc.model.CocUserFriend;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.dragon.DragonComponent;
import com.ngnsoft.ngp.component.dragon.model.DragonClan;
import com.ngnsoft.ngp.component.dragon.model.DragonClanUser;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.dragon.model.DragonUserFriend;
import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.BaseModel;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Message;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.SnsData;
import com.ngnsoft.ngp.model.SnsFriend;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserAccount;
import com.ngnsoft.ngp.model.UserApp;
import com.ngnsoft.ngp.model.UserAppData;
import com.ngnsoft.ngp.model.UserFriend;
import com.ngnsoft.ngp.model.UserKey;
import com.ngnsoft.ngp.model.UserProfile;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.model.show.Comrade;
import com.ngnsoft.ngp.model.show.FriendInfo;
import com.ngnsoft.ngp.protocol.user.USFS;
import com.ngnsoft.ngp.service.DataManager;
import com.ngnsoft.ngp.service.MessageManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.user.key.DefaultKey;
import com.ngnsoft.ngp.user.key.DeviceKey;
import com.ngnsoft.ngp.user.key.FacebookKey;
import com.ngnsoft.ngp.user.key.GamecenterKey;
import com.ngnsoft.ngp.user.key.GoogleplayKey;
import com.ngnsoft.ngp.user.key.KeyFactory;
import com.ngnsoft.ngp.user.key.NameKey;
import com.ngnsoft.ngp.user.key.SnsKey;
import com.ngnsoft.ngp.user.key.WeiboKey;
import com.ngnsoft.ngp.util.ConvertUtil;
import com.ngnsoft.ngp.util.DateUtil;

/**
 *
 * @author fcy
 */
@Service
public class UserManagerImpl extends GenericManagerImpl implements UserManager {

	public static final String INFO_CREATE = "CREATE";
	public static final String INFO_LOGIN = "LOGIN";
	public static final String INFO_RELOGIN = "RELG";
	public static final String INFO_AUTOLOGIN = "AUTO";
	public static final String INFO_SWUS = "SWUS";

	Map<String, AtomicLong> deviceLock = new ConcurrentHashMap<String, AtomicLong>();

	Map<Long, AtomicLong> userLock = new ConcurrentHashMap<Long, AtomicLong>();

	@Autowired
	private DataManager dataManager;
	@Autowired
	private UserSessionManager userSessionManager;
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private MessageManager mm;
	@Autowired
	@Qualifier("redisImpl")
	private RedisImpl redisImpl;

	@Override
	public Response login(Request req, App app) {
		String info = INFO_LOGIN;
		String sid = req.getSession();
		if (sid != null && !sid.isEmpty()) {
			info = INFO_RELOGIN;
		}
		String macId = req.getMacId();
		String idfa = req.getIdfa();
		if ((macId == null || macId.isEmpty()) && (idfa == null || idfa.isEmpty())) {
			return new Response(Response.NO, "NO_DEVICE_ID");
		}
		String sns = null;
		String snsKeyType = null;
		if (req.hasKey("sns")) {
			snsKeyType = req.getStr("sns");
			sns = req.getStr(snsKeyType);
		} else {
			if (req.hasKey("googleplay")) {
				sns = req.getStr("googleplay");
				snsKeyType = GoogleplayKey.TYPE;
			} else if (req.hasKey("gamecenter")) {
				sns = req.getStr("gamecenter");
				snsKeyType = GamecenterKey.TYPE;
			} else if (req.hasKey("facebook")) {
				sns = req.getStr("facebook");
				snsKeyType = FacebookKey.TYPE;
			} else if (req.hasKey("weibo")) {
				sns = req.getStr("weibo");
				snsKeyType = WeiboKey.TYPE;
			}
		}

		User user = null;
		Map deviceUser = getUserIdByDevice(macId, idfa);
		String deviceUserIdStr = null;
		if (deviceUser != null) {
			Object o = deviceUser.get("user_id");
			if (o != null) {
				deviceUserIdStr = o.toString();
			}
		}
		Long deviceUserId;
		Long oldDeviceKeyId = null;
		if (deviceUserIdStr != null) {
			deviceUserId = Long.parseLong(deviceUserIdStr);
			oldDeviceKeyId = Long.parseLong(deviceUser.get("uk_id").toString());
		} else {
			String lockDevice = macId + idfa;
			AtomicLong lock = getDeviceLock(lockDevice);
			synchronized (lock) {
				deviceUser = getUserIdByDevice(macId, idfa);
				if (deviceUser != null) {
					Object o = deviceUser.get("user_id");
					if (o != null) {
						deviceUserIdStr = o.toString();
					}
				}
				if (deviceUserIdStr == null) {
					String deviceId;
					if (deviceUser == null) {
						Device device = new Device();
						device.setMacId(macId);
						device.setIdfa(idfa);
						save(device);
						deviceId = device.getId();
					} else {
						deviceId = (String) deviceUser.get("device_id");
					}
					user = createUser();
					deviceUserId = user.getId();
					createUserKey(deviceId, deviceUserId, DeviceKey.TYPE, req.getAppId());
					info = INFO_CREATE;
				} else {
					deviceUserId = Long.parseLong(deviceUserIdStr);
					oldDeviceKeyId = Long.parseLong(deviceUser.get("uk_id").toString());
				}
				if(lock.decrementAndGet() == 0) {
					deviceLock.remove(lockDevice);
				}
			}
		}
		if (sns != null && !sns.isEmpty()) {
			UserKey snsKey = findObject(new UserKey(null, sns, null, snsKeyType, null));
			if (snsKey == null || (snsKey.getUserId().longValue() != deviceUserId.longValue())) {
				String choice = req.getStr("choice");
				if (choice != null && !choice.isEmpty()) {
					if (DeviceKey.TYPE.equals(choice)) {
						if (snsKey != null) {
							/*
							 * snsKey.setUserId(deviceUserId);
							 * snsKey.setKeyFrom(req.getAppId());
							 * update(snsKey);
							 *
							 */
							JSONObject jo = new JSONObject();
							String deviceSns = null;
							Long snsUserId = null;
							if (snsKey != null) {
								snsUserId = snsKey.getUserId();
							}
							List<UserKey> ls = findMulti(new UserKey(null, null, deviceUserId, snsKeyType, null));
							if (ls != null && !ls.isEmpty()) {
								UserKey deviceSnsKey = ls.get(0);
								deviceSns = deviceSnsKey.getUserKey();
							}
							if (deviceSns != null) {
								jo.put(DeviceKey.TYPE, deviceSns);
							}
							if (snsUserId != null) {
								jo.put(snsKeyType, snsUserId);
							}
							return new Response(Response.NO, "SNS_USED", jo);
						} else {
							createUserKey(sns, deviceUserId, snsKeyType, req.getAppId());
						}
						user = get(deviceUserId, User.class);
					} else {
						if (snsKey == null) {
							user = createUser();
							info = INFO_CREATE;
							createUserKey(sns, user.getId(), snsKeyType, req.getAppId());
						} else {
							user = get(snsKey.getUserId(), User.class);
						}
						if (oldDeviceKeyId != null) {
							UserKey deviceKey = new UserKey(oldDeviceKeyId);
							deviceKey.setUserId(user.getId());
							deviceKey.setKeyFrom(req.getAppId());
							update(deviceKey);
						}
					}
				} else {
					if (snsKey != null) {
						JSONObject jo = new JSONObject();
						String deviceSns = null;
						Long snsUserId = snsKey.getUserId();
						List<UserKey> ls = findMulti(new UserKey(null, null, deviceUserId, snsKeyType, null));
						if (ls != null && !ls.isEmpty()) {
							UserKey deviceSnsKey = ls.get(0);
							deviceSns = deviceSnsKey.getUserKey();
						}
						if (deviceSns != null) {
							jo.put(DeviceKey.TYPE, deviceSns);
						}
						if (snsUserId != null) {
							jo.put(snsKeyType, snsUserId);
						}
						return new Response(Response.NO, "DIFF_USER", jo);
					} else {
						createUserKey(sns, deviceUserId, snsKeyType, req.getAppId());
						user = get(deviceUserId, User.class);
					}
				}
			} else {
				user = get(deviceUserId, User.class);
			}
		} else {
			if (user == null) {
				user = get(deviceUserId, User.class);
			}
		}

		Response resp = checkAndLogin(req, app, user, info);
		if (resp.isSuccess()) {
			req.put("userId", user.getId());
			resp.setOutput(getUserJson(req, user, (UserAppData) resp.getAttachObject()));
			resp.setAttachObject(null);
		}
		return resp;
	}

	private User createUser() {
		User user = new User();
		user.setEpswd(UUID.randomUUID().toString().substring(0, 8));
		save(user);
		UserProfile up = new UserProfile(user.getId());
		save(up);
		save(new UserAccount(user.getId()));
		return user;
	}

	private UserKey createUserKey(String key, Long userId, String keyType, String appId) {
		UserKey userKey = new UserKey(null, key, userId, keyType, appId);
		saveIgnoreDke(userKey);
		return userKey;
	}

	private Response checkAndLogin(Request req, App app, User user, String info) {
		if (User.STATUS_NORMAL != user.getStatus()) {
			return new Response(Response.NO, "USER_FORBID", new JSONObject(user.getStatusDetail()));
		}
		boolean withAppData = true;

		if(req.hasKey("appData")) {
			JSONArray appDataKeys = req.getJSONArray("appData");
			if (appDataKeys.length() == 0 || (appDataKeys.length() == 1 && AppComponent.GET_NONE_DATA_KEY.equals(appDataKeys.get(0).toString()))) {
				withAppData = false;
			}
		}
		Device device = getDevice(req);
		String deviceId = device == null ? null : device.getId();
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		Response resp = component.login(user.getId(), deviceId, withAppData, req);
		if (!resp.isSuccess()) {
			return resp;
		}
		String nsid = checkSession(user.getId(), deviceId, app, req);
		resp.setInfo(info);
		resp.setSid(nsid);
		return resp;
	}

	@Override
	public String checkSession(Long userId, String deviceId, App app, Request req) {
		AtomicLong lock = getUserLock(userId);
		String nsid = null;
		synchronized (lock) {
			UserSession os = userSessionManager.matchValidSession(userId);
			if (os != null) {
				if (os.getDeviceId().equals(deviceId)) {//同一设备
					if (os.getAppId().equals(req.getAppId())) {//重复登录
						nsid = os.getId();//找回
					} else {
						nsid = os.getId();//公用一个sid
						onLogin(req, app, userId, deviceId);
					}
				} else {
					userSessionManager.kickSession(os);//互斥登录
				}
			}
			if (nsid == null) {
				onLogin(req, app, userId, deviceId);
				nsid = userSessionManager.login(userId, deviceId, req);
			} else {
				userSessionManager.touchSession(nsid);
			}
			if (lock.decrementAndGet() == 0) {
				userLock.remove(userId);
			}
		}
		return nsid;
	}

	private synchronized AtomicLong getUserLock(Long userId) {
		AtomicLong lock = userLock.get(userId);
		if (lock == null) {
			lock = new AtomicLong(1);
			userLock.put(userId, lock);
		} else {
			lock.incrementAndGet();
		}
		return lock;
	}

	private JSONObject getUserJson(Request req, User user, UserAppData userAppData) {
		JSONObject ret = new JSONObject();
		try {
			ret.put("userId", user.getId());
			ret.put("epswd", user.getEpswd());
			/*
			 * JSONArray userDatas = getUserData(user.getId().toString()); if
			 * (userDatas != null && userDatas.length() > 0) {
			 * ret.put("userData", userDatas.get(0)); }
			 *
			 */
			ret.put("userData", new JSONObject());
			if (userAppData != null) {
				ret.put("ulld", userAppData.getUlld());
				JSONObject resultObj = null;
				if (userAppData instanceof UserData && ((UserData) userAppData).getSearchLock() == 1) {
					UserData tmpUserData = new UserData();
					tmpUserData.setSearchLock(1);
					tmpUserData.setLockTime(((UserData) userAppData).getLockTime());
					resultObj = tmpUserData.toJSONObject();
				} else {
					JSONArray appDataKeys = new JSONArray();
					appDataKeys.put("all");
					JSONObject bizObject = new JSONObject();
					if (!req.hasKey("appData")) {
						bizObject.put("appData", appDataKeys);
					} else {
						bizObject.put("appData", req.getJSONArray("appData"));
					}
					resultObj = dataManager.getDataFromJsonData(bizObject, userAppData.toJSONObject(), "appData");
				}
				if (resultObj != null) {
					ret.put("appData", resultObj);
				}
			} else {
				ret.put("ulld", req.getMacId());
			}
		} catch (JSONException ex) {
		}
		return ret;
	}

	@Override
	public JSONArray getUserData(String... userIds) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("rangeList", Arrays.asList(userIds));
		List<UserProfile> objs = findByNamedQuery("find_userProfile_with_account", paramMap, UserProfile.class);
		JSONArray jsonArray = new JSONArray();
		for (UserProfile profile : objs) {
			JSONObject profileObj = new JSONObject(profile.getJsonAll());
			profileObj.put("coin", profile.getUserAccount().getCoin());
			profileObj.put("userId", profile.getUserId());
			jsonArray.put(profileObj);
		}
		return jsonArray;
	}

	private void onLogin(Request req, App app, Long userId, String deviceId) {
		UserApp userApp = new UserApp(null, userId, app.getId());
		saveIgnoreDke(userApp);

		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		component.onLogin(req, app, userId, deviceId);

		Message message = new Message();
		message.setTo(req.getUserId());
		message.setStatus(Message.STATUS_UNREAD);
		long unreadMsgNum = mm.countTotalNum(message);
		try {
			redisImpl.del(Message.redisUnreadNumKey(userId));
			if (unreadMsgNum > 0) {
				redisImpl.incr(Message.redisUnreadNumKey(userId), unreadMsgNum);
			}
		} catch (Exception e) {
			logger.error("Redis operate ERROR:", e);
		}
	}

	@Override
	public Response swus(Request req, App app) {
		Response resp = validate(req);
		if (resp.isSuccess()) {
			User user = (User) resp.getAttachObject();
			resp = checkAndLogin(req, app, user, INFO_SWUS);
			if (resp.isSuccess()) {
				String macId = req.getMacId();
				String idfa = req.getIdfa();
				Map deviceUser = getUserIdByDevice(macId, req.getIdfa());
				if (deviceUser == null) {
					Device device = new Device();
					device.setMacId(macId);
					device.setIdfa(idfa);
					save(device);
					createUserKey(device.getId(), user.getId(), DeviceKey.TYPE, req.getAppId());
				} else {
					UserKey deviceKey = new UserKey((Long) deviceUser.get("uk_id"));
					deviceKey.setUserId(user.getId());
					deviceKey.setKeyFrom(req.getAppId());
					update(deviceKey);
				}
				req.put("userId", user.getId());
				resp.setOutput(getUserJson(req, user, (UserAppData)resp.getAttachObject()));
				resp.setAttachObject(null);
				//                if (resp.isSuccess()) {//TODO
				//                    logout(req.getSession());//maybe someone else use this sid,should not logout it
				//                }
			}
		}
		return resp;
	}

	public Response validate(Request req) {
		String pass = req.getStr("pass");
		if (pass == null || pass.isEmpty()) {
			return new Response(Response.NO, "NO_pass");
		}
		String user = req.getStr("user");
		if (user == null || user.isEmpty()) {
			return new Response(Response.NO, "NO_user");
		}
		String token = req.getStr("token");
		if (token == null) {
			token = "";
		}
		Response resp;
		if (DefaultKey.TYPE.equals(pass) || NameKey.TYPE.equals(pass)) {
			User u;
			if (DefaultKey.TYPE.equals(pass)) {
				u = (User) get(Long.parseLong(user), User.class);
			} else {
				u = (User) findObject(new User(null, user, null, null, null));
			}
			if (u != null) {
				if (token == null || token.isEmpty()) {
					resp = new Response(Response.NO, "NO_PSWD");
				} else {
					boolean isPawdValid = false;
					if (passwordEncoder != null) {
						passwordEncoder.isPasswordValid(u.getEpswd(), token, null);
					} else {
						isPawdValid = token.equals(u.getOpswd());
					}
					if (isPawdValid) {
						resp = new Response(Response.YES);
						resp.setAttachObject(u);
					} else {
						resp = new Response(Response.NO, "ERR_PSWD");
					}
				}
			} else {
				resp = new Response(Response.NO, "NO_USER");
			}
		} else {
			//            if ("weibo".equals(pass)) {
			UserKey userKey = (UserKey) findObject(new UserKey(null, user, null, pass, null));
			if (userKey != null) {
				SnsKey snsKey = KeyFactory.getSnsKey(pass);
				boolean isValid = snsKey.validate(user, token);
				if (isValid) {
					resp = new Response(Response.YES);
					User u = (User) get(userKey.getUserId(), User.class);
					resp.setAttachObject(u);
				} else {
					resp = new Response(Response.NO, "ERR_TOKEN");
				}
			} else {
				resp = new Response(Response.NO, "NO_USER");
			}
			//            } else if ("facebook".equals(pass)) {
			//            } else if ("googleplay".equals(pass)) {
			//            } else if ("gamecenter".equals(pass)) {
			//            } else {
			//                resp = new Response(Response.NO, "ERR_pass");
			//            }
		}
		return resp;
	}

	@Override
	public Response updateUser(Request req) throws JSONException, IOException {
		Long userId = req.getUserId();
		if (userId == null) {
			return new Response(Response.NO, "NO_USER");
		}
		JSONObject bizData = req.getBizData();
		Response resp = checkUpdateUser(bizData, userId);
		if (!resp.isSuccess()) {
			return resp;
		}
		User user = (User) resp.getAttachObject();
		resp = checkUpdateUserProfile(bizData, userId);
		if (!resp.isSuccess()) {
			return resp;
		}
		UserProfile userProfile = (UserProfile) resp.getAttachObject();
		Iterator enu = bizData.keys();
		User upUser = null;
		UserProfile upUserProfile = null;
		JSONObject upJsonAll = null;
		while (enu.hasNext()) {
			String key = (String) enu.next();
			if ("passwd".equals(key)) {
				String keyValue = bizData.getString(key);
				if (user == null) {
					user = get(userId, User.class);
				}
				if (!keyValue.equals(user.getOpswd())) {
					if (upUser == null) {
						upUser = new User(userId);
					}
					upUser.setOpswd(keyValue);
					if (passwordEncoder != null) {
						upUser.setEpswd(passwordEncoder.encodePassword(keyValue, null));
					}
				}
			} else if ("name".equals(key) || "nick".equals(key)) {//name and nick
				String keyValue = bizData.getString(key);
				if (user == null) {
					user = get(userId, User.class);
				}
				if (userProfile == null) {
					userProfile = loadUserProfile(userId);
				}
				if ("name".equals(key)) {
					if (!keyValue.equals(user.getName())) {
						if (upUser == null) {
							upUser = new User(userId);
						}
						upUser.setName(keyValue);
						if (upJsonAll == null) {
							upJsonAll = new JSONObject(userProfile.getJsonAll());
						}
						upJsonAll.put(key, keyValue);
					}
				} else if ("nick".equals(key)) {
					if (!keyValue.equals(user.getNick())) {
						if (upUser == null) {
							upUser = new User(userId);
						}
						upUser.setNick(keyValue);
						if (upJsonAll == null) {
							upJsonAll = new JSONObject(userProfile.getJsonAll());
						}
						upJsonAll.put(key, keyValue);
					}
				}
			} else {//user_profile famous attribute update
				String keyValue = bizData.getString(key);
				if (userProfile == null) {
					userProfile = (UserProfile) get(userId, UserProfile.class);
				}
				if ("age".equals(key) || "gender".equals(key) || "country".equals(key) || "email".equals(key) || "mobile".equals(key) || "avatar".equals(key)) {
					if ("age".equals(key)) {
						int age = Integer.parseInt(keyValue);
						if (age != userProfile.getAge()) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							upUserProfile.setAge(age);
						}
					} else if ("gender".equals(key)) {
						int gender = Integer.parseInt(keyValue);
						if (gender != userProfile.getGender()) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							upUserProfile.setGender(gender);
						}
					} else if ("country".equals(key)) {
						if (!keyValue.equals(userProfile.getCountry())) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							upUserProfile.setCountry(keyValue);
						}
					} else if ("email".equals(key)) {
						if (!keyValue.equals(userProfile.getEmail())) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							upUserProfile.setEmail(keyValue);
						}
					} else if ("mobile".equals(key)) {
						if (!keyValue.equals(userProfile.getMobile())) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							upUserProfile.setMobile(keyValue);
						}
					} else {//avatar
						if (keyValue != null) {
							if (upUserProfile == null) {
								upUserProfile = new UserProfile(userId);
							}
							if (keyValue.isEmpty()) {
								upUserProfile.setAvatar("");
							} else {
								String avatarType = bizData.getString("avatarType");
								if ("http".equals(avatarType)) {
									upUserProfile.setAvatar(keyValue);
								} else if ("file".equals(avatarType)) {
									String uuid = Integer.toHexString(keyValue.hashCode());
									String avatar = FileStorage.genUrn(FileStorage.SHORT_NAME_AVATAR) + uuid;
									upUserProfile.setAvatar(avatar);
								} else if (avatarType.startsWith("base64")) {
									BASE64Decoder decoder = new BASE64Decoder();
									byte[] byteData = decoder.decodeBuffer(keyValue);
									for (int i = 0; i < byteData.length; i++) {
										if (byteData[i] < 0) {
											byteData[i] += 256;
										}
									}
									FileStorage fs = new FileStorage("avatar", null);
									fs.setData(byteData);
									fs.setName(avatarType);
									save(fs);
									upUserProfile.setAvatar(fs.getUrn());
									upUserProfile.setUpdateTime(new Date());
								}
							}
						}
					}
				} else if ("userId".equals(key) || "avatarType".equals(key)) {//not update item
					//do nothing
				} else {//only json_all at user_profile
					if (upJsonAll == null) {
						if (userProfile == null) {
							userProfile = loadUserProfile(userId);
						}
						upJsonAll = new JSONObject(userProfile.getJsonAll());
					}
					upJsonAll.put(key, keyValue);
				}
			}
		}
		if (upUser != null) {
			update(upUser);
		}
		if (upJsonAll != null) {
			if (upUserProfile == null) {
				upUserProfile = new UserProfile(userId);
			}
			upUserProfile.setJsonAll(upJsonAll.toString());
		}
		if (upUserProfile != null) {
			update(upUserProfile);
		}
		return new Response(Response.YES);
	}

	private UserProfile loadUserProfile(Long userId) {
		UserProfile userProfile = get(userId, UserProfile.class);
		if (userProfile == null) {
			userProfile = new UserProfile(userId);
		}
		return userProfile;
	}

	private Response checkUpdateUser(JSONObject bizData, Long userId) throws JSONException {
		User user = null;
		if (bizData.has("name")) {
			String name = bizData.getString("name");
			if (name.isEmpty()) {
				return new Response(Response.NO, "EMPTY_name");
			}
			user = get(userId, User.class);
			if (!name.equalsIgnoreCase(user.getName())) {
				User test = new User();
				test.setName(name);
				if (exists(test)) {
					return new Response(Response.NO, "DUPL_name");
				}
			}
		}

		if (bizData.has("nick")) {
			String nick = bizData.getString("nick");
			if (nick.isEmpty()) {
				return new Response(Response.NO, "EMPTY_nick");
			}
			if (user == null) {
				user = get(userId, User.class);
			}
			if (!nick.equalsIgnoreCase(user.getNick())) {
				User test = new User();
				test.setNick(nick);
				if (exists(test)) {
					return new Response(Response.NO, "DUPL_nick");
				}
			}
		}

		if (bizData.has("passwd")) {
			String passwd = bizData.getString("passwd");
			if (passwd.isEmpty()) {
				return new Response(Response.NO, "EMPTY_passwd");
			}
			if (user == null) {
				user = get(userId, User.class);
			}
		}
		Response resp = new Response(Response.YES);
		resp.setAttachObject(user);
		return resp;
	}

	private Response checkUpdateUserProfile(JSONObject bizData, Long userId) throws JSONException {
		UserProfile up = null;
		/*
		 * if (bizData.has("email")) { if (up == null) { up = (UserProfile)
		 * get(userId, UserProfile.class); } String email =
		 * bizData.getString("email"); if
		 * (!email.equalsIgnoreCase(up.getEmail())) { if
		 * (exist(email.toLowerCase(), UserKey.class)) { return new
		 * Response(Response.NO, "DUPL_email"); } } }
		 *
		 * if (bizData.has("mobile")) { if (up == null) { up = (UserProfile)
		 * get(userId, UserProfile.class); } String mobile =
		 * bizData.getString("mobile"); if (!mobile.equals(up.getMobile())) { if
		 * (exist(mobile, UserKey.class)) { return new Response(Response.NO,
		 * "DUPL_mobile"); } } }
		 *
		 */
		Response resp = new Response(Response.YES);
		resp.setAttachObject(up);
		return resp;
	}

	@Override
	public void addFriends(Long userId, App app, JSONArray frds) {
		for (int i = 0; i < frds.length(); i++) {
			JSONObject jo = null;
			try {
				jo = frds.getJSONObject(i);
				String sns = jo.getString("sns");
				String message = jo.has("msg") ? jo.getString("msg") : null;
				SnsKey uk = KeyFactory.getSnsKey(sns);
				String snsId = uk.getSnsId(jo.getJSONObject("data"));
				String type = uk.getKeyType();
				if (NameKey.TYPE.equals(type)) {
					User searchUser = new User();
					searchUser.setName(snsId);
					User user = (User) findObject(searchUser);
					if (user == null) {
						continue;
					}
					snsId = user.getId().toString();
					type = DefaultKey.TYPE;
				}
				addAndConfirmFriends(userId, snsId, message, type, app, jo.getJSONObject("data"));
			} catch (Exception e) {
				logger.error("Parsing json object or saving userFriends ERROR >>>", e);
			}
		}
	}

	private void addAndConfirmFriends(Long fromUserId, String snsId, String msg, String type, App app, JSONObject snsObject) throws JSONException {
		//query records that adding friends and inviting to become friends
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		UserAppData uad = component.getUadModel(fromUserId);
		uad = (UserAppData) findObject(uad);
		UserFriend blankUf = generateUserFriend(uad);
		if (DefaultKey.TYPE.equals(type)) {
			if (fromUserId.intValue() != Long.valueOf(snsId).intValue()) {
				// must not be the same user
				try {
					User user = (User) get(Long.valueOf(snsId), User.class);
					if (user != null) {
						Map<String, Object> paramsMap = new HashMap<String, Object>();
						paramsMap.put("userId", fromUserId);
						paramsMap.put("snsId", snsId);

						List<UserFriend> userFriends = findByNamedQuery("find_userFriends_by_fa_or_fb", paramsMap, blankUf.getClass(), UserFriend.class);
						if (userFriends.size() > 0) {
							for (UserFriend userFriend : userFriends) {
								if (userFriend.getFb().intValue() == fromUserId.intValue() && userFriend.getFs() == 0) {
									//confirm friendShip
									UserFriend updateObj = blankUf;
									updateObj.setId(userFriend.getId());
									updateObj.setFs(UserFriend.CONFIRMED);
									update(updateObj);
									break;
								}
							}
						} else {
							// add friends
							UserFriend userFriend = blankUf;
							userFriend.setFa(fromUserId);
							userFriend.setFb(Long.valueOf(snsId));
							userFriend.setFs(UserFriend.UNCONFIRMED);
							userFriend.setMsg(msg);
							save(userFriend);
						}

					}
				} catch (NumberFormatException e) {
				}
			}
		} else {
			UserKey searchUserKey = new UserKey();
			searchUserKey.setUserKey(snsId);
			searchUserKey.setKeyType(type);
			UserKey userKey = (UserKey) findObject(searchUserKey);
			if (userKey != null) {
				Map<String, Object> paramsMap = new HashMap<String, Object>();
				paramsMap.put("userId", fromUserId);
				paramsMap.put("snsId", userKey.getUserKey());
				paramsMap.put("sns", type);
				List<UserFriend> userFriends = findByNamedQuery("find_userFriends_by_fa_or_fb", paramsMap, blankUf.getClass(), UserFriend.class);
				if (userFriends.size() > 0) {
					for (UserFriend userFriend : userFriends) {
						if (userFriend.getFb().intValue() == fromUserId.intValue() && userFriend.getFs() == 0) {
							//confirm friendShip
							UserFriend updateObj = blankUf;
							updateObj.setId(userFriend.getId());
							updateObj.setFs(UserFriend.CONFIRMED);
							update(updateObj);
							break;
						}
					}
				} else {
					// add friends
					UserFriend userFriend = blankUf;
					userFriend.setFa(fromUserId);
					userFriend.setFb(userKey.getUserId());
					userFriend.setFs(UserFriend.UNCONFIRMED);
					userFriend.setMsg(msg);
					save(userFriend);
					//insert into user_sns if fb is on other platform
					SnsData searchData = new SnsData();
					searchData.setUserKey(snsId);
					searchData.setKeyType(type);

					BaseModel baseModel = findObject(searchData);
					if (baseModel != null) {
						SnsData snsData = (SnsData) baseModel;
						snsData.setSnsJson(snsObject.toString());
						update(snsData);
					} else {
						SnsData snsData = new SnsData();
						snsData.setUserKey(snsId);
						snsData.setKeyType(type);
						snsData.setSnsJson(snsObject.toString());
						save(snsData);
					}
				}
			} else {
				SnsFriend snsFriend = new SnsFriend();
				snsFriend.setFaUid(fromUserId);
				snsFriend.setFbUk(snsId);
				snsFriend.setFbKt(type);
				save(snsFriend);
			}
		}
	}

	private UserFriend generateUserFriend(UserAppData uad) {
		if(uad instanceof UserData) {
			return new CocUserFriend();
		} else if(uad instanceof DragonData) {
			return new DragonUserFriend();
		} else {
			return new UserFriend();
		}
	}

	@Override
	public Response lgsn(Long userId, String appId, String sns, JSONObject data) {
		SnsKey snsKey = KeyFactory.getSnsKey(sns);
		snsKey.mapKeys(data);
		UserKey userKey = (UserKey) findObject(new UserKey(null, snsKey.getUserKey(), null, sns, null));
		SnsData userSns = (SnsData) findObject(new SnsData(null, snsKey.getUserKey(), sns, null));
		if (userKey == null) {
			save(new UserKey(null, snsKey.getUserKey(), userId, sns, appId));
			if (userSns == null) {
				save(new SnsData(null, snsKey.getUserKey(), sns, data.toString()));
			} else {
				userSns.setSnsJson(data.toString());
				update(userSns);
				List<SnsFriend> sfs = findMulti(new SnsFriend(null, null, snsKey.getUserKey(), snsKey.getKeyType(), null));
				if (sfs != null) {
					for (int i = 0; i < sfs.size(); i++) {
						SnsFriend sf = sfs.get(i);
						SnsFriend upSf = new SnsFriend();
						upSf.setId(sf.getId());
						upSf.setFbUid(userId);
						update(upSf);
					}
				}
			}
		} else {
			if (userKey.getUserId() != userId) {
				return new Response(Response.NO, "SNS_EXIST");
			} else {
				userKey.setKeyFrom(appId);
				update(userKey);
				userSns.setSnsJson(data.toString());
				update(userSns);
			}
		}
		if (data.has("nick")) {
			User upUser = new User(userId);
			try {
				upUser.setNick(data.getString("nick"));
				update(upUser);
			} catch (JSONException ex) {
			}
		}
		if (data.has("avatar")) {
			UserProfile upUserProfile = new UserProfile(userId);
			try {
				upUserProfile.setAvatar(data.getString("avatar"));
				update(upUserProfile);
			} catch (JSONException ex) {
			}
		}
		return new Response(Response.YES);
	}

	@Override
	public Response mtuf(Request req) {
		Message msg = new Message();
		msg.setFrom(req.getUserId());
		msg.setTo(req.getLong("to"));
		if (req.hasKey("title")) {
			msg.setTitle(req.getStr("title"));
		}
		if (req.hasKey("body")) {
			msg.setBody(req.getStr("body"));
		}
		if (req.hasKey("attach")) {
			msg.setAttach(req.getStr("attach"));
		}
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		paramsMap.put("ua", msg.getFrom());
		paramsMap.put("ub", msg.getTo());
		List<UserFriend> userFriends = findByNamedQuery("find_user_friend_by_ua_and_ub", paramsMap, UserFriend.class);
		if (userFriends == null) {
			return new Response(Response.NO, "NOT_FRIEND");
		}
		save(msg);
		return new Response(Response.YES);
	}

	@Override
	public Response ubsn(Request req) {
		String sns = req.getStr("sns");
		String snsId = req.getStr("snsId");
		UserKey userKey = new UserKey(null, snsId, req.getUserId(), sns, null);
		remove(userKey);
		List<SnsFriend> sfs = findMulti(new SnsFriend(null, null, snsId, sns, null));
		if (sfs != null) {
			for (int i = 0; i < sfs.size(); i++) {
				SnsFriend sf = sfs.get(i);
				SnsFriend upSf = new SnsFriend();
				upSf.setId(sf.getId());
				upSf.setFbUid(0L);
				update(upSf);
			}
		}
		return new Response(Response.YES);
	}

	@Override
	public JSONObject listFriends(Request req, String sns, Pagination page) throws JSONException {
		Long snsApp = req.getJSONObject(USFS.LIST_FRIENDS_TYPE).has("app") ? req.getJSONObject(USFS.LIST_FRIENDS_TYPE).getLong("app") : 0l;
		Long confirm = req.getJSONObject(USFS.LIST_FRIENDS_TYPE).has("confirm") ? req.getJSONObject(USFS.LIST_FRIENDS_TYPE).getLong("confirm") : 0l;
		//add user information if userData is 1
		Long userData = req.getJSONObject(USFS.LIST_FRIENDS_TYPE).has("userData") ? req.getJSONObject(USFS.LIST_FRIENDS_TYPE).getLong("userData") : 0l;
		//add sns's friends if snsData is 1
		Long snsData = req.getJSONObject(USFS.LIST_FRIENDS_TYPE).has("snsData") ? req.getJSONObject(USFS.LIST_FRIENDS_TYPE).getLong("snsData") : 0l;
		JSONArray appDataKeys = req.getJSONObject(USFS.LIST_FRIENDS_TYPE).has("appData") ? req.getJSONObject(USFS.LIST_FRIENDS_TYPE).getJSONArray("appData") : new JSONArray();

		Long userId = req.getUserId();
		String appId = req.getAppId();
		JSONObject resultData = new JSONObject();
		Long count = 0l;
		JSONArray infoArray = new JSONArray();
		App app = (App) get(appId, App.class);
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		UserAppData uad = component.getUadModel(req.getUserId());
		uad = (UserAppData) findObject(uad);
		//get friends
		if (sns.equals(DefaultKey.TYPE)) {
			//local friends
			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			paramMap.put("snsApp", snsApp);
			paramMap.put("appId", app.getBaseId());
			paramMap.put("confirm", (confirm + 1) % 2);
			if (uad instanceof UserData) {
				if (snsApp == 1) {
					count = countByNameQuery("count_local_friends_with_coc_info_by_map", paramMap, uad.getClass());
				} else {
					count = countByNameQuery("count_local_friends_without_app_info_by_map", paramMap, CocUserFriend.class);
				}
				resultData.put("count", count);
				if (count > 0) {
					List<FriendInfo> friendInfos = null;
					if (snsApp == 1) {
						friendInfos = findByNamedQuery("find_local_friends_with_coc_info_by_map", paramMap, page, uad.getClass(), FriendInfo.class);
					} else {
						List<UserFriend> userFriends = findByNamedQuery("find_local_friends_without_app_info_by_map", paramMap, page, CocUserFriend.class, UserFriend.class);
						friendInfos = new ArrayList<FriendInfo>();
						for (UserFriend userFriend : userFriends) {
							FriendInfo friendInfo = new FriendInfo();
							if (userFriend.getFa().intValue() == userId.intValue()) {
								friendInfo.setUserKey(userFriend.getFb().toString());
							} else {
								friendInfo.setUserKey(userFriend.getFa().toString());
							}
							friendInfos.add(friendInfo);
						}
					}
					for (FriendInfo info : friendInfos) {
						info.setKeyType(DefaultKey.TYPE);
						JSONObject friendJson = info.toJSONObject();
						if (appDataKeys.length() > 0) {
							setJsonDataFromAppData(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), Long.valueOf(info.getUserKey()), friendJson, app.getBaseId(), uad);
						}
						if (userData > 0) {
							JSONArray jsonArray = getUserData(info.getUserKey());
							if (jsonArray.length() > 0) {
								friendJson.put("userData", jsonArray.getJSONObject(0));
							}
						}
						infoArray.put(friendJson);
					}
					resultData.put("data", infoArray);
				}
			} else if (uad instanceof DragonData) {
				if (snsApp == 1) {
					count = countByNameQuery("count_local_friends_with_dragon_info_by_map", paramMap, uad.getClass());
				} else {
					count = countByNameQuery("count_local_friends_without_app_info_by_map", paramMap, DragonUserFriend.class);
				}
				resultData.put("count", count);
				if (count > 0) {
					List<FriendInfo> friendInfos = null;
					if (snsApp == 1) {
						friendInfos = findByNamedQuery("find_local_friends_with_dragon_info_by_map", paramMap, page, uad.getClass(), FriendInfo.class);
					} else {
						List<UserFriend> userFriends = findByNamedQuery("find_local_friends_without_app_info_by_map", paramMap, page, DragonUserFriend.class, UserFriend.class);
						friendInfos = new ArrayList<FriendInfo>();
						for (UserFriend userFriend : userFriends) {
							FriendInfo friendInfo = new FriendInfo();
							friendInfo.setMsg(userFriend.getMsg());
							if (userFriend.getFa().intValue() == userId.intValue()) {
								friendInfo.setUserKey(userFriend.getFb().toString());
							} else {
								friendInfo.setUserKey(userFriend.getFa().toString());
							}
							friendInfo.setUserId(Long.valueOf(friendInfo.getUserKey()));
							friendInfos.add(friendInfo);
						}
					}
					for (FriendInfo info : friendInfos) {
						info.setKeyType(DefaultKey.TYPE);
						JSONObject friendJson = info.toJSONObject();
						if (appDataKeys.length() > 0) {
							setJsonDataFromAppData(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), Long.valueOf(info.getUserKey()), friendJson, app.getBaseId(), uad);
						}
						if (userData > 0) {
							JSONArray jsonArray = getUserData(info.getUserKey());
							if (jsonArray.length() > 0) {
								friendJson.put("userData", jsonArray.getJSONObject(0));
							}
						}
						infoArray.put(friendJson);
					}
					resultData.put("data", infoArray);
				}
			} else {
				if (snsApp == 1) {
					count = countByNameQuery("count_local_friends_with_app_info_by_map", paramMap, UserAppData.class);
				} else {
					count = countByNameQuery("count_local_friends_without_app_info_by_map", paramMap, UserFriend.class);
				}
				resultData.put("count", count);
				if (count > 0) {
					List<FriendInfo> friendInfos = null;
					if (snsApp == 1) {
						friendInfos = findByNamedQuery("find_local_friends_with_app_info_by_map", paramMap, page, UserAppData.class, FriendInfo.class);
					} else {
						List<UserFriend> userFriends = findByNamedQuery("find_local_friends_without_app_info_by_map", paramMap, page, UserFriend.class);
						friendInfos = new ArrayList<FriendInfo>();
						for (UserFriend userFriend : userFriends) {
							FriendInfo friendInfo = new FriendInfo();
							friendInfo.setMsg(userFriend.getMsg());
							if (userFriend.getFa().intValue() == userId.intValue()) {
								friendInfo.setUserKey(userFriend.getFb().toString());
							} else {
								friendInfo.setUserKey(userFriend.getFa().toString());
							}
							friendInfo.setUserId(Long.valueOf(friendInfo.getUserKey()));
							friendInfos.add(friendInfo);
						}
					}
					for (FriendInfo info : friendInfos) {
						info.setKeyType(DefaultKey.TYPE);
						JSONObject friendJson = info.toJSONObject();
						if (appDataKeys.length() > 0) {
							setJsonDataFromAppData(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), Long.valueOf(info.getUserKey()), friendJson, app.getBaseId(), uad);
						}
						if (userData > 0) {
							JSONArray jsonArray = getUserData(info.getUserKey());
							if (jsonArray.length() > 0) {
								friendJson.put("userData", jsonArray.getJSONObject(0));
							}
						}
						infoArray.put(friendJson);
					}
					resultData.put("data", infoArray);
				}
			}
			return resultData;
		} else {
			//other sns platform
			if (snsApp == 0) {
				SnsFriend searchSnsFriend = new SnsFriend();
				searchSnsFriend.setFaUid(userId);
				searchSnsFriend.setFbKt(sns);
				count = countTotalNum(searchSnsFriend);
				resultData.put("count", count);
				if (count > 0) {
					List<SnsFriend> snsFriends = findMulti(searchSnsFriend, page);
					if (uad instanceof UserData) {
						resultData.put("data", convertFriends2Array(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), appDataKeys, app.getBaseId(), snsData, snsFriends, true));
					} else {
						resultData.put("data", convertFriends2Array(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), appDataKeys, app.getBaseId(), snsData, snsFriends, false));
					}
				}
			} else {
				Map paramMap = new HashMap();
				paramMap.put("userId", userId);
				paramMap.put("appId", app.getBaseId());
				paramMap.put("sns", sns);
				if (uad instanceof UserData || uad instanceof DragonData) {
					count = countByNameQuery("count_snsFriends_join_userData_by_map", paramMap, SnsFriend.class);
					if (count > 0) {
						List<SnsFriend> snsFriends = findByNamedQuery("find_snsFriends_join_userData_by_map", paramMap, page, SnsFriend.class);
						resultData.put("data", convertFriends2Array(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), appDataKeys, app.getBaseId(), snsData, snsFriends, true));
					}
				} else {
					count = countByNameQuery("count_snsFriends_join_userAppData_by_map", paramMap, SnsFriend.class);
					if (count > 0) {
						List<SnsFriend> snsFriends = findByNamedQuery("find_snsFriends_join_userAppData_by_map", paramMap, page, SnsFriend.class);
						resultData.put("data", convertFriends2Array(req.getJSONObject(USFS.LIST_FRIENDS_TYPE), appDataKeys, app.getBaseId(), snsData, snsFriends, true));
					}
				}
			}
		}
		return resultData;
	}

	public void setJsonDataFromAppData(JSONObject fromJson, Long userId, JSONObject friendJson, String baseId, UserAppData userData) {
		UserAppData userAppData = null;
		if (userData instanceof UserData) {
			BaseModel baseModel = get(userId, UserData.class);
			userAppData = baseModel == null ? null : (UserData) baseModel;
		} else if (userData instanceof DragonData) {
			userAppData = get(userId, DragonData.class);
		} else {
			UserAppData searchObj = new UserAppData();
			searchObj.setUserId(userId);
			searchObj.setBaid(baseId);
			List<UserAppData> userAppDatas = findMulti(searchObj);
			userAppData = userAppDatas.size() > 0 ? userAppDatas.get(0) : null;
		}
		if (userAppData != null && userAppData.getJsonData() != null) {
			JSONObject wholeAppDataJson = new JSONObject(userAppData.getJsonData());
			JSONObject appDataObj = dataManager.getDataFromJsonData(fromJson, wholeAppDataJson, "appData");
			appDataObj.put("touchTime", (userAppData.getTouchTime().getTime() * 1000) / 1000);
			friendJson.put("appData", appDataObj);
		}
	}

	private JSONArray convertFriends2Array(JSONObject fromJsonObj, JSONArray appDataKeys, String baseId, Long snsData, List<SnsFriend> snsFriends, boolean iscoc) throws JSONException {
		JSONArray infoArray = new JSONArray();
		for (SnsFriend snsFriend : snsFriends) {
			FriendInfo info = new FriendInfo();
			info.setKeyType(snsFriend.getFbKt());
			info.setUserKey(snsFriend.getFbUk());
			info.setUserId(snsFriend.getFbUid());
			if (snsData.intValue() == 1) {
				SnsData searchObj = new SnsData();
				searchObj.setUserKey(snsFriend.getFbUk());
				searchObj.setKeyType(snsFriend.getFbKt());
				BaseModel baseModel = findObject(searchObj);
				if (baseModel != null) {
					info.setSnsData(((SnsData) baseModel).getSnsJson());
				}
			}
			JSONObject friendJson = info.toJSONObject();
			if (appDataKeys.length() > 0 && snsFriend.getFbUid() != null) {
				if (snsFriend.getFbUid() != null) {
					UserAppData userAppData = null;
					if (iscoc) {
						userAppData = (UserData) get(snsFriend.getFbUid(), UserData.class);
						if (appDataKeys.length() > 0 && userAppData != null) {
							JSONObject wholeAppDataJson = userAppData.getJsonData() == null ? new JSONObject() : new JSONObject(userAppData.getJsonData());
							JSONObject appDataObj = dataManager.getDataFromJsonData(fromJsonObj, wholeAppDataJson, "appData");
							appDataObj.put("touchTime", userAppData.getTouchTime());
							friendJson.put("appData", appDataObj);
						}
					} else {
						UserAppData searchObj = new UserAppData();
						searchObj.setUserId(snsFriend.getFbUid());
						searchObj.setBaid(baseId);
						List<UserAppData> userAppDatas = findMulti(searchObj);
						userAppData = userAppDatas.size() > 0 ? userAppDatas.get(0) : null;

					}
					if (appDataKeys.length() > 0 && userAppData != null) {
						JSONObject wholeAppDataJson = userAppData.getJsonData() == null ? new JSONObject() : new JSONObject(userAppData.getJsonData());
						JSONObject appDataObj = dataManager.getDataFromJsonData(fromJsonObj, wholeAppDataJson, "appData");
						appDataObj.put("touchTime", userAppData.getTouchTime());
						friendJson.put("appData", appDataObj);
					}
				}
			}
			infoArray.put(info.toJSONObject());
		}
		return infoArray;
	}

	private static final int RANDOM_MIN = 5;
	private static final int RANDOM_MAX = 20;

	@Override
	public Response searchFacilitator(Request req) throws JSONException {
		Long userData = req.getJSONObject(DragonComponent.SEARCH_COMRADE_TYPE).has("userData") ? req.getJSONObject(DragonComponent.SEARCH_COMRADE_TYPE).getLong("userData") : 0l;
		JSONArray appDataKeys = req.getJSONObject(DragonComponent.SEARCH_COMRADE_TYPE).has("appData") ? req.getJSONObject(DragonComponent.SEARCH_COMRADE_TYPE).getJSONArray("appData") : new JSONArray();

		JSONObject jo = req.getBizData();
		App app = (App) get(req.getAppId(), App.class);
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		UserAppData uad = component.getUadModel(req.getUserId());
		uad = (UserAppData) findObject(uad);

		Map paramMap = new HashMap();
		paramMap.put("userId", req.getUserId().toString());
		paramMap.put("baseId", app.getBaseId());
		List<Comrade> frs = null;
		int level = 0;
		if (uad instanceof UserData) {
			frs = findByNamedQuery("find_friends_user_data_as_comrade", paramMap, uad.getClass(), Comrade.class);
			level = ((UserData) uad).getLevel();
		} else if (uad instanceof DragonData) {
			frs = findByNamedQuery("find_friends_dragon_data_as_comrade", paramMap, uad.getClass(), Comrade.class);
			level = ((DragonData) uad).getLevel();
		} else {
			frs = findByNamedQuery("find_friends_user_app_data_as_comrade", paramMap, UserAppData.class, Comrade.class);
		}

		List<Long> userIds = new ArrayList<Long>();
		List<Comrade> filterComrads = new ArrayList<Comrade>();
		for (Comrade comrade : frs) {
			userIds.add(comrade.getUserId());
			if (comrade.getLevel() <= level && DateUtil.getDateIntervalWithHour(comrade.getActionTime(), new Date()) < 12) {
				filterComrads.add(comrade);
			} else if (comrade.getLevel() > level && DateUtil.getDateIntervalWithHour(comrade.getActionTime(), new Date()) < (12 - (comrade.getLevel() - level) / 6)) {
				filterComrads.add(comrade);
			}
		}
		paramMap = new HashMap();
		paramMap.put("baseId", app.getBaseId());
		paramMap.put("levelLimit", level - 30);
		paramMap.put("levelUpper", level + 30);
		List<Comrade> riskers = new ArrayList<Comrade>();
		List<Comrade> randOneRiskers = null;
		long count = 0;
		Integer start = 0;
		Integer randIndex = 0;
		Set<Integer> searchIndexs = new HashSet<Integer>();
		if (uad instanceof UserData) {
			count = countByNameQuery("count_risker_user_data_as_comrade", paramMap, uad.getClass());
			if (count <= 5) {
				start = 0;
			} else {
				start = ConvertUtil.generateRandom(0, (int) (count - 5));
			}
			paramMap.put("start", start);
			riskers = findByNamedQuery("find_risker_user_data_as_comrade", paramMap, uad.getClass(), Comrade.class);
		} else if (uad instanceof DragonData) {
			count = countByNameQuery("count_risker_dragon_data_as_comrade", paramMap, uad.getClass());
			for (int i = 0; i < 5; i++) {
				if (count > 0) {
					randIndex = ConvertUtil.generateRandom(0, (int) count - 1);
					paramMap.put("start", randIndex);
					paramMap.put("rangeList", userIds);
					randOneRiskers = findByNamedQuery("find_risker_dragon_data_as_comrade", paramMap, uad.getClass(), Comrade.class);
					if (randOneRiskers != null && randOneRiskers.size() > 0) {
						riskers.addAll(randOneRiskers);
						userIds.add(randOneRiskers.get(0).getUserId());
					}
				}
			}
		} else {
			count = countByNameQuery("count_risker_user_app_data_as_comrade", paramMap, UserAppData.class);
			if (count <= 5) {
				start = 0;
			} else {
				start = ConvertUtil.generateRandom(0, (int) (count - 5));
			}
			paramMap.put("start", start);
			riskers = findByNamedQuery("find_risker_user_app_data_as_comrade", paramMap, UserAppData.class, Comrade.class);
		}

		filterComrads.addAll(riskers);
		JSONArray frsArray = new JSONArray();
		for (Comrade comrade : filterComrads) {
			try {
				boolean flag = true;
				String key = DragonComponent.REDIS_DRAGON_FRD_ID_PREFIX + req.getUserId() + "_" + comrade.getUserId();
				if (comrade.getIsFrd()) {
					try {
						flag = !redisImpl.exist(key);
					} catch (Exception e) {
						logger.error("Redis operate ERROR:", e);
					}
				}
				if (flag) {
					JSONObject jsonUser = new JSONObject();
					jsonUser.put("userId", comrade.getUserId());
					jsonUser.put("isFrd", comrade.getIsFrd());
					if (comrade.getJsonData() != null && appDataKeys.length() > 0) {
						JSONObject jsonObj = dataManager.getDataFromJsonData(req.getJSONObject(DragonComponent.SEARCH_COMRADE_TYPE), new JSONObject(comrade.getJsonData()), "appData");
						jsonUser.put("appData", jsonObj);
					}
					if (userData > 0) {
						JSONArray jsonArray = getUserData(comrade.getUserId().toString());
						if (jsonArray.length() > 0) {
							jsonUser.put("userData", jsonArray.getJSONObject(0));
						}
					}
					frsArray.put(jsonUser);
				}
			} catch (Exception e) {
			}
		}
		JSONObject resultObj = new JSONObject();
		resultObj.put("data", frsArray);

		uad.setActionTime(new Date());
		update(uad);
		resultObj.put("touchTime", (uad.getTouchTime().getTime() / 1000) * 1000);
		return new Response(Response.YES, resultObj);
	}

	@Override
	public Response delFriends(Long userId, App app, JSONArray frds) {
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		UserAppData uad = component.getUadModel(userId);
		uad = (UserAppData) findObject(uad);
		UserFriend blankUf = generateUserFriend(uad);
		for (int i = 0; i < frds.length(); i++) {
			SnsKey uk;
			String snsId;
			JSONObject jsonObj = frds.getJSONObject(i);
			uk = KeyFactory.getSnsKey(jsonObj.getString("keyType"));
			try {
				snsId = jsonObj.getString("userKey");
			} catch (JSONException e) {
				snsId = String.valueOf(jsonObj.getLong("userKey"));
			}
			String type = uk.getKeyType();
			Map paramMap = new HashMap();
			paramMap.put("userId", userId);
			if (DefaultKey.TYPE.equals(type)) {
				paramMap.put("friendId", Long.valueOf(snsId));
				remove("remove_userFriend_by_map", paramMap, blankUf.getClass());
			} else {
				paramMap.put("friendId", snsId);
				paramMap.put("type", type);
				remove("remove_snsFriend_by_map", paramMap, blankUf.getClass());
			}
		}
		return new Response(Response.YES);
	}

	@Override
	public Response usdt(Request req) {
		JSONObject ret = new JSONObject();
		App app = (App) get(req.getAppId(), App.class);
		String[] userIds;
		if (req.hasKey("userId")) {
			userIds = req.getStr("userId").split(",");
		} else {
			userIds = new String[1];
			userIds[0] = String.valueOf(req.getUserId());
		}
		JSONArray ja = new JSONArray();
		if (userIds.length > 0) {
			if (req.hasKey("userData") && req.getLong("userData") == 1) {
				JSONArray uds = getUserData(userIds);
				for (int i = 0; i < uds.length(); i++) {
					JSONObject ud = uds.getJSONObject(i);
					Long userId = ud.getLong("userId");
					AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
					UserAppData uad = component.getUadModel(userId);
					uad = (UserAppData) findObject(uad);

					JSONObject jo = new JSONObject();
					jo.put("userId", userId);
					jo.put("userData", ud);
					if (req.hasKey("appData")) {
						UserAppData userAppData = null;
						if (uad instanceof UserData) {
							userAppData = (UserData) get(userId, UserData.class);
						} else if (uad instanceof DragonData) {
							userAppData = (DragonData) get(userId, DragonData.class);
						} else {
							UserAppData searchObj = new UserAppData();
							searchObj.setUserId(userId);
							searchObj.setBaid(app.getBaseId());
							List<UserAppData> userAppDatas = findMulti(searchObj);
							userAppData = userAppDatas.size() > 0 ? userAppDatas.get(0) : null;
						}
						if (userAppData != null && userAppData.getJsonData() != null) {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getBizData(), new JSONObject(userAppData.getJsonData()), "appData");
							jo.put("appData", jsonObj);
						} else {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getBizData(), new JSONObject(userAppData.getJsonData()), "appData");
							jo.put("appData", jsonObj);
						}
					}
					if (req.hasKey("cmData") && req.getLong("cmData") == 1) {
						DragonClanUser clanUser = new DragonClanUser();
						clanUser.setUserId(userId);
						DragonClanUser cu = findObject(clanUser);
						if (cu != null) {
							DragonClan dc = get(cu.getClanId(), DragonClan.class);
							if (dc != null) {
								JSONObject memberData = new JSONObject();
								memberData.put("role", cu.getRole());
								memberData.put("clanId", dc.getId());
								jo.put("cmData", memberData);
							}
						}
					}
					ja.put(jo);
				}
			} else {
				for (int i = 0; i < userIds.length; i++) {
					JSONObject jo = new JSONObject();
					jo.put("userId", Long.parseLong(userIds[i]));
					if (req.hasKey("appData")) {
						AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
						UserAppData uad = component.getUadModel(Long.valueOf(userIds[i]));
						uad = (UserAppData) findObject(uad);
						UserAppData userAppData = null;
						if (uad instanceof UserData) {
							userAppData = (UserData) get(Long.valueOf(userIds[i]), UserData.class);
						} else if (uad instanceof DragonData) {
							userAppData = (DragonData) get(Long.valueOf(userIds[i]), DragonData.class);
						} else {
							UserAppData searchObj = new UserAppData();
							searchObj.setUserId(Long.valueOf(userIds[i]));
							searchObj.setBaid(app.getBaseId());
							List<UserAppData> userAppDatas = findMulti(searchObj);
							userAppData = userAppDatas.size() > 0 ? userAppDatas.get(0) : null;
						}
						if (userAppData != null && userAppData.getJsonData() != null) {
							JSONObject jsonObj = dataManager.getDataFromJsonData(req.getBizData(), new JSONObject(userAppData.getJsonData()), "appData");
							jo.put("appData", jsonObj);
						}
					}
					if (req.hasKey("cmData") && req.getLong("cmData") == 1) {
						DragonClanUser clanUser = new DragonClanUser();
						clanUser.setUserId(Long.valueOf(userIds[i]));
						DragonClanUser cu = findObject(clanUser);
						if (cu != null) {
							DragonClan dc = get(cu.getClanId(), DragonClan.class);
							if (dc != null) {
								JSONObject memberData = new JSONObject();
								memberData.put("role", cu.getRole());
								memberData.put("clanId", dc.getId());
								jo.put("cmData", memberData);
							}
						}
					}
					ja.put(jo);
				}
			}
		}
		ret.put("data", ja);
		return new Response(Response.YES, ret);
	}

	@Override
	public Response checkEpswd(Request req, App app) {
		Response resp = new Response(Response.YES);
		Long userId = req.getUserId();
		String epswd = req.getEpswd();
		if (userId == null || epswd == null || epswd.isEmpty()) {
			return resp;
		}
		User user = get(userId, User.class);
		if (user != null && epswd.equals(user.getEpswd())) {
			if (User.STATUS_FORBID == user.getStatus()) {
				return new Response(Response.NO, "USER_FORBID", new JSONObject(user.getStatusDetail()));
			} else {
				resp.setAttachObject(user.getId());
			}
		}
		return resp;
	}

	@Override
	public Response checkDevice(Request req, App app) {
		Response resp = new Response(Response.YES);
		String macId = req.getMacId();
		String idfa = req.getIdfa();
		if ((macId == null || macId.isEmpty()) && (idfa == null || idfa.isEmpty())) {
			return new Response(Response.NO, "NO_DEVICE_ID");
		}
		Map deviceUser = getUserIdByDevice(macId, idfa);
		Long userId;
		String deviceId;
		String deviceUserIdStr = null;
		if (deviceUser != null) {
			Object o = deviceUser.get("user_id");
			if (o != null) {
				deviceUserIdStr = o.toString();
			}
		}
		if (deviceUserIdStr == null) {
			UserKey uk = createDeviceUser(macId, idfa, req.getAppId());
			userId = uk.getUserId();
			deviceId = uk.getUserKey();
		} else {
			userId = Long.parseLong(deviceUser.get("user_id").toString());
			deviceId = (String) deviceUser.get("device_id");
		}
		resp.setAttachObject(userId);
		resp.setInfo(deviceId);
		return resp;
	}

	private UserKey createDeviceUser(String macId, String idfa, String appId) {
		String lockDevice = macId + idfa;
		AtomicLong lock = getDeviceLock(lockDevice);
		Long userId;
		String deviceId;
		String deviceUserIdStr = null;
		synchronized(lock) {
			Map deviceUser = getUserIdByDevice(macId, idfa);
			if (deviceUser != null) {
				Object o = deviceUser.get("user_id");
				if (o != null) {
					deviceUserIdStr = o.toString();
				}
			}
			if (deviceUserIdStr == null) {
				if (deviceUser == null) {
					Device device = new Device();
					device.setMacId(macId);
					device.setIdfa(idfa);
					save(device);
					deviceId = device.getId();
				} else {
					deviceId = (String) deviceUser.get("device_id");
				}
				User user = createUser();
				userId = user.getId();
				createUserKey(deviceId, userId, DeviceKey.TYPE, appId);
			} else {
				userId = Long.parseLong(deviceUser.get("user_id").toString());
				deviceId = (String) deviceUser.get("device_id");
			}
			if(lock.decrementAndGet() == 0) {
				deviceLock.remove(lockDevice);
			}
		}
		return new UserKey(null, deviceId, userId, DeviceKey.TYPE, null);
	}

	private synchronized AtomicLong getDeviceLock(String lockDevice) {
		AtomicLong lock = deviceLock.get(lockDevice);
		if (lock == null) {
			lock = new AtomicLong(1);
			deviceLock.put(lockDevice, lock);
		} else {
			lock.incrementAndGet();
		}
		return lock;
	}

	@Override
	public Device getDevice(Request req) {
		String macId = req.getMacId();
		String idfa = req.getIdfa();
		if ((macId == null || macId.isEmpty()) && (idfa == null || idfa.isEmpty())) {
			return null;
		}
		Device device = new Device();
		device.setMacId(req.getMacId());
		device.setIdfa(req.getIdfa());
		String findSql = "find_device_by_or";
		List<Device> ds = findByNamedQuery(findSql, device, Device.class);
		if (ds != null && !ds.isEmpty()) {
			return ds.get(0);
		} else {
			return null;
		}
	}

	@Override
	public String getDvid(Request req) {
		String dvid = req.getDvid();
		if (dvid == null) {
			Device device = getDevice(req);
			if (device != null) {
				dvid = device.getId();
			}
		}
		return dvid;
	}

	private Map getUserIdByDevice(String macId, String idfa) {
		if ((macId == null || macId.isEmpty()) && (idfa == null || idfa.isEmpty())) {
			return null;
		}
		Device device = new Device();
		device.setMacId(macId);
		device.setIdfa(idfa);
		String findSql = "find_userkey_by_device_or";
		List<Map> uks = findByNamedQuery(findSql, device, UserKey.class, Map.class);
		Map deviceUser = null;
		if (uks != null && !uks.isEmpty()) {
			if (uks.size() > 1) {
				logger.error("@@@@@@@@@@@@@@@@uks.size=" + uks.size() + ",macId=" + macId + ",idfa=" + idfa);
			}
			deviceUser = uks.get(0);
			String deviceId = (String) deviceUser.get("device_id");
			boolean up = false;
			Device upDevice = new Device();
			if (macId != null && !macId.isEmpty()) {
				String o = (String)deviceUser.get("mac_id");
				if (!macId.equals(o)) {
					upDevice.setMacId(macId);
					up = true;
				}
			}
			if (idfa != null && !idfa.isEmpty()) {
				String o = (String)deviceUser.get("idfa");
				if (!idfa.equals(o)) {
					upDevice.setIdfa(idfa);
					up = true;
				}
			}
			if (up) {
				upDevice.setId(deviceId);
				upDevice.setUpdateTime(new Date());
				update(upDevice);
			}
		}
		return deviceUser;
	}
}