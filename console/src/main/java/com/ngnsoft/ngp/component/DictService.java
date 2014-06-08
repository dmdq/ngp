package com.ngnsoft.ngp.component;

import java.util.Calendar;
import java.util.Date;

import javax.annotation.PostConstruct;

import org.quartz.Job;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.service.EventSchedulerService;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.util.DateUtil;

@Component("dictService")
public class DictService {
	
	@Value("${file.server}")
	private String fileServerVal;
	
	@Value("${redis.channel.placard}")
	private String placardChannel;
	
	@Value("${redis.channel.placard.clean}")
	private String placardCleanChannel;
	
	@Value("${redis.channel.placard.modify}")
	private String placardModifyChannel;
	
	@Autowired
	private ResolveTask resolveTask;
	
	@Autowired
	@Qualifier("genericManager")
	private GenericManager gm;
	
	@Autowired
	private EventSchedulerService eventScheduler;
	
	@Autowired
	@Qualifier("eventBakJob")
	private Job eventBakJob;
	
	@Autowired
	@Qualifier("rewardsRefreshJob")
	private Job rewardsRefreshJob;
	
	private static String fileServerPrefix;
	
	@PostConstruct
	public void init() {
		fileServerPrefix = fileServerVal;
		
		//execute task service when tomcat is starting before 3.pm
		Calendar c = Calendar.getInstance();
		int hour = c.get(Calendar.HOUR_OF_DAY);
		if(hour >= 15) {
			resolveTask.persistPromoCount(); 
		}
		
		startScheduler(false);
	}
	
	public void startScheduler(boolean result){
		Event event = gm.findObject(new Event());
		if(event != null) {
			if (result == true) {
				initEvent(event);
			}
			try {
				if(event.getCurStatus() == Event.CUR_STATUS_NORMAL) {
					eventScheduler.schedulerJob(event, eventBakJob);
					eventScheduler.schedulerJob(event, rewardsRefreshJob);
				}
			} catch (SchedulerException e) {
				e.printStackTrace();
			}
			gm.update(event);
		}
	}
	
	private void initEvent(Event event) {
//		if(event.getCurUnit().equalsIgnoreCase("week")) {
//			event.setRefreshStartTime(DateUtil.getNowWeekBegin());
//		} else {
			event.setRefreshStartTime(DateUtil.getDawnDay(1));
//		}
		event.setBakStartTime(event.getRefreshStartTime() - 30*1000);
	}
	
	public static String getAbsolutePath(String urn) {
		return fileServerPrefix + urn;
	}
	
	public static String parseVersion(String version) {
		if(!StringUtils.hasText(version)) return null;
		String[] versionArr = version.split("\\.");
		String parseVer = "";
		int index = 0;
		for(String str : versionArr) {
			if(str.startsWith("0")) {
				parseVer += str.substring(1);
				if(index < versionArr.length - 1) parseVer += ".";
			}
			index ++;
		}
		return parseVer;
	}

	public String getPlacardChannel() {
		return placardChannel;
	}

	public String getPlacardCleanChannel() {
		return placardCleanChannel;
	}

	public String getPlacardModifyChannel() {
		return placardModifyChannel;
	}
	
	public static void main(String[] args) {
		System.out.println(new Date().getTime());
	}

}
