package com.ngnsoft.ngp.web;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
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

import com.ngnsoft.ngp.component.DictService;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.AppVersion;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.model.PlacardTarget;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.model.ZoneEngine;
import com.ngnsoft.ngp.model.show.PlacardShow;
import com.ngnsoft.ngp.model.show.PlacardTargetShow;
import com.ngnsoft.ngp.service.PlacardTargetService;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/placardTarget")
public class PlacardTargetCtrl {
	
	private static Logger LOGGER = Logger.getLogger(PlacardTargetCtrl.class);

    private final String LIST_ACTION = "/web/placardTarget";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private PlacardTargetService placardTargetService;
    @Autowired
    private RedisImpl redisImpl;
    @Autowired
    private DictService dictService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute PlacardTarget searchPlacardTarget, Model model, String msg) {
    	Map<String, String> requestMap = getAppPageMap(request);
        if(!StringUtils.hasText(requestMap.get("p_placardId"))){
        	searchPlacardTarget.setPlacardId(null);
		} else {
			searchPlacardTarget.setPlacardId(Long.parseLong(requestMap.get("p_placardId")));
		}
        if(!StringUtils.hasText(requestMap.get("p_zoneId"))){
        	searchPlacardTarget.setZoneId(null);
        } else {
        	searchPlacardTarget.setZoneId(Long.parseLong(requestMap.get("p_zoneId")));
        }
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        if (searchPlacardTarget.getAppId() == null || searchPlacardTarget.getAppId().equals("")) {
            searchPlacardTarget.setAppId(null);
        }
        List<Placard> placards = this.placardTargetService.findMulti(new Placard());
        List<Zone> zones = this.placardTargetService.findMulti(new Zone());
        List<App> apps = this.placardTargetService.findMulti(new App());
        List<PlacardTargetShow> placardTargets = placardTargetService.findMulti(searchPlacardTarget, PlacardTargetShow.class, page);

        Long totalNum = placardTargetService.countTotalNum(searchPlacardTarget);
        model.addAttribute("placardTargets", placardTargets);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("zones", zones);
        model.addAttribute("apps", apps);
        model.addAttribute("placards", placards);
        model.addAttribute("searchPlacardTarget", searchPlacardTarget);

        return "placard/placard_target_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, HttpServletRequest request, Model model) {

        PlacardTarget placardTarget = new PlacardTarget();
        model.addAttribute("placardTarget", placardTarget);
        model.addAttribute("page", page);

        return "placard/placard_target_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute PlacardTarget placardTarget, HttpServletRequest request, Model model) {
        String msg = null;
        String[] zoneIds = request.getParameterValues("zoneIds") == null ? new String[]{} : request.getParameterValues("zoneIds");
        try {
            if (request.getParameter("baid").equals("1")) {
                placardTarget.setBaid(true);
            } else {
                placardTarget.setBaid(false);
            }
            int count = placardTargetService.saveBatch(placardTarget, zoneIds);
            sendMessageByPlacard(placardTarget.getPlacardId());
            if (count == 0) {
                msg = "success";
            } else {
                msg = "exist";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{placardId}/toUpdate")
    public String update(@PathVariable Long placardId, Model model, Pagination page, HttpServletRequest request) {

        PlacardTarget placardTarget = placardTargetService.get(placardId, PlacardTarget.class);
        placardTarget.setPlacard(placardTargetService.get(placardTarget.getPlacardId(), Placard.class));
        model.addAttribute("placardTarget", placardTarget);
        model.addAttribute("page", page);

        return "placard/placard_target_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute PlacardTarget placardTarget, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            placardTargetService.update(placardTarget);
            sendMessageByPlacard(placardTarget.getPlacardId());
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{id}")
    public ModelAndView del(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            placardTargetService.remove(id, PlacardTarget.class);
            sendMessageByPlacard(null);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "selectApp")
    public String selectApp(@ModelAttribute App searchApp, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        String appId = null;
        if (!StringUtils.hasText(searchApp.getName())) {
            searchApp.setName(null);
        }
        if (StringUtils.hasText(searchApp.getId())) {
            appId = searchApp.getId();
        }
        String[] appIds = request.getParameter("appId") == null ? new String[]{} : request.getParameter("appId").split(",");
        String baid = request.getParameter("baid");
        Map paraMap = new HashMap();
        paraMap.put("appId", appId);
        paraMap.put("baid", baid);
        List<App> apps = placardTargetService.findByNamedQuery("find_app_by_isBaid_and_appId", paraMap, page, App.class);
        List<AppShow> appExpands = new ArrayList<AppShow>();
        for (App app : apps) {
            AppShow show = new AppShow();
            BeanUtils.copyProperties(app, show);
            if (Arrays.asList(appIds).contains(app.getId())) {
                show.setSelect(true);
            } else {
                show.setSelect(false);
            }
            appExpands.add(show);
        }
        Long totalNum = placardTargetService.countByNameQuery("count_app_by_isBaid_and_appId", paraMap, App.class);
        String placard = request.getParameter("placard");
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchApp", searchApp);
        model.addAttribute("placard", placard);
        model.addAttribute("baid", baid);
        return "placard/app_select";
    }
    
    @RequestMapping(value = "selectBaseIds")
    public String selectBaseIds(@ModelAttribute App searchApp, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        String[] baseIds = request.getParameter("baseId") == null ? new String[]{} : request.getParameter("baseId").split(",");
        List<App> apps = (List<App>)placardTargetService.findByNamedQuery("find_distinct_base_id", searchApp, page, App.class);
        List<AppShow> appExpands = new ArrayList<AppShow>();
        for (App node : apps) {
            AppShow show = new AppShow();
            BeanUtils.copyProperties(node, show);
            if (Arrays.asList(baseIds).contains(String.valueOf(node.getBaseId()))) {
                show.setSelect(true);
            } else {
                show.setSelect(false);
            }
            appExpands.add(show);
        }
        Long totalNum = placardTargetService.countByNameQuery("count_distinct_base_id", searchApp, App.class);
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchPlacard", searchApp);
        return "placard/base_id_select";
    }

    @RequestMapping(value = "selectPlacards")
    public String selectPlacards(@ModelAttribute Placard searchPlacard, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        if (!StringUtils.hasText(searchPlacard.getTitle())) {
            searchPlacard.setTitle(null);
        }
        String[] placardIds = request.getParameter("placardId") == null ? new String[]{} : request.getParameter("placardId").split(",");
        List<Placard> placards = (List<Placard>) placardTargetService.findMulti(searchPlacard, page);
        List<PlacardShow> placardExpands = new ArrayList<PlacardShow>();
        for (Placard node : placards) {
            PlacardShow show = new PlacardShow();
            BeanUtils.copyProperties(node, show);
            if (Arrays.asList(placardIds).contains(String.valueOf(node.getId()))) {
                show.setSelect(true);
            } else {
                show.setSelect(false);
            }
            placardExpands.add(show);
        }
        Long totalNum = placardTargetService.countTotalNum(searchPlacard);
        model.addAttribute("placards", placardExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchPlacard", searchPlacard);
        return "placard/placard_select";
    }

    @RequestMapping(value = "selectAppVersions")
    public String selectAppVersions(@ModelAttribute AppVersion searchAppVersion, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        List<AppVersion> appVersions = placardTargetService.findMulti(searchAppVersion, page);
        Long totalNum = placardTargetService.countTotalNum(searchAppVersion);
        model.addAttribute("appVersions", appVersions);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchAppVersion", searchAppVersion);
        return "placard/app_version_select";
    }

    private void sendMessageByPlacard(Long placardId) {
        Map<String, Object> placardMap = new HashMap<String, Object>();
        if (placardId != null) {
            Placard placard = placardTargetService.get(placardId, Placard.class);
            if (placard != null) {
                placardMap.put("placard", placard);
                Map paramMap = new HashMap();
                paramMap.put("placardId", placard.getId());
                paramMap.put("sysDate", new Date());
                List<PlacardTarget> placardTargets = placardTargetService.findByNamedQuery("find_placard_Target_by_placardId", paramMap, PlacardTarget.class);
                Map<Long, List<String>> zoneEngineIds = new HashMap<Long, List<String>>();
                if (placardTargets != null) {
                    placardMap.put("placardTargets", placardTargets);
                    for (PlacardTarget placardTarget : placardTargets) {
                        if (placardTarget.getZoneId() != null) {
                            if (!zoneEngineIds.containsKey(placardTarget.getZoneId())) {
                                List<String> engineIds = placardTargetService.findByNamedQuery("find_engineIds_by_zones", placardTarget.getZoneId(), ZoneEngine.class, String.class);
                                zoneEngineIds.put(placardTarget.getZoneId(), engineIds);
                            }
                        }
                    }
                }
                if (zoneEngineIds.size() > 0) {
                    placardMap.put("zone", zoneEngineIds);
                }
            }
        }
        try {
        	redisImpl.sendMessage(dictService.getPlacardChannel(), (Serializable) placardMap);
            redisImpl.sendMessage(dictService.getPlacardCleanChannel(), "");
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
    }
    
    private Map<String, String> getAppPageMap(HttpServletRequest request){
		String p_placardId = request.getParameter("p_placardId");
		String p_zoneId = request.getParameter("p_zoneId");
		Map<String, String> map = new HashMap<String, String>();
		map.put("p_placardId", p_placardId);
		map.put("p_zoneId", p_zoneId);
		return map;
	}
}
