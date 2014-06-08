package com.ngnsoft.ngp.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateUtil {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy");
	
	private static TimeZone zeroZone = TimeZone.getTimeZone("GMT-0");
	
	public static String formatDate(Date date) {
		return sdf.format(date);
	}
	
	public static Date parseDate(String date) {
		try {
			return sdf.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> getDateInterval(Date startDate, Date endDate) {
		List<String> intervalDates = new ArrayList<String>();
		String startDateFormat = sdf.format(startDate);
		String endDateForamt = sdf.format(endDate);
		try {
			startDate = sdf.parse(startDateFormat);
			endDate = sdf.parse(endDateForamt);
			long days = (endDate.getTime() - startDate.getTime())/(3600*24*1000);
			for(int i = 0; i < days; i ++) {
				Calendar rightNow = Calendar.getInstance();
				rightNow.setTime(startDate);
				rightNow.add(Calendar.DATE, 1);
				Date dt = rightNow.getTime();
				intervalDates.add(sdf.format(dt));
				startDate = dt;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return intervalDates;
	}
	
	public static String getYear(Date date) {
		return sdf2.format(date);
	}
	
	public static int getWeekNum(Date date) {
		Calendar cl = Calendar.getInstance(); 
		cl.setTime(date);
		int week = cl.get(Calendar.WEEK_OF_YEAR); 
		return week;
	}
	
	public static int getWeekNumWithYear(Date date) {
		int year = Integer.valueOf(getYear(date));
		Calendar cl = Calendar.getInstance(); 
		cl.setTime(date);
		int week = cl.get(Calendar.WEEK_OF_YEAR);
		String weekStr = week > 9 ? week + "" : "0" + week;
		weekStr = year + weekStr; 
		return Integer.valueOf(weekStr);
	}
	
	public static Date getBreforeDate(Date date, int interval) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.add(Calendar.DATE, interval);
		return rightNow.getTime();
	}
	
	public static long getDateIntervalWithHour(Date startDate, Date endDate) {
		long hourInterval = (endDate.getTime() - startDate.getTime())/(3600*1000);
		return hourInterval;
	}
	
	public static long getDateIntervalWithMinute(long currentMillis, long oldMillis) {
		long minuteInterval = (currentMillis - oldMillis)/(60*1000);
		return minuteInterval;
	}
	
	public static long getNowWeekBegin() {
		int mondayPlus;
		Calendar calendar = Calendar.getInstance();
		Date d = new Date();
		long t = d.getTime();
		
		calendar.setTimeZone(zeroZone);
		calendar.setTime(new Date(t));
		// 获得今天是一周的第几天，星期日是第一天，星期二是第二天......
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		if (dayOfWeek == 1) {
			mondayPlus = 0;
		} else {
			mondayPlus = 1 - dayOfWeek;
		}
		calendar.add(Calendar.DATE, mondayPlus);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime().getTime();

	}
	
	public static long getDawnDay(int days) {
		int mondayPlus;
		Calendar calendar = Calendar.getInstance();
		Date d = new Date();
		long t = d.getTime();
		
		calendar.setTimeZone(zeroZone);
		calendar.setTime(new Date(t));
		
		if (days == 1) {
			mondayPlus = 0;
		} else {
			mondayPlus = 1 - days;
		}
		calendar.add(Calendar.DATE, mondayPlus);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		
		return calendar.getTime().getTime();

	}

	public static String getCurrentUtcTime(long timeMillis) throws ParseException {
        Date l_datetime = new Date(timeMillis);
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone l_timezone = TimeZone.getTimeZone("GMT+8");
        formatter.setTimeZone(l_timezone);
        String l_utc_date = formatter.format(l_datetime);
        return l_utc_date;
	}
	
	public static void main(String[] args) throws ParseException {
		
//		TimeZone timezone = TimeZone.getTimeZone("GMT+10");
		
		System.out.println(getCurrentUtcTime(getNowWeekBegin()));
//		System.out.println(getNowWeekBegin(timezone1));
		long s= getDawnDay(1) - getNowWeekBegin();
		System.out.println(s/(1000*60*60));
		System.out.println(getDawnDay(1));
	}

}
