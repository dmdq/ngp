package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.ResUpdate;
import com.ngnsoft.ngp.model.ResUpdateFile;
import com.ngnsoft.ngp.service.ResFileService;
import com.ngnsoft.ngp.service.ResUpdateService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/resUpdate")
public class ResUpdateCtrl {

    private final String LIST_ACTION = "/web/resUpdate";
    private final String PLACARD_REQ_VAL = "1";
    @Autowired
    private ResFileService resFileService;
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private ResUpdateService resUpdateService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute ResUpdate searchResUpdate, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getResUpdatePageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_appId"))) {
            searchResUpdate.setAppId(null);
        } else {
            searchResUpdate.setAppId(requestMap.get("p_appId"));
        }
        List<ResUpdate> resUpdates = resUpdateService.findMulti(searchResUpdate, page);
        Long totalNum = resUpdateService.countTotalNum(searchResUpdate);

        List<App> apps = resUpdateService.findMulti(new App());

        model.addAttribute("resUpdates", resUpdates);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("apps", apps);
        model.addAttribute("searchResUpdate", searchResUpdate);

        return "res/res_update_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, @ModelAttribute ResUpdate searchResUpdate, HttpServletRequest request, Model model) {

        App app = new App();

        model.addAttribute("app", app);
        model.addAttribute("page", page);
        model.addAttribute("searchResUpdate", searchResUpdate);
        model.addAllAttributes(getResUpdatePageMap(request));
        return "res/res_update_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(HttpServletRequest request, Pagination page, @ModelAttribute ResUpdate searchResUpdate, @ModelAttribute FileStorage searchFileStorage, Model model) {
        String[] resUpdateNames = request.getParameterValues("resUpdateNames") == null ? new String[]{} : request.getParameterValues("resUpdateNames");
        List<FileStorage> fss = resFileService.findMulti(new FileStorage(PhotoType.RES_TYPE.value(), null));
        try {
            resUpdateService.addResUpdate(resUpdateNames, searchResUpdate, fss);
        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
        }
        model.addAllAttributes(getResUpdatePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{id}/toUpdate")
    public String update(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
        ResUpdate ru = resUpdateService.get(id, ResUpdate.class);
        ResUpdateFile ruf = new ResUpdateFile();
        ruf.setRuId(ru.getId());
        List<ResUpdateFile> rufs = resUpdateService.findByNamedQuery("findResUpdateFile", ruf, ResUpdateFile.class);
        model.addAttribute("ru", ru);
        model.addAttribute("rufs", rufs);
        model.addAttribute("page", page);
        model.addAllAttributes(getResUpdatePageMap(request));
        return "res/res_update_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute ResUpdate searchResUpdate, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        String[] names = request.getParameterValues("resUpdateNames") == null ? new String[]{} : request.getParameterValues("resUpdateNames");
        ResUpdate ru = resUpdateService.get(searchResUpdate.getId(), ResUpdate.class);
        ru.setUpdateTime(new Date());
        resUpdateService.update(ru);
        ResUpdateFile ruf = new ResUpdateFile();
        ruf.setRuId(ru.getId());
        List<ResUpdateFile> rufs = resUpdateService.findByNamedQuery("findResUpdateFile", ruf, ResUpdateFile.class);
        try {
            for (int i = 0; i < rufs.size(); i++) {
                resUpdateService.remove(rufs.get(i).getId(), ResUpdateFile.class);
            }
            for (int i = 0; i < names.length; i++) {
                ruf.setRuId(ru.getId());
                ruf.setFileUrn(names[i]);
                FileStorage fs = new FileStorage(PhotoType.RES_TYPE.value(), null);
                fs.setId(names[i].substring(names[i].lastIndexOf("/") + 1, names[i].length()));
                fs = resFileService.findObject(fs);
                ruf.setFileName(fs.getName());
                ruf.setUpdateTime(new Date());

                resUpdateService.save(ruf);
            }
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            // TODO: handle exception
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getResUpdatePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{id}")
    public ModelAndView del(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            ResUpdateFile ruf = new ResUpdateFile();
            ruf.setRuId(id);
            List<ResUpdateFile> rufs = resUpdateService.findByNamedQuery("findResUpdateFile", ruf, ResUpdateFile.class);
            for (int i = 0; i < rufs.size(); i++) {
                resUpdateService.remove(rufs.get(i).getId(), ResUpdateFile.class);
            }
            resUpdateService.remove(id, ResUpdate.class);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getResUpdatePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "selectResUpdateFile/{id}")
    public String selectResUpdateFile(@PathVariable Long id, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        ResUpdateFile resUF = new ResUpdateFile();
        resUF.setRuId(id);
        List<ResUpdateFile> resUpdateFiles = resUpdateService.findMulti(resUF, page);
        Long totalNum = resUpdateService.countTotalNum(resUF);
        model.addAttribute("resUpdateFiles", resUpdateFiles);
        model.addAttribute("id", id);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        return "res/res_update_file_info";
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
        List<App> apps = (List<App>) resUpdateService.findMulti(searchApp, page);
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
        Long totalNum = resUpdateService.countTotalNum(searchApp);
        String placard = request.getParameter("placard");
        model.addAttribute("apps", appExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchApp", searchApp);
        model.addAttribute("placard", placard);
        String forwardUrl = PLACARD_REQ_VAL.equals(placard) ? "placard/app_select" : "app_config/app_select";
        return forwardUrl;
    }

    private Map<String, String> getResUpdatePageMap(HttpServletRequest request) {
        String p_appId = request.getParameter("p_appId");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_appId", p_appId);
        return map;
    }
}
