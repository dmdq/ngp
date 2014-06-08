package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.alibaba.fastjson.JSON;
import com.ngnsoft.ngp.component.coc.model.CocKeep;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.slots.model.SlotsKeep;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.factory.UserAppDataFactory;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.chart.Column3DChart;
import com.ngnsoft.ngp.model.chart.Column3DChartData;
import com.ngnsoft.ngp.model.chart.Column3DDataItem;
import com.ngnsoft.ngp.service.GenericManager;
import com.ngnsoft.ngp.util.ConvertUtil;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/report/{gameName}")
public class ReportCtrl {

	@Autowired
	@Qualifier("genericManager")
	private GenericManager gm;

	@Autowired
	private PageConfig pageConfig;


	@RequestMapping
	public String query(HttpServletRequest request, Model model, Placard searchPlacard) {
		return "report/query_new_user_chart";
	}

	@RequestMapping(value = "queryUserByRate")
	public String queryUserByRate(HttpServletRequest request, Model model, Placard searchPlacard) {
		Long countUser = gm.countTotalNum(new UserData());
		Long countName = gm.countByNameQuery("count_userData_by_nick", new UserData(), UserData.class);
		String min = ConvertUtil.convertDouble2Ratio(Double.parseDouble(countName.toString()), Double.parseDouble(countUser.toString()));

		model.addAttribute("countUser", countUser);
		model.addAttribute("countName", countName);
		model.addAttribute("min", min);
		return "report/quert_user_count_by_rate";
	}

	@RequestMapping(value = "/queryNewUser")
	public String queryNewUser(@PathVariable("gameName") String gameName, HttpServletRequest request, Model model, Placard searchPlacard) {
		Column3DChart chart = new Column3DChart();
		chart.setCaption("每日新增");
		chart.setXAxisName("日期");
		chart.setYAxisName("数量");
		Map paramMap = new HashMap();
		PageUtils.setMapFromRequest(paramMap, request, null);
		List<Map> maps = gm.findByNamedQuery("findEveryDayUserCount", paramMap, UserAppDataFactory.getUserAppData(gameName).getClass(), Map.class);
		List<Column3DDataItem> data = new LinkedList<Column3DDataItem>();
		for(Map map : maps) {
			Column3DDataItem item = new Column3DDataItem();
			item.setLabel(map.get("dates").toString());
			item.setValue(map.get("count").toString());
			data.add(item);
		}
		Column3DChartData chartData = new Column3DChartData();
		chartData.setChart(chart);
		chartData.setData(data);
		model.addAttribute("data", chartData);
		model.addAttribute("beginTime", request.getParameter("beginTime"));
		model.addAttribute("endTime", request.getParameter("endTimes"));
		System.out.println(JSON.toJSONString(chartData));
		return "jsonView";
	}

	@RequestMapping(value = "/queryUserCountByRate")
	public String queryUserCountByRate(@PathVariable("gameName") String gameName, Model model) {
		Column3DChart chart = new Column3DChart();
		chart.setCaption("用户级别与比例");
		chart.setXAxisName("级别");
		chart.setYAxisName("比例");
		chart.setNumberSuffix("%25");
		chart.setRotateNames("1");
		chart.setNumDivLines("3"); 
		chart.setDecimalPrecision("2");
		chart.setDivlineDecimalPrecision("0");
		chart.setLimitsDecimalPrecision("0");
		Long count1 = gm.countTotalNum(new UserData());
		Long userCount = gm.countTotalNum(new User());
		Map paramMap = new HashMap();
		List<Map> maps = gm.findByNamedQuery("findUserCountByRate", paramMap, UserAppDataFactory.getUserAppData(gameName).getClass(), Map.class);
		List<Column3DDataItem> data = new LinkedList<Column3DDataItem>();
		Map<Long, Long> rangeMap = new TreeMap<Long, Long>();
		for(Map map : maps) {
			Long count = Long.parseLong(map.get("count").toString());
			Long level = Long.parseLong(map.get("level").toString());
			if(level <= 10) {
				rangeMap.put(level - 1, count);
			} else {
				if(level <= 80) {
					Long key = (level-1)/10 * 10;
					if(rangeMap.get(key) != null) {
						rangeMap.put(key, rangeMap.get(key) + count);
					} else {
						rangeMap.put(key, count);
					}
				} else {
					Long key = 80l;
					if(rangeMap.get(key) != null) {
						rangeMap.put(key, rangeMap.get(key) + count);
					} else {
						rangeMap.put(key, count);
					}
				}
			}
		}
		for(Long range : rangeMap.keySet()) {
			Column3DDataItem item = new Column3DDataItem();
			if(range < 10) {
				item.setLabel((range + 1) + "(" + rangeMap.get(range) +")");
			} else {
				if(range < 80) {
					item.setLabel((range + 1) + "~" + (range + 10) + "(" + rangeMap.get(range) +")");
				} else {
					item.setLabel((range + 1) + "以上" + "(" + rangeMap.get(range) +")");
				}
			}
			item.setValue(ConvertUtil.exactDoubleWithoutPrecent(rangeMap.get(range) * 1.0/count1 * 1.0));
			data.add(item);
		}
		Column3DChartData chartData = new Column3DChartData();
		chartData.setChart(chart);
		chartData.setData(data);
		model.addAttribute("data", chartData);
		return "jsonView";
	}

	@RequestMapping(value = "/keep")
	public String userKeep(String day, Model model){
		return "report/report_list";
	}
	
	@RequestMapping(value = "/slotsKeep")
	public String slotsKeep(String day, Model model){
		return "report/report_slots_list";
	}

	@RequestMapping(value = "/remain")
	public String userList(Pagination page, HttpServletRequest request, Model model){
		Map paramMap = new HashMap();
		PageUtils.setMapFromRequest(paramMap, request, null);
		List<CocKeep> keeps = gm.findByNamedQuery("find_cocKeep_by_time", paramMap, CocKeep.class);
		model.addAttribute("keeps", keeps);
		model.addAttribute("beginTime", request.getParameter("beginTime"));
		model.addAttribute("endTime", request.getParameter("endTimes"));
		return "report/report_list";
	}
	
	@RequestMapping(value = "/slotsRemain")
	public String slotsList(Pagination page, HttpServletRequest request, Model model){
		Map paramMap = new HashMap();
		PageUtils.setMapFromRequest(paramMap, request, null);
		List<SlotsKeep> keeps = gm.findByNamedQuery("find_slotsKeep_by_time", paramMap, SlotsKeep.class);
		model.addAttribute("keeps", keeps);
		model.addAttribute("beginTime", request.getParameter("beginTime"));
		model.addAttribute("endTime", request.getParameter("endTimes"));
		return "report/report_slots_list";
	}

}
