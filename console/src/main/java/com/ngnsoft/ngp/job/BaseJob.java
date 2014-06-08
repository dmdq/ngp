package com.ngnsoft.ngp.job;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public abstract class BaseJob implements Job {
	
	private Logger LOGGER = Logger.getLogger(BaseJob.class);
	
	public abstract void initBeans(JobExecutionContext context)  throws Exception;
	
	public abstract void executeWithBean(JobExecutionContext context) throws JobExecutionException;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		try {
			initBeans(context);
			executeWithBean(context);
		} catch (Exception e) {
			LOGGER.error("init bean error :", e);
		}
	}

}
