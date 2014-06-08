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
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.EngineNodeShow;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.model.ZoneShow;
import com.ngnsoft.ngp.service.EngineService;
import com.ngnsoft.ngp.service.ZoneService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/zone")
public class ZoneCtrl {

    private final String LIST_ACTION = "/web/zone";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private ZoneService zoneService;
    @Autowired
    private EngineService engineService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute Zone searchZone, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getZonePageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_id"))) {
            searchZone.setId(null);
        } else {
            searchZone.setId(Long.parseLong(requestMap.get("p_id")));
        }
        if (!StringUtils.hasText(requestMap.get("p_fveid"))) {
            searchZone.setFavEngineId(null);
        } else {
            searchZone.setFavEngineId(requestMap.get("p_fveid"));
        }
        List<Zone> zones = zoneService.findMulti(searchZone, page);
        List<EngineNode> engineNodes = engineService.findMulti(new EngineNode());

        List<Zone> zs = zoneService.findMulti(new Zone());

        Long totalNum = zoneService.countTotalNum(searchZone);
        model.addAttribute("zones", zones);
        model.addAttribute("engineNodes", engineNodes);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("zs", zs);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchZone", searchZone);

        return "zone/zone_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String toAdd(@ModelAttribute Zone SearchZone, Pagination page, HttpServletRequest request, Model model) {

        model.addAllAttributes(getZonePageMap(request));
        model.addAttribute("page", page);
        model.addAttribute("SearchZone", SearchZone);
        return "zone/zone_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute Zone zone, Pagination page, HttpServletRequest request, Model model) {
        String msg = null;
        String[] engineNames = request.getParameterValues("engineNames") == null ? new String[]{} : request.getParameterValues("engineNames");
        try {
            Zone z = new Zone();
            z.setName(zone.getName());
            List<Zone> zs = zoneService.findMulti(z);
            if (zs.size() != 0) {
                msg = "exist";
            } else {
                zoneService.saveZone(zone, engineNames);
                msg = "success";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getZonePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{id}/toUpdate")
    public String update(@PathVariable String id, Model model, Pagination page, HttpServletRequest request) {

        Zone zone = zoneService.get(Long.parseLong(id), Zone.class);
        model.addAllAttributes(getZonePageMap(request));
        model.addAttribute("zone", zone);
        model.addAttribute("page", page);

        return "zone/zone_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute Zone zone, String id, Model model, Pagination page, HttpServletRequest request) {
        String[] engineNames = request.getParameterValues("engineNames") == null ? new String[]{} : request.getParameterValues("engineNames");
        if (zone.getFavEngineId() == null || zone.getFavEngineId().equals("")) {
        	zone.setFavEngineId(null);
        }
        String msg = null;
        try {
            zoneService.updateZone(zone, engineNames);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getZonePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{id}")
    public ModelAndView del(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            zoneService.delZone(id);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getZonePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "selectZones")
    public String selectZones(@ModelAttribute Zone searchZone, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        String[] zoneIds = request.getParameter("zoneIds") == null ? new String[]{} : request.getParameter("zoneIds").split(",");
        List<Zone> zones = zoneService.findMulti(searchZone, page);
        List<ZoneShow> zoneExpands = new ArrayList<ZoneShow>();
        for (Zone node : zones) {
            ZoneShow show = new ZoneShow();
            BeanUtils.copyProperties(node, show);
            if (Arrays.asList(zoneIds).contains(String.valueOf(node.getId()))) {
                show.setSelect(true);
            } else {
                show.setSelect(false);
            }
            zoneExpands.add(show);
        }
        Long totalNum = zoneService.countTotalNum(searchZone);
        model.addAttribute("zones", zoneExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchPlacard", searchZone);
        return "app/zone_id_select";
    }

    @RequestMapping(value = "selectEngines")
    public String selectEngines(@ModelAttribute EngineNode searchEngine, Pagination page, Model model, HttpServletRequest request) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        if (!StringUtils.hasText(searchEngine.getName())) {
            searchEngine.setName(null);
        }
        String[] engineNames = request.getParameter("engineNames") == null ? new String[]{} : request.getParameter("engineNames").split(",");
        List<EngineNode> engines = engineService.findMulti(searchEngine, page);
        List<EngineNodeShow> enginNodeExpands = new ArrayList<EngineNodeShow>();
        for (EngineNode node : engines) {
            EngineNodeShow show = new EngineNodeShow();
            BeanUtils.copyProperties(node, show);
            if (Arrays.asList(engineNames).contains(String.valueOf(node.getId()))) {
                show.setSelect(true);
            } else {
                show.setSelect(false);
            }
            enginNodeExpands.add(show);
        }
        Long totalNum = engineService.countTotalNum(searchEngine);
        model.addAttribute("engines", enginNodeExpands);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchEngine", searchEngine);
        return "zone/engine_select";
    }

    private Map<String, String> getZonePageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        String p_fveid = request.getParameter("p_fveid");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        map.put("p_fveid", p_fveid);
        return map;
    }
}
