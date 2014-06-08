package com.ngnsoft.ngp;

import com.ngnsoft.ngp.component.AppComponent;
import com.ngnsoft.ngp.component.AppComponentFactory;
import com.ngnsoft.ngp.enums.DataType;
import com.ngnsoft.ngp.model.*;
import com.ngnsoft.ngp.service.*;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.IOSDesUtil;
import java.io.StringReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.InputSource;

/**
 *
 * @author fcy
 */
public class Engine {

	private static final Logger logger = LoggerFactory.getLogger(Engine.class);
	public static final String REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY = "global_srh_queue_key";
	public static final String REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE = "global_srh_value";
	public static final String REDIS_SRH_STATUS_TRACK_KEY = "srh_track_key";
	public static final String REDIS_SRH_STATUS_TRACK_VALUE = "srh_track_value";
	private ApplicationContext ctx = null;
	private EngineConfig config = null;
	private static Engine instance = new Engine();
	private String id;
	private GenericManager gm;
	private UserManager um;
	private UserSessionManager usm;
	private Map<String, Object> baseIdCacheMap = new HashMap<String, Object>();
	private Map<String, Object> appIdCacheMap = new HashMap<String, Object>();
	private AtomicLong loading = new AtomicLong(0);
	private int status = EngineNode.STATUS_MAINTENANCE;
	private String statusDesc = EngineNode.STATUS_DESC_MAINTENANCE;
	private static final String SUCCESS = "success";
	private static final String FAILED = "failed";
	public static final String PLACARD_REDIS_PREFIX = "placard_cache_";

	private Engine() {
		String[] locations = {
				"classpath:applicationContext-core.xml",
		"classpath*:**/applicationContext*.xml"};
		ctx = new ClassPathXmlApplicationContext(locations);
		config = ctx.getBean(EngineConfig.class);
		logger.info("ApplicationContext loaded successfully!");
	}

	public static Engine getInstance() {
		return instance;
	}

	public ApplicationContext getCtx() {
		return ctx;
	}

	public EngineConfig getConfig() {
		return config;
	}

	public String getId() {
		return id;
	}

	public long getLoading() {
		return loading.get();
	}

	public void setLoading(long loading) {
		this.loading.set(loading);
		//NOTDO, put to redis
	}

	public long incrAndGetLoading() {
		long ret = loading.incrementAndGet();
		//NOTDO, put to redis
		return ret;
	}

	public long decrAndGetLoading() {
		long ret = loading.decrementAndGet();
		//NOTDO, put to redis
		return ret;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public void init() {
		logger.info("Initiating game engine...");

		gm = ctx.getBean("genericManager", GenericManager.class);
		um = ctx.getBean(UserManager.class);
		usm = ctx.getBean(UserSessionManager.class);

		initPacardAndGlobalCacheMap();

		String host = "";
		try {
			host = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException ex) {
		}
		id = config.getId();
		loading.set(0);
		EngineNode en = gm.get(id, EngineNode.class);
		if (en != null) {
			en.setTouchTime(new Date());
			en.setLoading(getLoading());
			gm.update(en);
			status = en.getStatus();
			statusDesc = en.getStatusDesc();
		} else {
			en = new EngineNode(id);
			en.setName(id);
			en.setLoad(config.getLoad());
			en.setHost(config.getProtocol() + host + ":" + config.getServerPort() + config.getServerContext());
			gm.save(en);
		}
	}

	/**
	 * init blank conllection for placard cache map
	 */
	private void initPacardAndGlobalCacheMap() {
		RedisImpl redisImpl = ctx.getBean("redisImpl", RedisImpl.class);
		PlacardManager pm = ctx.getBean("placardManager", PlacardManager.class);
		List<Placard> placards = pm.findMulti(new Placard());
		for (Placard p : placards) {
			try {
				redisImpl.set(PLACARD_REDIS_PREFIX + p.getId(), p);
			} catch (Exception e) {
				logger.error("Redis operate ERROR:", e);
			}
		}
		/*
		 * try { if(!redisImpl.exist(REDIS_SRH_STATUS_TRACK_KEY)) {
		 * redisImpl.set(REDIS_SRH_STATUS_TRACK_KEY,
		 * REDIS_SRH_STATUS_TRACK_VALUE);
		 * redisImpl.rpush(REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0,
		 * REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE); } } catch (Exception e) {
		 * logger.error("Redis operate ERROR:", e);
		}
		 */
	}

	public void destory() {
	}

	public Object ad(JSONObject gpo) {
		String result = null;
		try {
			String ad = gpo.getString("ad");
			if("js".equalsIgnoreCase(ad)){//gr?rq=ad&ad=js&appId=xxx
				AppConfig appConfig = new AppConfig();
				appConfig.setAppId(gpo.getString("appId"));
				AppConfig ac = gm.findObject(appConfig);
				AppConfigBase appConfigBase = new AppConfigBase(gpo.getString("appId"));
				AppConfig acb = gm.findObject(appConfigBase);

				if(acb != null){
					JSONObject jo = new JSONObject(acb.getJsonAll());
					if(jo.has("appver")){
						result = jo.get("appver").toString();
					}
				}
				if(ac != null){
					JSONObject jo = new JSONObject(ac.getJsonAll());
					if(jo.has("appver")){
						result = jo.get("appver").toString();
					}
				}
				return result;
			}
			Sale sale = new Sale();
			sale.setAd(ad);
			sale.setAppId(gpo.getString("appId"));
			sale.setIp(gpo.getString("ip"));
			if (gpo.has("pid")) {
				sale.setPid(gpo.getString("pid"));
			}
			sale.setJsonData(gpo.toString());
			if ("chartboost".equalsIgnoreCase(ad)) {//gr?rq=ad&ad=chartboost&appId=xxx&uuid=xxx&type=xxx&from=xxx&to=xxx&campaign=xxx
				String type = gpo.getString("type");
				if ("impression".equalsIgnoreCase(type) || "click".equalsIgnoreCase(type)) {
					return null;
				}
				String udid = gpo.getString("uuid");
				Device device = new Device();
				device.setUdid(udid);
				device = gm.findObject(device);
				if (device != null) {
					sale.setDeviceId(device.getId());
				}
			} else if ("flurry".equalsIgnoreCase(ad)) {//gr?rq=ad&ad=flurry&appId=xxx&user=xxx&pid=xxx
				Device device = new Device();
				device.setMacId(gpo.getString("user"));
				device = gm.findObject(device);
				if (device != null) {
					sale.setDeviceId(device.getId());
				}
			} else if ("cmge".equalsIgnoreCase(ad)) {//gr?rq=ad&ad=flurry&appId=xxx&user=xxx&pid=xxx
				if (IOSDesUtil.ntData2SignStr("nt_data=" + gpo.getString("nt_data")).equals(IOSDesUtil.decode(gpo.getString("sign")))) {
					JAXBContext context = JAXBContext.newInstance(OrderWrapper.class);
					Unmarshaller unmarshaller = context.createUnmarshaller();
					OrderWrapper orderWrapper = (OrderWrapper) unmarshaller.unmarshal(new InputSource(new StringReader(IOSDesUtil.decode(gpo.getString("nt_data")))));
					Order order = orderWrapper.getOrder();
					order.setDeviceId(order.getGameRole().split("_")[0]);
					order.setPid(order.getGameRole().split("_")[1]);
					order.setAppId(gpo.getString("appId"));
					OrderService os = ctx.getBean(OrderService.class);
					if (os.saveOrder(order)) {
						Device device = new Device();
						device.setMacId(order.getDeviceId());
						device = gm.findObject(device);
						if (device != null) {
							sale.setDeviceId(device.getId());
						}
						sale.setPid(order.getPid());
						sale.setJsonData(order.getJsonData());
						result = SUCCESS;
					} else {
						result = FAILED;
					}
				} else {
					result = FAILED;
				}
			}
			if (result == null || SUCCESS.equals(result)) {
				gm.save(sale);
			}
		} catch (Exception e) {
			//ignore
		}
		return result;
	}

	public Response gr(Request req) {
		String key = req.getKey().toUpperCase();
		if (appFree(key)) {
			try {
				return handle(req, null);
			} catch (Exception ex) {
				return new Response(Response.NO, "EXCEPTION");
			}
		}
		String appId = req.getAppId();
		if (appId == null || appId.isEmpty()) {
			return new Response(Response.NO, "NO_appId");
		}

		String appVersion = req.getAppVersion();
		if (appVersion == null || appVersion.isEmpty()) {
			return new Response(Response.NO, "NO_appVersion");
		}
		//        String deviceId = req.getDeviceId();
		//        if (deviceId == null || deviceId.isEmpty()) {
		//            return new Response(Response.NO, "NO_deviceId");
		//        }

		DataManager dm = ctx.getBean(DataManager.class);

		App app = gm.get(appId, App.class);
		if (app == null) {
			return new Response(Response.NO, "NO_APP");
		}
		boolean isDebug = req.isDebug();
		if (!isDebug && app.getStatus() != App.STATUS_NORMAL) {
			if (app.getStatus() == App.STATUS_OFF || app.getStatus() == App.STATUS_MAINTENANCE
					|| (app.getStatus() == App.STATUS_TO_MAINTENANCE && Engine.isLogin(req.getKey()))) {
				JSONObject jo = new JSONObject();
				try {
					jo.put("status", app.getStatus());
					jo.put("statusDesc", app.getStatusDesc());
				} catch (JSONException ex) {
				}
				return new Response(Response.NO, "INFO_APP", jo);
			}
		}
		//check LSEN
		if ("LSEN".equalsIgnoreCase(key)) {
			if (!config.isLsenEnable()) {
				return new Response(Response.NO, "NOT_LSEN");
			}
		}

		//check engine status
		if (!isDebug && status < EngineNode.STATUS_LIVE) {
			if (status == EngineNode.STATUS_DOWN || status == EngineNode.STATUS_MAINTENANCE
					|| (status == EngineNode.STATUS_TO_MAINTENANCE && isLogin(key))) {
				JSONObject jo = new JSONObject();
				try {
					jo.put("status", status);
					jo.put("statusDesc", statusDesc);
				} catch (JSONException ex) {
				}
				return new Response(Response.NO, "INFO_ENGINE", jo);
			}
		}

		Response resp;

		if ("LSEN".equalsIgnoreCase(key)) {
			try {
				resp = handle(req, app);
			} catch (Exception ex) {
				logger.error("LSEN Exception");
				resp = new Response(Response.NO, "EXCEPTION");
			}
			return resp;
		} else {
			if (config.isLsenEnable()) {
				logger.warn("NOT LSEN:" + key + ",app=" + app.getId());
			}
		}


		/*
		 * //check app at engine 
		 * if (!"LSEN".equalsIgnoreCase(key)) {// LSEN is not restricted to any engine 
		 * AppEngine ae = (AppEngine)gm.get(AppEngine.getId(appId, config.getEngineId()), AppEngine.class); 
		 * if (ae == null) { return new Response(Response.NO, "Not serve for this app"); } }
		 *
		 */

		Long uid = req.getUserId();
		String sid = req.getSession();
		/*if (!authFree(key) && (sid == null || sid.isEmpty())) {
            return new Response(Response.NO, "NEED_LOGIN");
        }*/

		try {
			String newSid = null;
			Long userId = null;
			if (!isLogin(key)) {
				resp = usm.checkSid(req, app);
				if (!resp.isSuccess()) {
					return resp;
				}
				userId = (Long) resp.getAttachObject();
				if (userId == null) {
					resp = um.checkEpswd(req, app);
					if (!resp.isSuccess()) {
						return resp;
					}
					userId = (Long) resp.getAttachObject();
					if (userId != null) {
						String deviceId = um.getDvid(req);
						if (sid != null && !sid.isEmpty()) {
							resp = appLogin(userId, deviceId, app, req);
							if (!resp.isSuccess()) {
								return resp;
							}
						}
						newSid = um.checkSession(userId, deviceId, app, req);
						req.setDvid(deviceId);
					}
				} else {
					if (uid != null && uid.longValue() != 0 && uid.longValue() != userId.longValue()) {
						JSONObject jo = new JSONObject();
						jo.put("userId", userId);
						logger.error("@@@@@@@@@@CONFLICT_1,uid="+uid+",sid="+sid +",userId="+userId);
						return new Response(Response.NO, "USER_CONFLICT", jo);
					}
				}
				if (userId == null) {
					resp = um.checkDevice(req, app);
					if (!resp.isSuccess()) {
						return resp;
					}
					userId = (Long) resp.getAttachObject();
					if (userId != null) {
						User user = um.get(userId, User.class);
						if (User.STATUS_FORBID == user.getStatus()) {
							return new Response(Response.NO, "USER_FORBID", new JSONObject(user.getStatusDetail()));
						}
						String deviceId = resp.getInfo();
						if (sid != null && !sid.isEmpty()) {
							resp = appLogin(userId, deviceId, app, req);
							if (!resp.isSuccess()) {
								return resp;
							}
						}
						newSid = um.checkSession(userId, deviceId, app, req);
						req.setDvid(deviceId);
					}
				} else {
					if (uid != null && uid.longValue() != 0 && uid.longValue() != userId.longValue()) {
						JSONObject jo = new JSONObject();
						jo.put("userId", userId);
						logger.error("@@@@@@@@@@CONFLICT_2,uid="+uid+",sid="+sid+",userId="+userId);
						return new Response(Response.NO, "USER_CONFLICT", jo);
					}
				}
				req.setUserId(userId);
			} else {
				req.remove("userId");
			}
			resp = handle(req, app);
			if (userId == null) {
				userId = req.getUserId();
			}
			postHandle(req, app);
			//            CommonSP.protocolMonitor(key,req,resp);
			if (newSid != null) {
				resp.setSid(newSid);
			}

			ZoneManager zm = ctx.getBean(ZoneManager.class);
			JSONObject extra = null;
			Map<String, Object> queryMap = new HashMap<String, Object>();
			queryMap.put("appId", appId);
			queryMap.put("baseId", app.getBaseId());
			queryMap.put("appVersion", appVersion);
			queryMap.put("rangeList", zm.getZonesByAppAndEngine(appId, id, app.getBaseId()));

			List<JSONObject> resultArray = (List<JSONObject>) dm.getDataByType(DataType.PLACARD, queryMap);
			if (resultArray.size() > 0) {
				if (extra == null) {
					extra = new JSONObject();
				}
				extra.put("placard", new JSONArray(resultArray));
			}
			if (!getConfig().isLsenEnable()) {
				RedisImpl redis = ctx.getBean("redisImpl", RedisImpl.class);
				Long msgNum = redis.getFromCounter(Message.redisUnreadNumKey(userId));
				if (msgNum > 0) {
					if (extra == null) {
						extra = new JSONObject();
					}
					extra.put("message", msgNum);
				}
			}
			if (extra != null) {
				resp.setExtra(extra);
			}
			return resp;
		} catch (Exception ex) {
			ex.printStackTrace();
			return new Response(Response.NO, "EXCEPTION");
		}
	}

	private Response handle(Request req, App app) throws Exception {
		String key = req.getKey().toUpperCase();
		Executor ex = getExecutor(key);
		if (ex == null) {
			return new Response(Response.NO, "NO_PROTOCOL");
		} else {
			Response resp = ex.exec(req, app);
			return resp;
		}
	}

	private void postHandle(Request req, App app) {
		String key = req.getKey().toUpperCase();
		Executor ex = getExecutor(key);
		if (ex != null) {
			if (ex instanceof SyncExecutor) {
				SyncExecutor sync = (SyncExecutor) ex;
				sync.onAction(req, app);
			}
		}
	}

	private Executor getExecutor(String key) {
		return ctx.getBean(key, Executor.class);
	}

	//app login
	private Response appLogin(Long userId, String deviceId, App app, Request req) {
		boolean withAppData = false;
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		return component.login(userId, deviceId, withAppData, req);
	}

	private static boolean appFree(String key) {
		if ("RTCR".equalsIgnoreCase(key) || "RTUP".equalsIgnoreCase(key)) {
			return true;
		} else {
			return false;
		}
	}

	private static boolean authFree(String key) {
		if ("USLG".equalsIgnoreCase(key) || "SWUS".equalsIgnoreCase(key) || "UPNF".equalsIgnoreCase(key)
				|| "MRGM".equalsIgnoreCase(key) || "LSSL".equalsIgnoreCase(key) || "LGSL".equalsIgnoreCase(key)
				|| "LSEN".equalsIgnoreCase(key) || "CKRV".equalsIgnoreCase(key) || "UPDV".equalsIgnoreCase(key)
				|| "PSTK".equalsIgnoreCase(key) || "PSTU".equalsIgnoreCase(key)) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean isLogin(String key) {
		if ("USLG".equalsIgnoreCase(key) || "SWUS".equalsIgnoreCase(key)) {
			return true;
		} else {
			return false;
		}
	}

	public Map<String, Object> getBaseIdCacheMap() {
		return baseIdCacheMap;
	}

	public void setBaseIdCacheMap(Map<String, Object> baseIdCacheMap) {
		this.baseIdCacheMap = baseIdCacheMap;
	}

	public Map<String, Object> getAppIdCacheMap() {
		return appIdCacheMap;
	}

	public void setAppIdCacheMap(Map<String, Object> appIdCacheMap) {
		this.appIdCacheMap = appIdCacheMap;
	}
}
