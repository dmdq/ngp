package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

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

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.AppVersion;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.AppVersionService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/appVersion")
public class AppVersionCtrl {

    private final String LIST_ACTION = "/web/appVersion";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private AppVersionService appVersionService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute AppVersion searchAppVersion, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getAppVersionPageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_appId"))) {
            searchAppVersion.setAppId(null);
        } else {
            searchAppVersion.setAppId(requestMap.get("p_appId"));
        }

        List<AppVersion> appVersions = appVersionService.findMulti(searchAppVersion, page);
        Long totalNum = appVersionService.countTotalNum(searchAppVersion);

        List<App> apps = appVersionService.findMulti(new App());

        model.addAttribute("appVersions", appVersions);
        model.addAttribute("apps", apps);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchAppVersion", searchAppVersion);
        return "app_version/app_version_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, @ModelAttribute AppVersion searchAppVersion, HttpServletRequest request, Model model) {

        AppVersion appVersion = new AppVersion();
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("searchAppVersion", searchAppVersion);
        model.addAttribute("page", page);
        model.addAllAttributes(getAppVersionPageMap(request));
        return "app_version/app_version_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute AppVersion appVersion, Pagination page, HttpServletRequest request, Model model) {
        String msg = null;
        try {
            appVersionService.save(appVersion);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getAppVersionPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{id}/toUpdate")
    public String update(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {

        AppVersion appVersion = appVersionService.get(id, AppVersion.class);
        model.addAttribute("appVersion", appVersion);
        model.addAttribute("page", page);
        model.addAllAttributes(getAppVersionPageMap(request));
        return "app_version/app_version_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute AppVersion appVersion, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            appVersionService.update(appVersion);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getAppVersionPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{id}")
    public ModelAndView del(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            appVersionService.remove(id, AppVersion.class);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getAppVersionPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
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
        List<App> apps = (List<App>) appVersionService.findMulti(searchApp, page);
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
        Long totalNum = appVersionService.countTotalNum(searchApp);
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchApp", searchApp);
        return "app_version/app_select";
    }

    private Map<String, String> getAppVersionPageMap(HttpServletRequest request) {
        String p_appId = request.getParameter("p_appId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_appId", p_appId);
        return map;
    }
}
