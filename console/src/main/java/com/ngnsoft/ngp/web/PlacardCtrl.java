package com.ngnsoft.ngp.web;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
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
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Placard;
import com.ngnsoft.ngp.service.PlacardService;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/placard")
public class PlacardCtrl {

    private final String LIST_ACTION = "/web/placard";
    
    private static final String[] datePatterns = new String[]{"yyyy-MM-dd HH:mm:ss"};
    
    private static Logger LOGGER = Logger.getLogger(PlacardCtrl.class);
    
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private PlacardService placardService;
    @Autowired
    private RedisImpl redisImpl;
    @Autowired
    private DictService dictService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute Placard searchPlacard, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }

        Map<String, String> requestMap = getPlacardPageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_id"))) {
            searchPlacard.setId(null);
        } else {
            searchPlacard.setId(Long.parseLong(requestMap.get("p_id")));
        }
        if (!StringUtils.hasText(requestMap.get("p_status"))) {
            searchPlacard.setStatus(null);
        } else {
            searchPlacard.setStatus(Integer.parseInt(requestMap.get("p_status")));
        }
        List<Placard> placards = placardService.findMulti(searchPlacard, page);
        List<Placard> ps = placardService.findMulti(new Placard());
        Long totalNum = placardService.countTotalNum(searchPlacard);

        model.addAttribute("placards", placards);
        model.addAttribute("ps", ps);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("searchPlacard", searchPlacard);

        return "placard/placard_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String add(Pagination page, HttpServletRequest request, Model model) {

        Placard placard = new Placard();
        model.addAttribute("placard", placard);
        model.addAttribute("page", page);
        model.addAllAttributes(getPlacardPageMap(request));
        return "placard/placard_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute Placard placard, Pagination page, HttpServletRequest request, Model model) {
        Date startTime = null;
        Date endTime = null;
        String msg = null;
        try {
            startTime = DateUtils.parseDate(request.getParameter("beginTime"), datePatterns);
            endTime = DateUtils.parseDate(request.getParameter("overTime"), datePatterns);
            placard.setStartTime(startTime);
            placard.setEndTime(endTime);
            placard.setCreateBy("admin");
            placardService.save(placard);
            msg = "success";
            sendMessage("modify", placard);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getPlacardPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "{placardId}/toUpdate")
    public String update(@PathVariable String placardId, Model model, Pagination page, HttpServletRequest request) {
        Placard placard = placardService.get(Long.parseLong(placardId), Placard.class);
        model.addAttribute("placard", placard);
        model.addAttribute("page", page);
        model.addAllAttributes(getPlacardPageMap(request));
        return "placard/placard_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute Placard placard, String id, Model model, Pagination page, HttpServletRequest request) {
        Date startTime = null;
        Date endTime = null;
        String msg = null;
        try {
            startTime = DateUtils.parseDate(request.getParameter("beginTime"), datePatterns);
            endTime = DateUtils.parseDate(request.getParameter("overTime"), datePatterns);
            placard.setStartTime(startTime);
            placard.setEndTime(endTime);
            placardService.update(placard);
            msg = "success";
            sendMessage("modify", placard);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getPlacardPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "del/{placardId}")
    public ModelAndView del(@PathVariable Long placardId, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
        	Placard placard = placardService.get(placardId, Placard.class);
            placardService.removePlacard(placardId);
            msg = "success";
            sendMessage("del", placard);
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getPlacardPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }
    
    public void sendMessage(String action, Placard placard) {
    	Map<String, Object> map = new HashMap<String, Object>();
        map.put("action", action);
        map.put("placard", placard);
        try {
        	redisImpl.sendMessage(dictService.getPlacardModifyChannel(), (Serializable) map);
        	redisImpl.sendMessage(dictService.getPlacardCleanChannel(), "");
		} catch (Exception e) {
			LOGGER.error("Redis operate ERROR:", e);
		}
    }

    private Map<String, String> getPlacardPageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        String p_status = request.getParameter("p_status");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        map.put("p_status", p_status);
        return map;
    }
}
