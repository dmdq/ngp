package com.ngnsoft.ngp.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.component.slots.model.SlotsData;
import com.ngnsoft.ngp.service.EventSchedulerService;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.service.impl.SlotsDataManagerImpl;
import com.ngnsoft.ngp.util.DateUtil;

@Component("rewardsRefreshTask")
public class RewardsRefreshTask extends BaseTask {
	
	private Logger LOGGER = Logger.getLogger(RewardsRefreshTask.class);
	
	private GenericManager gm;
	
	private EventSchedulerService eventScheduler;
	
	private EventBakTask eventBakTask;
	
	private RewardsRefreshTask rewardsRefreshTask;

	@Override
	public void executeWithBean() {
		try {
			LOGGER.debug("refresh reqards start...");
			resetSlotsData();
			
			//start another
			Event event = gm.findObject(new Event());
			event.setPeriod(event.getPeriod() + 1);
			event.setLastType(event.getCurType());
			if(event.getNextUnit() == null) {
				event.setCurUnit(event.getCurUnit());
				event.setCurTotalHours(event.getCurTotalHours());
			} else {
				event.setCurUnit(event.getNextUnit());
				event.setCurTotalHours(event.getNextTotalHours());
			}
			event.setNextStatus(event.getCurStatus());
			event.setRefreshStartTime(DateUtil.getDawnDay(1));
			event.setBakStartTime(event.getRefreshStartTime() - 30*1000);
			if(event.getCurStatus() == Event.CUR_STATUS_NORMAL) {
				eventScheduler.schedulerTask(event, eventBakTask);
				eventScheduler.schedulerTask(event, rewardsRefreshTask);
			}
			gm.update(event);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void resetSlotsData() throws Exception {
		//update integral into 0
		Map params = new HashMap();
		params.put("activityData", SlotsDataManagerImpl.ACTIVITY_INIT_DATA);
		gm.update("update_integral_into_zero", params, SlotsData.class);
	}

	@Override
	public void initBeans() throws Exception {
		ApplicationContext applicationContext =  WebApplicationContextUtils.getWebApplicationContext(
		        ContextLoaderListener.getCurrentWebApplicationContext().getServletContext()
		    );
		gm = applicationContext.getBean("genericManager", GenericManager.class);
		eventScheduler = applicationContext.getBean(EventSchedulerService.class);
		eventBakTask = applicationContext.getBean("eventBakTask", EventBakTask.class);
		rewardsRefreshTask = applicationContext.getBean("rewardsRefreshTask", RewardsRefreshTask.class);
	}
	
	public static void main(String[] args) {
		Integer s1 = 3;
		Integer s2 = 3;
		System.out.println(s1 == s2);
		
	}
	
}
