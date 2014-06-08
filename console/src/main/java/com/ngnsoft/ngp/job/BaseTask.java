package com.ngnsoft.ngp.job;

import java.util.TimerTask;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionException;

public abstract class BaseTask extends TimerTask {
	
	private Logger LOGGER = Logger.getLogger(BaseTask.class);
	
	public abstract void initBeans() throws Exception;
	
	public abstract void executeWithBean() throws JobExecutionException;
	
	@Override
	public void run() {
		try {
			initBeans();
			executeWithBean();
		} catch (Exception e) {
			LOGGER.error("init bean error :", e);
		}
	}

}
