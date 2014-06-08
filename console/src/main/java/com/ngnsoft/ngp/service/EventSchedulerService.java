package com.ngnsoft.ngp.service;

import java.util.TimerTask;

import org.quartz.Job;
import org.quartz.SchedulerException;

import com.ngnsoft.ngp.component.slots.model.Event;



public interface EventSchedulerService {
	
	void schedulerJob(Event event, Job job) throws SchedulerException;
	
	void schedulerTask(Event event, TimerTask task) throws Exception;
	
}
