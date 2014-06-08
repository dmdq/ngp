package com.ngnsoft.ngp.web;

import java.util.Date;
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
import com.ngnsoft.ngp.enums.EngineStatus;
import com.ngnsoft.ngp.model.EngineNode;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.EngineService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/engine")
public class EngineCtrl {

    private final String LIST_ACTION = "/web/engine";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private EngineService engineService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute EngineNode searchEngine, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getEnginePageMap(request);
        searchEngine.setId(requestMap.get("p_id"));
        if (!StringUtils.hasText(requestMap.get("p_status"))) {
        } else {
            searchEngine.setStatus(Integer.parseInt(requestMap.get("p_status")));
        }
        if (searchEngine.getId() == null || searchEngine.getId().equals("")) {
            searchEngine.setId(null);
        }

        List<EngineNode> ens = engineService.findMulti(new EngineNode());
        List<EngineNode> engines = (List<EngineNode>) engineService.findMulti(searchEngine, page);
        Long totalNum = engineService.countTotalNum(searchEngine);
        model.addAttribute("engines", engines);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("ens", ens);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("searchEngine", searchEngine);
        model.addAttribute("engineStatusMap", getEngineStatusMap());
        return "engine/engine_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, HttpServletRequest request, Model model) {

        EngineNode engine = new EngineNode();
        model.addAllAttributes(getEnginePageMap(request));
        model.addAttribute("engine", engine);
        model.addAttribute("page", page);
        model.addAttribute("engineStatusMap", getEngineStatusMap());
        return "engine/engine_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute EngineNode engine, Pagination page, HttpServletRequest request, Model model) {
        String msg = null;
        EngineNode existEngine = engineService.get(engine.getId(), EngineNode.class);

        boolean result = existEngine != null;
        if (result) {
            msg = "same";
        } else {
            try {
                if (!StringUtils.hasText(engine.getStatusDesc())) {
                    engine.setStatusDesc(EngineStatus.getDesc(engine.getStatus()));
                }
                engineService.save(engine);
                msg = "success";
            } catch (Exception e) {
                e.printStackTrace();
                msg = "failed";
            }
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getEnginePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{id}/toUpdate")
    public String update(@PathVariable String id, Model model, Pagination page, HttpServletRequest request) {

        EngineNode engine = engineService.get(id, EngineNode.class);
        model.addAttribute("engine", engine);
        model.addAttribute("page", page);
        model.addAllAttributes(getEnginePageMap(request));
        model.addAttribute("engineStatusMap", getEngineStatusMap());
        return "engine/engine_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute EngineNode engine, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            EngineNode oldEngine = engineService.get(engine.getId(), EngineNode.class);

            if (!StringUtils.hasText(engine.getStatusDesc())) {
                oldEngine.setStatusDesc(EngineStatus.getDesc(engine.getStatus()));
            } else {
                oldEngine.setStatusDesc(engine.getStatusDesc());
            }

            oldEngine.setUpdateTime(new Date());
            oldEngine.setStatus(engine.getStatus());
            oldEngine.setHost(engine.getHost());
            oldEngine.setName(engine.getName());
            oldEngine.setStatusLock(engine.getStatusLock());

            engineService.update(oldEngine);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getEnginePageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{id}")
    public ModelAndView del(@PathVariable String id, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            engineService.remove(id, EngineNode.class);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    private Map<Integer, String> getEngineStatusMap() {
        Map<Integer, String> engineStatusMap = new HashMap<Integer, String>();
        for (EngineStatus status : EngineStatus.values()) {
            engineStatusMap.put(status.code(), status.name());
        }
        return engineStatusMap;
    }

    private Map<String, String> getEnginePageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        String p_status = request.getParameter("p_status");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        map.put("p_status", p_status);
        return map;
    }
}
