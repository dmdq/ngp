package com.ngnsoft.ngp.util;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.util.StringUtils;

import com.ngnsoft.ngp.enums.OrderTimeType;
import com.ngnsoft.ngp.model.Pagination;

public class PageUtils {

	private static final String[] datePatterns = new String[]{"yyyy-MM-dd"};

	public static boolean isEmpty(Pagination page) {
		boolean ret = true;
		if (page != null)
			ret = page.getCurrent_page() == 0 & page.getItems_per_page() == 0 & page.getNum_edge_entries() == 0
			& page.getNum_display_entries() == 0;
		return ret;
	}

	public static Map<String, String> getPhotoPageMap(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_photoName", request.getParameter("p_photoName"));
		params.put("p_supplierId", request.getParameter("p_supplierId"));
		return params;
	}

	public static Map<String, String> getPhotoPageMapToUnicode(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_photoName", UnicodeUtil.getUnicodeWithoutU(request.getParameter("p_photoName")));
		params.put("p_supplierId", request.getParameter("p_supplierId"));
		return params;
	}

	public static Map<String, String> getVideoPageInfo(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_videoName", request.getParameter("p_videoName"));
		params.put("p_supplierId", request.getParameter("p_supplierId"));
		params.put("p_videoState", request.getParameter("p_videoState"));
		params.put("p_videoEncodeType", request.getParameter("p_videoEncodeType"));
		return params;
	}

	public static Map<String, String> getVideoPageInfoToUnicode(HttpServletRequest request) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("p_videoName", UnicodeUtil.getUnicodeWithoutU(request.getParameter("p_videoName")));
		params.put("p_supplierId", request.getParameter("p_supplierId"));
		params.put("p_videoState", request.getParameter("p_videoState"));
		params.put("p_videoEncodeType", request.getParameter("p_videoEncodeType"));
		return params;
	}

	public static Map<String, Integer> getPageToMap(Pagination page){
		Map<String, Integer> params = new HashMap<String, Integer>();
		try {
			for(Field f : Pagination.class.getDeclaredFields()){
				f.setAccessible(true);
				params.put(f.getName(), f.getInt(page));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return params;
	}

	public static void setMapFromRequest(Map paramMap, HttpServletRequest request, String userId) {
		Date startTime = null;
		Date endTime = null;

		try {
			if (request.getParameter("beginTime") == null || "".equals(request.getParameter("beginTime"))) {
				startTime = DateUtils.parseDate("2013-09-01", datePatterns);
			} else {
				if (!StringUtils.hasText(userId)) {
					startTime = DateUtils.parseDate(request.getParameter("beginTime"), datePatterns);
				} else {
					startTime = DateUtils.parseDate("2013-09-01", datePatterns);
				}
			}
			if(request.getParameter("endTimes") == null || request.getParameter("endTimes").equals("")){
				endTime = new Date();
			} else {
				if (!StringUtils.hasText(userId)) {
					endTime = DateUtils.parseDate(request.getParameter("endTimes"), datePatterns);
				} else {
					endTime = new Date();
					paramMap.put("userId", Long.parseLong(userId));
				}
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		paramMap.put("beginTime", startTime);
		paramMap.put("endTimes", endTime);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		paramMap.put("startTime", df.format(startTime));
		paramMap.put("endTime", df.format(endTime));
	}

	public static void setDetailsTime(Map paramMap, String time){
		Date startTime = null;
		Date endTime = new Date();
		String times = "";
		Calendar cal = Calendar.getInstance();
		try {
			if (time.equals(OrderTimeType.YESTERDAY_TYPE.value())) {
				cal.add(Calendar.DATE, -1);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
				endTime = DateUtils.parseDate(times, datePatterns);
			} else if (time.equals(OrderTimeType.TODAY_TYPE.value())) {
				startTime = new Date();
			} else if (time.equals(OrderTimeType.SEVENDAYS_TYPE.value())) {
				cal.add(Calendar.DATE, -6);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
			} else if (time.equals(OrderTimeType.FIFTEENTHDAYS_TYPE.value())) {
				cal.add(Calendar.DATE, -14);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
			} else if (time.equals(OrderTimeType.FEBRUARY_TYPE.value())) {
				cal.add(Calendar.MONTH, -1);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
			} else if (time.equals(OrderTimeType.MARCH_TYPE.value())) {
				cal.add(Calendar.MONTH, -3);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
			} else if (time.equals(OrderTimeType.JUNE_TYPE.value())) {
				cal.add(Calendar.MONTH, -6);
				times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
				startTime = DateUtils.parseDate(times, datePatterns);
			} else {
				startTime = DateUtils.parseDate("2013-09-01", datePatterns);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		paramMap.put("beginTime", startTime);
		paramMap.put("endTimes", endTime);
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");

		paramMap.put("startTime", df.format(startTime));
		paramMap.put("endTime", df.format(endTime));
	}
	
	public static void main(String[] args) {
		Date date = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, -3);
		String times = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		System.out.println(times);
	}
}
