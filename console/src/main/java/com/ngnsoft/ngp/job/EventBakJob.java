package com.ngnsoft.ngp.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ngnsoft.ngp.component.slots.SlotsComponent;
import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.model.SlotsIntegralHistory;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.sp.redis.RedisImpl;

@Component("eventBakJob")
public class EventBakJob extends BaseJob {
	
	private Logger LOGGER = Logger.getLogger(EventBakJob.class);
	
	private GenericManager gm;
	
	private RedisImpl redisImpl;
	
	@Override
	public void executeWithBean(JobExecutionContext context) {
		try {
			LOGGER.debug("Bak event start.....");
			bakSlotsIntegralBoard();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void bakSlotsIntegralBoard() throws Exception {
		Event event = gm.findObject(new Event());
		
		Map params = new HashMap();
		params.put("week", event.getPeriod());
		//backup data this week
		gm.save("save_slots_history_from_slots_data", params, SlotsIntegralHistory.class);
		
		redisImpl.del(SlotsComponent.REDIS_SLOTS_LAST_WEEK_BASE_ID_TOP_KEY);
		redisImpl.del(SlotsComponent.REDIS_SLOTS_BASE_ID_TOP_KEY);
		
	}

	@Override
	public void initBeans(JobExecutionContext context) throws Exception {
		ApplicationContext applicationContext =  WebApplicationContextUtils.getWebApplicationContext(
			        ContextLoaderListener.getCurrentWebApplicationContext().getServletContext()
			    );
		gm = applicationContext.getBean("genericManager", GenericManager.class);
		redisImpl = applicationContext.getBean("redisImpl", RedisImpl.class);
	}

	
}
