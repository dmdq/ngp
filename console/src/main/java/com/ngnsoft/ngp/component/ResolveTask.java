package com.ngnsoft.ngp.component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.coc.model.BattleLog;
import com.ngnsoft.ngp.component.coc.model.CocKeep;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.slots.model.SlotsKeep;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.PromoCount;
import com.ngnsoft.ngp.model.UserFriend;
import com.ngnsoft.ngp.service.AsyncOperManager;
import com.ngnsoft.ngp.service.PromoCountManager;
import com.ngnsoft.ngp.service.impl.UserDataManagerImpl;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.ConvertUtil;
import com.ngnsoft.ngp.util.DateUtil;

@Component("resolveTask")
public class ResolveTask {
	
	private static Logger LOGGER = Logger.getLogger(ResolveTask.class);
	
	private static String PROMO_KEY_PREFIX = "PROMO_";
	
	private static final long ROBOT_IDENTIFY_VAL = 1000000l;
	
	private static final String COC_KEEP_BASEID = "100002";
	
	private static final String SLOTS_KEEP_BASEID = "100004"; 
	
	@Autowired
	private PromoCountManager promoCountManager;
	
	@Autowired
	private RedisImpl redisImpl;
	
	@Autowired
	private AsyncOperManager asyncOperManager;
	
	public void exportData() {
		Long count =  promoCountManager.countByNameQuery("countLogInitTrophy", new HashMap(), BattleLog.class);
		int index = 0;
		LOGGER.debug("import data starting ---------------------------------------------------------");
		while(index < count) {
			LOGGER.debug("left data count : " + (count - index));
			Pagination page = new Pagination();
			page.setCurrent_page(index);
			page.setItems_per_page(100);
			List<BattleLog> battles = promoCountManager.findByNamedQuery("findBattlesInitTrophy", new HashMap(), page, BattleLog.class);
			for(BattleLog battle : battles) {
				JSONObject battleData = JSONObject.parseObject(battle.getJsonData());
				JSONObject baseDataJson = battleData.containsKey("BaseData") ? battleData.getJSONObject("BaseData") : new JSONObject();
				int cup = battleData.getJSONObject("RivalData").getJSONObject("Key00000").containsKey("Cup") ? battleData.getJSONObject("RivalData").getJSONObject("Key00000").getIntValue("Cup") : 0;
				BattleLog newBattle = new BattleLog();
				newBattle.setId(battle.getId());
				newBattle.setTrophy(cup);
				newBattle.setBaseData(baseDataJson.toJSONString());
				try {
					asyncOperManager.updateData(newBattle);
				} catch (Exception e) {
				}
			}
			index += 100;
		}
		LOGGER.debug("import data successfully ---------------------------------------------------------");
	}
	
	public void persistPromoCount() {
		//select last update date time from promo_count
		Date lastCountDate = promoCountManager.getLastCountDate();
		if(lastCountDate != null) {
			List<String> dateIntervals = DateUtil.getDateInterval(lastCountDate, DateUtil.getBreforeDate(new Date(), -1));
			for(String date : dateIntervals) {
				String pattern = formatRegex("*" + PROMO_KEY_PREFIX + date + "*");
				persistByPattern(pattern);
			}
			
		} else {
			String pattern = "*" + PROMO_KEY_PREFIX + "*";
			persistByPattern(pattern);
		}
	}
	
	/**
	 * refresh information for robot
	 */
	public void refreshRobotForDragon() {
		Integer start = 0;
		Long skip = 0l;
		Map paramMap = new HashMap();
		paramMap.put("robotId", ROBOT_IDENTIFY_VAL);
		Long count = promoCountManager.countByNameQuery("countRobotByMap", paramMap, DragonData.class);
		skip = count/10 == 0 ? 2 : count/10;
		paramMap.put("skip", skip);
		if(count <= skip) {
			start = 0;
		} else {
			long index = count - skip >= 0 ? count - skip : 0;
			start = ConvertUtil.generateRandom(0, (int)index);
		}
		paramMap.put("start", start);
		if(count > 0) {
			List<DragonData> dragonDatas = promoCountManager.findByNamedQuery("find_robot_random", paramMap, DragonData.class);
			for(DragonData dragonData : dragonDatas) {
				dragonData.setActionTime(new Date());
				promoCountManager.update(dragonData);
			}
		}
		//refresh robot who didn't login within three days
		paramMap.put("sysDate", new Date());
		paramMap.put("intervalDay", 3);
		promoCountManager.update("refreshRobotNotRandom", paramMap, DragonData.class);
	}
	
	/**
	 * automatic confirm the friends
	 */
	public void autoConfirmFriend() {
		Map paramMap = new HashMap();
		paramMap.put("robotId", ROBOT_IDENTIFY_VAL);
		promoCountManager.update("update_confirm_friend_automatic", paramMap, UserFriend.class);
	}
	
	private void persistByPattern(String pattern) {
		try {
			Set<String> set = redisImpl.keys(pattern);
			List<String> keys = new ArrayList<String>(set);
			Collections.sort(keys);
			for(String key : keys) {
				Long curCount = redisImpl.incr(key, 0);
				Long promoId = Long.valueOf(key.substring(17));
				Date countDate = DateUtil.parseDate(key.substring(6, 16));
				PromoCount promoCount = new PromoCount();
				promoCount.setPromoId(promoId);
				promoCount.setClickCount(curCount);
				promoCount.setCountDate(countDate);
				promoCountManager.save(promoCount);
			}
			redisImpl.clear(set);
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
	}
	
	public void printRedisSpecialKeys() {
		try {
			LOGGER.debug("track's status : " + redisImpl.get(Engine.REDIS_SRH_STATUS_TRACK_KEY));
			LOGGER.debug(redisImpl.exist(Engine.REDIS_GLOBAL_SRN_SYNC_QUEUE_KEY));
			LOGGER.debug("The count of coc srh is " + redisImpl.incr(UserDataManagerImpl.SRH_CONCURRENCY_COUNT_KEY, 0));
			Long succCount = redisImpl.incr(UserDataManagerImpl.SRH_CONCURRENCY_SUCC_COUNT_KEY, 0);
			Long failedCount = redisImpl.incr(UserDataManagerImpl.SRH_CONCURRENCY_FAILED_COUNT_KEY, 0);
			LOGGER.debug("The best solution success ratio is " + ConvertUtil.convertDouble2Ratio(succCount*1.0, succCount*1.0 + failedCount*1.0));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String formatRegex(String pattern) {
		return pattern.replaceAll("\\-", "\\\\-");
	}
	
	/**
	 * export user's data by timer
	 */
	public void cocKeepJob(){
		try {
			App app = new App();
			app.setBaseId(COC_KEEP_BASEID);
			List<App> apps = promoCountManager.findMulti(app);
			List<String> rangeList = new ArrayList<String>();
			for (App app2 : apps) {
				rangeList.add(app2.getId());
			}
			
			Map map = new HashMap();
			map.put("rangeList", rangeList);
			map.put("createTime", new Date());
			promoCountManager.save("save_cocKeep_createTime_and_logNumber", map, CocKeep.class);
			
			promoCountManager.update("update_cocKeep_insert_number", map, CocKeep.class);
			promoCountManager.update("update_cocKeep_tomorrow_keep", map, CocKeep.class);
			promoCountManager.update("update_cocKeep_seven_keep", map, CocKeep.class);
			promoCountManager.update("update_cocKeep_fifteen_keep", map, CocKeep.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * export user's data by timer
	 */
	public void slotsKeepJob(){
		try {
			App app = new App();
			app.setBaseId(SLOTS_KEEP_BASEID);
			List<App> apps = promoCountManager.findMulti(app);
			List<String> rangeList = new ArrayList<String>();
			for (App app2 : apps) {
				rangeList.add(app2.getId());
			}
			Map map = new HashMap();
			map.put("rangeList", rangeList);
			map.put("createTime", new Date());
			promoCountManager.save("save_slotsKeep_createTime_and_logNumber", map, SlotsKeep.class);
			
			promoCountManager.update("update_slotsKeep_insert_number", map, SlotsKeep.class);
			promoCountManager.update("update_slotsKeep_tomorrow_keep", map, SlotsKeep.class);
			promoCountManager.update("update_slotsKeep_seven_keep", map, SlotsKeep.class);
			promoCountManager.update("update_slotsKeep_fifteen_keep", map, SlotsKeep.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DateUtil.getWeekNum(new Date()));
	}
}
