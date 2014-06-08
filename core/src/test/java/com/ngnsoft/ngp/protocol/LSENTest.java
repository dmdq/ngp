package com.ngnsoft.ngp.protocol;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.Request;
import com.ngnsoft.ngp.component.bingo.BingoComponent;
import com.ngnsoft.ngp.component.bingo.service.impl.BingoManagerImpl;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.service.AsyncOperManager;
import com.ngnsoft.ngp.service.EngineNodeManager;
import com.ngnsoft.ngp.service.UserAppDataManager;
import com.ngnsoft.ngp.service.UserManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.ConvertUtil;

public class LSENTest extends ProtocolBaseTest {

	@Autowired
	private EngineNodeManager enm;

	@Autowired
	@Qualifier("userAppDataManager")
	private UserAppDataManager  userDataManager;

	@Autowired
	private RedisImpl redisImpl;
	
	@Autowired
	private AsyncOperManager asyncOperManager;
	
	private String[] names = new String[]{"Allen","Ban","Bella","Chih Chieh","Daegwang","Dali","Dong-Lyeor","Eva","Hao","Jing","Joshua","Jun","Karen","Max","Min",
			"Neil","Owen","Qian","Rain","Rex","Ricky","Ryan","Seung-Min","Sven","XiaoZi","Vincent","King","Randy","Fax","Fire"};
	private String[] ids = new String[]{"100004040870451","100004240264795","100001271401143","1201852980","100001117917120",
			"100002604658928","100001323600532","100002304360997","100006711551856","100003622020690","100001879599437","100003959442877","100001575379383"
			,"100002051958798","100004625851741","100003519057127","100006972483748","100003589201162","730727385","100001823290976",
			"100002495045665","100004206306407","100000444448689","100002802090098","100002812390098","100002802033321","10000282222111","1000028023895"
			,"10000280268528","10000280147258"};
	public static final String BINGO_REDIS_PREFIX = BingoComponent.APP_BASE_ID + "_BINGO_";
	
	//bingo start time
    public static final String BINGO_START_TIME_REDIS_PREFIX = BINGO_REDIS_PREFIX + "TIME_";
    //total number of the card
    public static final String BINGO_CNUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "CNUM_";
    //total number of the ready user
    public static final String BINGO_READY_NUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "RNUM_";
    //total number of the left bingo
    public static final String BINGO_NUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "NUM_";
    //total number of the bingo
    public static final String BINGO_TOTAL_NUM_REDIS_PREFIX = BINGO_REDIS_PREFIX + "TOTAL_NUM_";
    //the top list key
    public static final String BINGO_TOP_LIST_REDIS_PREFIX = BINGO_REDIS_PREFIX + "TOPLIST_";
    //the user with bingo key
    public static final String BINGO_ROLE_DATA_REDIS_PREFIX = BINGO_REDIS_PREFIX + "ROLE_DATA_";
    //all of the users in the room
    public static final String BINGO_MEMBER_DATA_REDIS_PREFIX = BINGO_REDIS_PREFIX + "MEMBER_DATA_";
    //all of the activities in the room
    public static final String BINGO_ACTIVITY_REDIS_PREFIX = BINGO_REDIS_PREFIX + "ACTIVITY_";
    //all of the chats in the room
    public static final String BINGO_CHAT_REDIS_PREFIX = BINGO_REDIS_PREFIX + "CHAT_";
    //the digits queue for every game
    public static final String BINGO_QUEUE_REDIS_PREFIX = BINGO_REDIS_PREFIX + "QUEUE_";

	@Override
	public void testProtocol() throws Exception {
		
		Engine engine = Engine.getInstance();
		
		/*List<SlotsData> slotsDatas = userDataManager.findMulti(new SlotsData());
		
		for (int i = 0; i < slotsDatas.size(); i++) {
			SlotsData slotsData = slotsDatas.get(i);
			JSONObject jsonObj = new JSONObject(slotsData.getJsonData());
			slotsData.setNick(jsonObj.getString("userName"));
			slotsData.setCoins(Long.parseLong(jsonObj.getJSONObject("userData").getString("coins")));
			slotsData.setLevel(jsonObj.getJSONObject("userData").getInt("level"));
			userDataManager.update(slotsData);
		}*/
		
		try {
			redisImpl.del("123");
			redisImpl.incr("123", 3);
			redisImpl.decr("123");
//			redisImpl.incr("123", 4);
			Set<byte[]> set4 = redisImpl.zRange(BINGO_MEMBER_DATA_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			System.out.println(redisImpl.incr("123", 0));
			redisImpl.zadd("345", 1, 2);
			redisImpl.zadd("345", 1, 2);
			long count = redisImpl.zCountByScore("345", 1, 1);
			redisImpl.flushDB();
 			if(redisImpl.exist(BingoComponent.APP_BASE_ID + "_BINGO_" + "MEMBER_DATA_" + "BINGO_ROOM_SCHOOL_" + 1049948)){
				System.out.println("123123");
			}
//			redisImpl.del(RobotService.BINGO_ROBOT_LIST_ALL_REDIS_KEY);
			Set<String> se2t = redisImpl.keys("*" + BingoManagerImpl.BINGO_REDIS_PREFIX + "*");
			redisImpl.clear(se2t);
			redisImpl.zRemRangeByScore(BINGO_ACTIVITY_REDIS_PREFIX + "BINGO_ROOM_SCHOOL", 0, System.currentTimeMillis());
			redisImpl.set(BINGO_START_TIME_REDIS_PREFIX + "BINGO_ROOM_SCHOOL", -1, System.currentTimeMillis() + 200 * 1000);
			
			redisImpl.del(BINGO_START_TIME_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_CNUM_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_READY_NUM_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_NUM_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_TOTAL_NUM_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_TOP_LIST_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_MEMBER_DATA_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			
			
			redisImpl.del(BINGO_CHAT_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			redisImpl.del(BINGO_QUEUE_REDIS_PREFIX + "BINGO_ROOM_SCHOOL");
			
			
			
			/*Set<byte[]> set3 = redisImpl.zRangeByScore(BingoComponent.APP_BASE_ID + "_BINGO_" + "MEMBER_DATA_" + "BINGO_ROOM_SCHOOL_" + 1049948, 1049948, 1049948);
			redisImpl.zRemRangeByScore(BingoComponent.APP_BASE_ID + "_BINGO_" + "MEMBER_DATA_" + "BINGO_ROOM_SCHOOL_" + 1049948, 1049948, 1049948);
			
			Activity activity = new Activity();
			activity.setTime(new Date().getTime());
			activity.setType(1);
			activity.setRid(1l);
			activity.setData("{}");
			
			Activity activity2 = new Activity();
			activity2.setTime(new Date().getTime());
			activity2.setData("{asdas}");
			activity2.setType(1);
			activity2.setRid(2l);
			
			
			redisImpl.zadd("123", activity.getRid(), activity);
			
			redisImpl.zadd("123", activity2.getRid(), activity2);
			
			redisImpl.zRemRangeByScore("123", 1, 1);
			
			Set<byte[]> set1 = redisImpl.zRange("123");
			Set<byte[]> set = redisImpl.zRangeByScore("123", 0, 2);
			List<Activity> activities = new ArrayList<Activity>();
			for(byte[] entry : set) {
				Activity entity = (Activity)RedisUtil.getObjectFromBytes(entry);
				activities.add(entity);
			}
			System.out.println(activities);
			
			redisImpl.zadd("1231", 1, "1231");
			
			redisImpl.zadd("1231", 2, "4545");
			redisImpl.zRemRangeByScore("1231", 1, 1);
			Set<byte[]> set2 = redisImpl.zRangeByScore("1231", 1, 2);
			for(byte[] entry : set2) {
				String entity = (String)RedisUtil.getObjectFromBytes(entry);
				System.out.println(entity);
			}*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		/*Engine engine = Engine.getInstance();
		
		DataInputStream in = new DataInputStream(new FileInputStream("C:/Users/NextGen/Desktop/candy20131213.txt"));
		String readStr = null;
		while((readStr = in.readLine()) != null) {
			Product product = new Product();
			product.setProductId(readStr.split("-")[0]);
			product.setDescription(readStr.split("-")[3]);
			product.setAmount(Double.parseDouble(readStr.split("-")[1]));
			product.setPicture(readStr.split("-")[4]);
			product.setQuantity(1L);
			product.setName(readStr.split("-")[2]);
			product.setDbName("candy");
			userDataManager.update(product);
		}*/
		
		/*Map params = new HashMap();
		params.put("week", 201347);
		//backup data this week
		try {
//			userDataManager.save("save_slots_history_from_slots_data", params, SlotsIntegralHistory.class);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		long userId = 100034;
		for(int i = 0; i < names.length; i++) {
			String avatar = "https://graph.facebook.com/" + ids[i] + "/picture?width=100&height=100";
			String jsonData = FileCopyUtils.copyToString(new FileReader("e:/data.txt"));
			JSONObject biz = new JSONObject(jsonData);
			JSONObject activity = new JSONObject("{\"activityData\":{}}");
			boolean flag = true;
			int min = 0;
			int integral = 0;
			String activityPuzzles = "";
			for(int j = 0; j < 6; j ++) {
				int val = ConvertUtil.generateRandom(0, 3);
				if(val == 0) {
					flag = false;
				}
				if(min == 0 || min > val) {
					min = val;
				}
				integral += val*(10 + j*5);
				if(j == 0) {
					activityPuzzles += val;
				} else {
					activityPuzzles += "|" + val;
				}
			}
			if(flag) {
				integral += min * 1000;
			}
			
			
			
			activity.getJSONObject("activityData").put("activityScore", integral);
			activity.getJSONObject("activityData").put("activityPuzzles", activityPuzzles);
			activity.getJSONObject("activityData").put("rewards", 0);
			biz.getJSONObject("userData").put("level", ConvertUtil.generateRandom(5, 29));
			biz.put("genter", ConvertUtil.generateRandom(0, 1));
			biz.put("avatar", avatar);
			biz.put("facebookName", names[i]);
			SlotsData newUserAppData = new SlotsData();
			Date curDate = new Date();
			newUserAppData.setEligible(1);
			newUserAppData.setIntegral(integral);	
			newUserAppData.setUserId(userId);
			newUserAppData.setJsonData(biz.toString());
			newUserAppData.setActivityData(activity.toString());
			newUserAppData.setTouchTime(curDate);
			newUserAppData.setActionTime(curDate);
			try {
				userDataManager.save(newUserAppData);
			} catch (Exception e) {
				e.printStackTrace();
			}
			userId ++;
		}*/
		
		
//		redisImpl.del(SlotsComponent.REDIS_SLOTS_LAST_WEEK_BASE_ID_TOP_KEY);
		/*for(int i = 0; i < 50; i++) {
			SynchThread thread = new SynchThread();
			new Thread(thread).start();
		}*/
//		UserData data = (UserData)redisImpl.get("13212dasdaqsda");
//		System.out.println(data);
		/*DataInputStream in = new DataInputStream(new FileInputStream("C:/Users/NextGen/Desktop/tangguo.txt"));
		String readStr = null;
		while((readStr = in.readLine()) != null) {
			if (readStr.split("_")[1].equals("013")) {
				for (int i = 0; i < 2; i++) {
					Sale sale = new Sale(null, "cmge", "com.toktoo.candymatchCMGE", readStr.split("_")[0], "003", "{}", "127.0.0.1");
					userDataManager.save(sale);
				}
			} else {
				Sale sale = new Sale(null, "cmge", "com.toktoo.candymatchCMGE", readStr.split("_")[0], readStr.split("_")[1], "{}", "127.0.0.1");
				userDataManager.save(sale);
				Sale sale1 = new Sale(null, "cmge", "com.toktoo.candymatchCMGE", readStr.split("_")[0], "005", "{}", "127.0.0.1");
				userDataManager.save(sale1);
			}
			System.out.println(readStr);
		}*/
		
		/*Request req = new Request(); 
		req.setAppId("553777703");
		req.setAppVersion("1.0.0");
		req.setDeviceId("fcy-test2");
		req.setIP("192.168.0.60"); 
		req.setKey("LSEN");

		App app = (App)enm.get(req.getAppId(), App.class);
		Map<Zone, List<EngineNode>> zoneEnginesMap = enm.getEngineNodeByApp(app);
		AppComponent component = AppComponentFactory.getComponent(app.getBaseId());
		Response response = component.lsen(app, zoneEnginesMap, req);*/


		/*Request req = new Request();
		req.put("sid", "c423e389-42f7-4e20-99bb-5bb99a24b71e");
		req.setUserId(123123l);
		req.setAppId("111111");
		req.setAppVersion("1.2.0");
		JSONObject biz = null;
		try {
			String jsonData = FileCopyUtils.copyToString(new FileReader("e:/json.txt"));
			biz = new JSONObject(jsonData);
		} catch (Exception e) {
			e.printStackTrace();
		}
		req.put("biz", biz);
		try {
			Response response = userDataManager.executeUserAppDataByAction(req);
			System.out.println(response.getOutput());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
//		Engine engine = Engine.getInstance();
//		redisImpl.set("US_s1_2222_123123123", 200, new UserSession());
		/*Thread t = new Thread(new MyThread());
		Set<String> keys = redisImpl.keys("*US_*");
		System.out.println(keys.size());
		redisImpl.clear(keys);
		Set<String> keys = redisImpl.keys("*" + UserSessionManagerImpl.SESSION_KEY_PREFIX + Engine.getInstance().getId() + "_*");
		int loading = keys.size();
		UserData data = new UserData();
		data.setUserId(1l);
		data.setJsonData("12312312");
		int s = userDataManager.update(data);*/
//		redisImpl.del(CocComponent.REDIS_COC_BASE_ID_TOP_KEY);
		
		//		t.start();
//		redisImpl.flushDB();
//		redisImpl.rpush(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0, new String("123"));
//		redisImpl.set("123123", "234234");
//		System.out.println(redisImpl.exist("123123"));
//		redisImpl.rpush("key1111", 0, new String("35434"));
//		redisImpl.set(Engine.REDIS_SRH_STATUS_TRACK_KEY, Engine.REDIS_SRH_STATUS_TRACK_VALUE);
//				System.out.println(redisImpl.keys("*")+"------------------------------");
//				redisImpl.clear(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY);
//				System.out.println(redisImpl.keys("*")+"------------------------------");
		
//		System.out.println(redisImpl.get(Engine.REDIS_SRH_STATUS_TRACK_KEY + "123123123123123123"));
		
//		System.out.println(redisImpl.exist(Engine.REDIS_SRH_STATUS_TRACK_KEY));
//		System.out.println(redisImpl.bRPop(5,Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY));
//		System.out.println(redisImpl.keys("*" + CocComponent.BE_TEMP_SRH_TARGET_ID_PREFIX + "*").size());
//		System.out.println(redisImpl.incr("34567"));
//		System.out.println(redisImpl.getFromCounter("34567"));
//		
//		redisImpl.del(Engine.REDIS_SRH_STATUS_TRACK_KEY);
//		
//		redisImpl.del(Engine.REDIS_SRH_STATUS_TRACK_KEY);
		
//				redisImpl.set(Engine.REDIS_SRH_STATUS_TRACK_KEY, Engine.REDIS_SRH_STATUS_TRACK_VALUE);
//				redisImpl.bRPop(30, Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY);
//				redisImpl.rpush(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0, Engine.REDIS_GLOBAL_SRH_SYNC_QUEUE_VALUE);
//				System.out.println(redisImpl.exist(Engine.REDIS_SRH_STATUS_TRACK_KEY));
//				System.out.println(redisImpl.exist(Engine.REDIS_SRH_STATUS_TRACK_KEY));
//		Object obj = redisImpl.bRPop(5, Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY);
//		System.out.println(obj + "---------------------------------");
//		System.out.println(redisImpl.get(Engine.REDIS_SRH_STATUS_TRACK_KEY));
		
		/*redisImpl.set("dragon_help_" + 123, "123");
		System.out.println(redisImpl.keys("*")+"------------------------------");
		String pattern = "*" + DragonComponent.REDIS_DRAGON_HELP_ID_PREFIX + "*";
		try {
			Set<String> set = redisImpl.keys(pattern);
			redisImpl.clear(set);
		} catch (Exception e) {
		}
//		Thread.sleep(3000);
		System.out.println(redisImpl.keys("*")+"------------------------------");*/
		
		/*PushNotify pn = new PushNotify();
        pn.setMessage("123123123123");
        pn.setBadge(1);
		Notification notif = new Notification(1000153l, "686390633", pn);
		
		try {
			asyncOperManager.addNotification(notif);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		Map paramMap = new HashMap();
		paramMap.put("attacker", 100015);
		paramMap.put("defenser", 100020);
		userDataManager.update("unlockUser", paramMap, UserData.class);*/

	}
	
	
	class SynchThread implements Runnable {
		@Override
		public void run() {
			Engine engine = Engine.getInstance();
			UserManager um = engine.getCtx().getBean(UserManager.class);
			Request req = new Request();
			req.setMacId("123123123");
			req.setAppId("123123123");
			req.setIdfa("123123123");
			req.setSession("123123123");
			App app = new App();
			app.setId("123123123");
			um.checkDevice(req, app);
		}
	}

	class MyThread implements Runnable {

		@Override
		public void run() {
			try {
				Thread.sleep(2000);
				System.out.println("++++++++++++++++++++++++++");
				try {
					redisImpl.rpush(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY, 0, new String("123"));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) {
		for(int j = 0; j < 17; j ++) {
			System.out.println(ConvertUtil.generateRandom(0, 2));
		}
	}

}
