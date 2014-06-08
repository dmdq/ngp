package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppZone;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.AppZoneService;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/appZone")
public class AppZoneCtrl {

	private final String LIST_ACTION = "/web/appZone";

	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private AppZoneService appZoneService;

	@SuppressWarnings("unchecked")
	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute AppZone searchAppZone, Model model, String msg) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		String baid = "";
		String choice = "";
		Map<String, String> requestMap = getAppPageMap(request);
		if(!StringUtils.hasText(requestMap.get("p_choice"))) {
			choice = "app";
		} else {
			choice = requestMap.get("p_choice");
		}
		
		List<AppZone> appZones = new ArrayList<AppZone>();
		
		List<App> apps = new ArrayList<App>();
		List<Zone> zones = new ArrayList<Zone>();
		
		long totalNum = 0L;
		
		if(choice.equals("app")) {
			if(!StringUtils.hasText(requestMap.get("p_id"))){
				searchAppZone.setAppId(null);
				baid = "-1";
			} else {
				searchAppZone.setAppId(requestMap.get("p_id"));
				if(!StringUtils.hasText(requestMap.get("p_baid"))){
					searchAppZone.setIsBaid(true);
					baid = "-1";
				} else {
					if(requestMap.get("p_baid").equals("1")) {
						baid = "1";
						searchAppZone.setIsBaid(false);
					} else if (requestMap.get("p_baid").equals("0")) {
						baid = "0";
						searchAppZone.setIsBaid(true);
					} else {
						baid = "-1";
					}
				}
			}
			
			apps = appZoneService.findByNamedQuery("find_app_in_app_zone", searchAppZone, page, App.class);
			for (App app : apps) {
				Map paramMap = new HashMap();
				paramMap.put("appId", app.getId());
				if(!StringUtils.hasText(requestMap.get("p_baid"))){
					paramMap.put("isBaid", 0);
				} else {
					paramMap.put("isBaid", 1);
				}
				zones = appZoneService.findByNamedQuery("find_zones_by_app_and_isBaid", paramMap, App.class, Zone.class);
				app.setZones(zones);
			}
			List<App> as = appZoneService.findByNamedQuery("find_app_in_app_zone", searchAppZone, App.class);
			totalNum = as.size();
		} else {
			if(!StringUtils.hasText(requestMap.get("p_zoneId"))) {
				searchAppZone.setZoneId(null);
			} else {
				searchAppZone.setZoneId(Long.parseLong(requestMap.get("p_zoneId")));
			}
			zones = appZoneService.findByNamedQuery("find_zone_in_app_zone", searchAppZone, page, Zone.class);
			
			for (Zone zone : zones) {
				Map paramMap = new HashMap();
				paramMap.put("zoneId", zone.getId());
				apps = appZoneService.findByNamedQuery("find_apps_by_zone", paramMap, Zone.class, App.class);
				zone.setApps(apps);
			}
			List<Zone> zs = appZoneService.findByNamedQuery("find_zone_in_app_zone", searchAppZone, Zone.class);
			totalNum = zs.size();
		}
		
		List<App> aps = appZoneService.findMulti(new App());
		List<Zone> zs = appZoneService.findMulti(new Zone());
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("choice", choice);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("azzs", appZones);
		model.addAttribute("apps", apps);
		model.addAttribute("aps", aps);
		model.addAttribute("baid", baid);
		model.addAttribute("zs", zs);
		model.addAttribute("zones", zones);
		model.addAttribute("checkId", requestMap.get("p_id"));
		model.addAttribute("checkBaId", requestMap.get("p_checkBaId"));
		model.addAttribute("searchAppZone", searchAppZone);

		return "app_zone/app_zone_list";
	}

	@RequestMapping(value = "toAdd", method = RequestMethod.GET)
	public String add(@ModelAttribute AppZone SearchAppZone,Pagination page, HttpServletRequest request, Model model) {

		model.addAttribute("page", page);
		model.addAttribute("SearchAppZone", SearchAppZone);
		return "app_zone/app_zone_add";
	}

	@RequestMapping(value = "add", method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute AppZone searchAppZone, HttpServletRequest request, Model model) {
		String msg = null;
		String[] zoneIds = request.getParameterValues("zoneIds") == null ? new String[]{} : request.getParameterValues("zoneIds");
		try {
			for (String zoneId : zoneIds) {
				searchAppZone.setZoneId(Long.parseLong(zoneId));
				if(request.getParameter("isBaid").equals("0")){
					searchAppZone.setIsBaid(false);
				} else {
					searchAppZone.setIsBaid(true);
				}
				List<AppZone> azs = appZoneService.findByNamedQuery("findAppZones", searchAppZone, AppZone.class);
				if(azs.size() == 0) {
					appZoneService.save(searchAppZone);
					msg = "success";
				} else {
					msg = "same";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
		return new ModelAndView(new RedirectView(LIST_ACTION, true));
	}

	@RequestMapping(value = "del/{id}")
	public ModelAndView del(@PathVariable Long id, Model model,Pagination page, HttpServletRequest request) {
		String msg = null;
		try {
			appZoneService.remove(id, AppZone.class);
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
		return new ModelAndView(new RedirectView(LIST_ACTION, true));
	}

	private Map<String, String> getAppPageMap(HttpServletRequest request){
		String p_choice = request.getParameter("p_choice");
		String p_id = request.getParameter("p_id");
		String p_baid = request.getParameter("p_baid");
		String p_checkBaId = request.getParameter("p_checkBaId");
		String p_zoneId = request.getParameter("p_zoneId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("p_choice", p_choice);
		map.put("p_id", p_id);
		map.put("p_baid", p_baid);
		map.put("p_checkBaId", p_checkBaId);
		map.put("p_zoneId", p_zoneId);
		return map;
	}
	
}
