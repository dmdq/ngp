package com.ngnsoft.ngp.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;

import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.job.EventBakJob;
import com.ngnsoft.ngp.job.EventBakTask;
import com.ngnsoft.ngp.service.EventSchedulerService;
import com.ngnsoft.ngp.util.DateUtil;

@Service("schedulerService")
public class EventSchedulerServiceImpl implements EventSchedulerService {
	
	@Override
	public void schedulerJob(Event event, Job job)throws SchedulerException {
		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();
		
		String uid = UUID.randomUUID().toString();
		String jobName = uid + "Name";
		String jobGroup = uid + "Group";
		String triggerName = uid + "TriggerName";
		String triggerGroup = uid + "TriggerGroup";

		JobDetail jobDetail = JobBuilder.newJob(job.getClass()).withIdentity(jobName, jobGroup)
				.build();

		Long time = job instanceof EventBakJob ? event.getBakStartTime() : event.getRefreshStartTime();
		
		time = time + event.getCurTotalHours() * 60 * 60 * 1000;
		
		Trigger trigger = TriggerBuilder.newTrigger()
				.withIdentity(triggerName, triggerGroup)
				.withSchedule(SimpleScheduleBuilder.simpleSchedule()
		                .withIntervalInHours(event.getCurTotalHours()).withRepeatCount(0))
		        .startAt(new Date(time))
				.build();
		
		sched.start();
		sched.scheduleJob(jobDetail, trigger);
	}
	
	@Override
	public void schedulerTask(Event event, TimerTask task)
			throws Exception {
		Timer timer = new Timer();
		long startTime = task instanceof EventBakTask ? event.getBakStartTime() : event.getRefreshStartTime();
		long delay = 60000;
		timer.schedule(task, delay);
	}
	
	
	public static void main(String[] args) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date = sdf.parse("2013-12-09 00:00:00");
		System.out.println(DateUtil.getDawnDay(3));
		System.out.println(DateUtil.getDawnDay(3)+(72 * 60 * 60 * 1000));
		System.out.println(new Date(1390348800000l));
		System.out.println(72 * 60 * 60 * 1000);
	}
	
}
