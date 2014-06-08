package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.QueryParam;

import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.enums.AppStatus;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.AppVersion;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.UserSession;
import com.ngnsoft.ngp.service.AppService;
import com.ngnsoft.ngp.service.AppVersionService;
import com.ngnsoft.ngp.service.EngineService;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/app")
public class AppCtrl {

    private final String LIST_ACTION = "/web/app";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private AppService appService;
    @Autowired
    private EngineService engineService;
    @Autowired
    private AppVersionService appVersionService;
    
    @Autowired
    @Qualifier("redisImpl")
    private RedisImpl redisImpl;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute App searchApp, Model model, String msg) {
    	Map<String, String> requestMap = getAppPageMap(request);
        if (searchApp.getId() == null || "".equals(searchApp.getId())) {
            searchApp.setId(null);
        }
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Long userId = StringUtils.hasText(request.getParameter("userId")) ? Long.valueOf(request.getParameter("userId")) : null ;
        	
        if(userId != null) {
        	model.addAttribute("userId", userId);
        	Set<String> set;
			try {
				set = redisImpl.keys("*US_" + "s2_" + userId + "_*");
				List<String> keys = new ArrayList<String>(set);
				Map<String, String> objs = new HashMap<String, String>();
				for(String key : keys) {
					objs.put(key, ((UserSession)redisImpl.get(key)).toJSONObject().toString());
					model.addAttribute("objs", objs);
				}
			} catch (Exception e) {
			}
        }

        List<App> apps = new ArrayList<App>();

        String baid = "-1";
        
        Long totalNum = 0L;
        
        if(requestMap.get("p_appId") != null && !requestMap.get("p_appId").equals("-1") && !"".equals(requestMap.get("p_appId"))) {
        	searchApp.setId(requestMap.get("p_appId"));
    	}
        if (!StringUtils.hasText(searchApp.getId())) {
        	if (request.getParameter("p_baid") == null || request.getParameter("p_baid").equals("") || request.getParameter("p_baid").equals("-1")) {
        		apps = appService.findByApp(searchApp, page);
        		totalNum = appService.countTotalNum(searchApp);
        	} else {
        		 baid = request.getParameter("p_baid");
                 Map paramMap = new HashMap();
                 paramMap.put("baid", baid);
                 apps = appService.findByNamedQuery("find_app_by_isBaid_and_appId", paramMap, page, App.class);
                 totalNum = appService.countByNameQuery("count_app_by_isBaid_and_appId", paramMap, App.class);
        	}
        } else {
            if (request.getParameter("p_baid") == null || request.getParameter("p_baid").equals("-1")) {
                apps = appService.findByApp(searchApp, page);
                totalNum = appService.countTotalNum(searchApp);
            } else {
                baid = request.getParameter("p_baid");
                Map paramMap = new HashMap();
                paramMap.put("baid", baid);
                paramMap.put("appId", searchApp.getId());
                apps = appService.findByNamedQuery("find_app_by_isBaid_and_appId", paramMap, page, App.class);
                totalNum = appService.countByNameQuery("count_app_by_isBaid_and_appId", paramMap, App.class);
            }
        }

        List<App> aps = appService.findMulti(new App());
        
        model.addAttribute("apps", apps);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("aps", aps);
        model.addAttribute("baid", baid);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchApp", searchApp);
		model.addAttribute("checkId", requestMap.get("p_id"));
		model.addAttribute("checkBaId", requestMap.get("p_checkBaId"));
        model.addAttribute("appStatusMap", getAppStatusMap());

        return "app/app_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String toAdd(@ModelAttribute App appSearch, Pagination page, HttpServletRequest request, Model model) {

        List<App> apps = appService.findByApp(appSearch, page);
        model.addAllAttributes(getAppPageMap(request));
        model.addAttribute("apps", apps);
        model.addAttribute("page", page);
        model.addAttribute("appStatusMap", getAppStatusMap());
        return "app/app_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute App app, Pagination page, HttpServletRequest request, Model model) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile appIcon = multipartRequest.getFile("icon");
        String msg = null;
        String appVersion = "";
        try {
        	if (appService.get(app.getId(), App.class) == null) {
	             if (!StringUtils.hasText(app.getBaseId())) {
	                app.setBaseId(app.getId());
	                app.setBaseName(app.getName());
	            } else {
	            	App a = new App(app.getBaseId());
	            	a = appService.get(a.getId(), App.class);
	            	app.setBaseName(a.getName());
	            }
	            if (!StringUtils.hasText(app.getStatusDesc())) {
	                app.setStatusDesc(AppStatus.getDesc(app.getStatus()));
	            }
	            
	            appService.saveApp(app, appIcon);
	
	            if (request.getParameter("appVersion") != null || !"".equals(request.getParameter("appVersion"))) {
	                appVersion = request.getParameter("appVersion");
	                AppVersion av = new AppVersion();
	                av.setAppId(app.getId());
	                av.setVersion(appVersion);
	                appVersionService.save(av);
	            }
	            msg = "success";
        	} else {
        		msg = "exist";
        	}
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAllAttributes(getAppPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{appId}/toUpdate")
    public String update(@PathVariable String appId, Model model, Pagination page, HttpServletRequest request) {
        App app = appService.get(appId, App.class);

        AppVersion av = new AppVersion();
        av.setAppId(appId);

        List<AppVersion> appVersions = appVersionService.findMulti(av);

        model.addAllAttributes(getAppPageMap(request));
        model.addAttribute("app", app);
        model.addAttribute("page", page);
        model.addAttribute("appVersions", appVersions);
        model.addAttribute("appStatusMap", getAppStatusMap());
        return "app/app_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute App app, String id, Model model, Pagination page, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile iconDoc = multipartRequest.getFile("icon");
        String msg = null;
        try {
        	if (!StringUtils.hasText(app.getBaseId())) {
                app.setBaseId(app.getId());
                app.setBaseName(app.getName());
            } else {
            	App a = new App(app.getBaseId());
            	a = appService.get(a.getId(), App.class);
            	app.setBaseName(a.getName());
            }
            if (!StringUtils.hasText(app.getStatusDesc())) {
                app.setStatusDesc(AppStatus.getDesc(app.getStatus()));
            }
            appService.updateApp(app, iconDoc);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getAppPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "selectBaseId")
    public String selectBaseIds(@ModelAttribute App searchApp, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        String[] baseIds = request.getParameter("baseId") == null ? new String[]{} : request.getParameter("baseId").split(",");
        List<App> apps = appService.findByNamedQuery("find_distinct_base_id", searchApp, page, App.class);
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
        Long totalNum = appService.countByNameQuery("count_distinct_base_id", searchApp, App.class);
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchPlacard", searchApp);
        return "app/base_id_select";
    }

    private Map<Integer, String> getAppStatusMap() {
        Map<Integer, String> appStatusMap = new HashMap<Integer, String>();
        for (AppStatus status : AppStatus.values()) {
            appStatusMap.put(status.code(), status.name());
        }
        return appStatusMap;
    }

    private Map<String, String> getAppPageMap(HttpServletRequest request) {
    	String p_appId = request.getParameter("p_appId");
		String p_checkBaId = request.getParameter("p_checkBaId");
        String p_baid = request.getParameter("p_baid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_appId", p_appId);
		map.put("p_checkBaId", p_checkBaId);
        map.put("p_baid", p_baid);
        return map;
    }

    private Map<String, String> getAppPageMapWithUnicode(HttpServletRequest request) {
        String p_appId = request.getParameter("p_appId");
        String p_baid = request.getParameter("p_baid");
		String p_checkBaId = request.getParameter("p_checkBaId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_appId", p_appId);
        map.put("p_baid", p_baid);
		map.put("p_checkBaId", p_checkBaId);
        return map;
    }
}
