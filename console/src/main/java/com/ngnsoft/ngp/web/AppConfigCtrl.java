package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
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

import com.alibaba.fastjson.JSON;
import com.ngnsoft.ngp.component.DictService;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppConfig;
import com.ngnsoft.ngp.model.AppConfigBase;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.AppConfigService;
import com.ngnsoft.ngp.util.JSONUtil;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/appConfig")
public class AppConfigCtrl {

    private final String LIST_ACTION = "/web/appConfig";
    private final String PLACARD_REQ_VAL = "1";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private AppConfigService appConfigService;
    @Autowired
    private DictService dictService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute AppConfig searchAppConfig, Model model, String msg) {

    	AppConfigBase acbb = new AppConfigBase();
        AppConfigBase acb = new AppConfigBase();
        if (!StringUtils.hasText(searchAppConfig.getAppId())) {
            if (!StringUtils.hasText(request.getParameter("app"))) {
            	searchAppConfig.setAppId(null);
            } else {
            	searchAppConfig.setAppId(request.getParameter("app"));
            	searchAppConfig.setAppName(request.getParameter("app")); 
            	acbb.setAppId(request.getParameter("app"));
            	acbb.setAppName(request.getParameter("app"));
                acb.setAppId(request.getParameter("app"));
                acb.setAppName(request.getParameter("app"));
            }
        } else {
        	acbb.setAppId(searchAppConfig.getAppId());
            acb.setAppId(searchAppConfig.getAppId());
        }
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        List<AppConfig> appConfigs = new ArrayList<AppConfig>();
        List<App> apps = appConfigService.findMulti(new App());
        String baid = "";
        Long totalNum = null;
        if (request.getParameter("baid") == null || request.getParameter("baid").equals("")) {
            List<AppConfig> acs = appConfigService.findMulti(searchAppConfig, page);
            List<AppConfig> ass = appConfigService.findMulti(acb, AppConfig.class, page);
            List<AppConfig> as = new ArrayList<AppConfig>();
            if (acs.size() >= ass.size()) {
                as = acs;
            } else {
                as = ass;
            }

            for (AppConfig appConfig : as) {
                for (App app : apps) {
                    if (app.getId().equals(appConfig.getAppId())) {
                        AppConfig appC = new AppConfig();
                        app = appConfigService.get(app.getId(), App.class);
                        String baseId = app.getId();
                        if (app.getBaseId() != null && !app.getBaseId().equals("")) {
                        	baseId = app.getBaseId();
                        }
                        AppConfig ac = appConfigService.get(app.getId(), AppConfig.class);
                        JSONObject output;
                        if (ac != null) {
                            output = ac.toJSONObject();
                        } else {
                            output = new JSONObject();
                        }
                        AppConfig acf = appConfigService.get(baseId, AppConfigBase.class);
                        if (acf != null) {
                            output = JSONUtil.mergeAndUpdate(acf.toJSONObject(), output);
                        }
                        appC.setAppId(app.getId());
                        appC.setJsonAll(output.toString());
                        appConfigs.add(appC);
                    }
                }
            }
            totalNum = appConfigService.countTotalNum(searchAppConfig);
        } else {
            baid = request.getParameter("baid");
            if (baid.equals("0")) {
                appConfigs = appConfigService.findMulti(acbb, AppConfig.class, page);
                totalNum = appConfigService.countTotalNum(acbb);
            } else {
                appConfigs = appConfigService.findMulti(searchAppConfig, page);
                totalNum = appConfigService.countTotalNum(searchAppConfig);
            }
        }
        model.addAttribute("apps", apps);
        model.addAttribute("appConfigs", appConfigs);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("baid", baid);
        model.addAttribute("app", request.getParameter("app"));
        model.addAttribute("searchAppConfig", searchAppConfig);

        return "app_config/app_config_list";
    }
    
    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, HttpServletRequest request, Model model) {

        AppConfig appConfig = new AppConfig();
        model.addAttribute("appConfig", appConfig);
        model.addAttribute("page", page);

        return "app_config/app_config_add";
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute AppConfig appConfig, HttpServletRequest request, Model model) {
        String msg = null;
        String status = request.getParameter("status");
        try {
            if (status.equals("0")) {
                AppConfigBase appConfigBase = new AppConfigBase();
                appConfigBase.setAppId(appConfig.getAppId());
                if (appConfigService.get(appConfigBase.getAppId(), AppConfigBase.class) != null) {
                    msg = "exist";
                } else {
                    Map<String, String[]> paramterMap = new HashMap(request.getParameterMap());
                    //去除appId属性
                    paramterMap.remove("appId");
                    Map<String, Map<String, String>> keyMap = new HashMap<String, Map<String, String>>();
                    Map<String, String> itemValMap = new HashMap<String, String>();
                    for (String key : paramterMap.keySet()) {
                        if (key.startsWith("key") && !keyMap.containsKey(key)) {
                            String suffixKey = key.substring(3);
                            Map<String, String> itemMap = new HashMap<String, String>();
                            itemMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                            itemValMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                            keyMap.put(key, itemMap);
                        } else {
                            continue;
                        }
                    }
                    appConfigBase.setJsonAll(JSON.toJSONString(itemValMap));
                    appConfigService.save(appConfigBase);
                }
            } else if (status.equals("-1")) {
                if (appConfigService.get(appConfig.getAppId(), AppConfig.class) != null) {
                    msg = "exist";
                } else {
                    Map<String, String[]> paramterMap = new HashMap(request.getParameterMap());
                    //去除appId属性
                    paramterMap.remove("appId");
                    Map<String, Map<String, String>> keyMap = new HashMap<String, Map<String, String>>();
                    Map<String, String> itemValMap = new HashMap<String, String>();
                    for (String key : paramterMap.keySet()) {
                        if (key.startsWith("key") && !keyMap.containsKey(key)) {
                            String suffixKey = key.substring(3);
                            Map<String, String> itemMap = new HashMap<String, String>();
                            itemMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                            itemValMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                            keyMap.put(key, itemMap);
                        } else {
                            continue;
                        }
                    }
                    appConfig.setJsonAll(JSON.toJSONString(itemValMap));
                    appConfigService.save(appConfig);
                    msg = "success";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "toUpdate/{appId}/{baid}")
    public String toUpdate(@PathVariable String appId, @PathVariable String baid, Model model, Pagination page, HttpServletRequest request) {

        AppConfig appConfig = new AppConfig();
        String mes = "";
        if (baid.equals("0")) {
            appConfig = appConfigService.get(appId, AppConfigBase.class);
            mes = "是";
        } else {
            appConfig = appConfigService.get(appId, AppConfig.class);
            mes = "否";
        }
        App app = appConfigService.get(appConfig.getAppId(), App.class);
        appConfig.setApp(app);
        model.addAttribute("jsonMap", JSON.parseObject(appConfig.getJsonAll(), Map.class));
        model.addAttribute("appConfig", appConfig);
        model.addAttribute("page", page);
        model.addAttribute("mes", mes);

        return "app_config/app_config_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute AppConfig appConfig, String id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            Map<String, String[]> paramterMap = new HashMap(request.getParameterMap());
            //去除appId属性
            paramterMap.remove("appId");
            Map<String, Map<String, String>> keyMap = new HashMap<String, Map<String, String>>();
            Map<String, String> itemValMap = new HashMap<String, String>();
            for (String key : paramterMap.keySet()) {
                if (key.startsWith("key") && !keyMap.containsKey(key)) {
                    String suffixKey = key.substring(3);
                    Map<String, String> itemMap = new HashMap<String, String>();
                    itemMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                    itemValMap.put(paramterMap.get(key)[0], paramterMap.get("value" + suffixKey)[0]);
                    keyMap.put(key, itemMap);
                } else {
                    continue;
                }
            }

            if (request.getParameter("mes").equals("0")) {
                AppConfigBase appConfigBase = new AppConfigBase();
                appConfigBase.setAppId(appConfig.getAppId());
                appConfigBase.setJsonAll(JSON.toJSONString(itemValMap));
                appConfigService.update(appConfigBase);
            } else {
                appConfig.setJsonAll(JSON.toJSONString(itemValMap));
                appConfigService.update(appConfig);
            }
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }

        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{appId}")
    public ModelAndView del(@PathVariable String appId, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            if (appConfigService.get(appId, AppConfig.class) != null) {
                appConfigService.remove(appId, AppConfig.class);
            } else {
                appConfigService.remove(appId, AppConfigBase.class);
            }
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
        if (!StringUtils.hasText(searchApp.getName())) {
            searchApp.setName(null);
        }
        String[] appIds = request.getParameter("appId") == null ? new String[]{} : request.getParameter("appId").split(",");
        List<App> apps = (List<App>) appConfigService.findMulti(searchApp, page);
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
        Long totalNum = appConfigService.countTotalNum(searchApp);
        String placard = request.getParameter("placard");
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchApp", searchApp);
        model.addAttribute("placard", placard);
        String forwardUrl = PLACARD_REQ_VAL.equals(placard) ? "placard/app_select" : "app_config/app_select";
        return forwardUrl;
    }
}
