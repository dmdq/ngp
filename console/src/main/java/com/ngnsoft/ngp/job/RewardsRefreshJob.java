package com.ngnsoft.ngp.job;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
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

@Component("rewardsRefreshJob")
public class RewardsRefreshJob extends BaseJob {
	
	private Logger LOGGER = Logger.getLogger(RewardsRefreshJob.class);
	
	private GenericManager gm;
	
	private EventSchedulerService eventScheduler;
	
	private EventBakJob eventBakJob;
	
	private RewardsRefreshJob rewardsRefreshJob;

	@Override
	public void executeWithBean(JobExecutionContext context) {
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
				event.setCurType(event.getCurType());
				event.setCurStatus(event.getCurStatus());
			} else {
				event.setCurUnit(event.getNextUnit());
				event.setCurTotalHours(event.getNextTotalHours());
				event.setCurType(event.getNextType());
				event.setCurStatus(event.getNextStatus());
			}
			event.setNextStatus(event.getCurStatus());
			event.setRefreshStartTime(DateUtil.getDawnDay(1));
			event.setBakStartTime(event.getRefreshStartTime() - 30*1000);
			if(event.getCurStatus() == Event.CUR_STATUS_NORMAL) {
				eventScheduler.schedulerJob(event, eventBakJob);
				eventScheduler.schedulerJob(event, rewardsRefreshJob);
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
	public void initBeans(JobExecutionContext context)  throws Exception {
		ApplicationContext applicationContext =  WebApplicationContextUtils.getWebApplicationContext(
		        ContextLoaderListener.getCurrentWebApplicationContext().getServletContext()
		    );
		gm = applicationContext.getBean("genericManager", GenericManager.class);
		eventScheduler = applicationContext.getBean(EventSchedulerService.class);
		eventBakJob = applicationContext.getBean("eventBakJob", EventBakJob.class);
		rewardsRefreshJob = applicationContext.getBean("rewardsRefreshJob", RewardsRefreshJob.class);
	}
	
}
