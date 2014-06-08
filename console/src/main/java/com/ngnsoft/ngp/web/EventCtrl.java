package com.ngnsoft.ngp.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ngnsoft.ngp.component.DictService;
import com.ngnsoft.ngp.component.slots.model.Event;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.ZoneService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/event")
public class EventCtrl {

    private final String LIST_ACTION = "/web/event";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private ZoneService zoneServiceImpl;
    
    @Autowired
    private DictService ds;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute Zone searchZone, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        List<Event> events = zoneServiceImpl.findMulti(new Event());
        model.addAttribute("msg", msg);
        model.addAttribute("events", events);
        model.addAttribute("totalNum", 1L);
        model.addAttribute("page", page);
        return "event/event_list";
    }

    @RequestMapping(value = "{eventId}/toUpdate")
    public String update(@PathVariable Integer eventId, Model model, Pagination page, HttpServletRequest request) {
    	Event event = zoneServiceImpl.get(eventId, Event.class);
        model.addAttribute("event", event);
        return "event/event_update";
    }

    @RequestMapping(value = "update")
    public ModelAndView update(@ModelAttribute Event event, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
        	Event ev = zoneServiceImpl.get(event.getId(), Event.class);
        	Event e = zoneServiceImpl.get(event.getId(), Event.class);
        	e.setCurStatus(event.getCurStatus());
        	e.setCurTotalHours(event.getCurUnit().equals("week") ? 7*24 : Integer.parseInt(event.getCurUnit())*24);
        	e.setCurUnit(event.getCurUnit().equals("week") ? "week" : event.getCurUnit() + "day");
        	e.setNextUnit(event.getNextUnit().equals("week") ? "week" : event.getNextUnit() + "day");
        	e.setNextStatus(event.getNextStatus());
        	e.setNextType(event.getNextType());
        	e.setNextTotalHours(event.getNextUnit().equals("week") ? 7*24 : Integer.parseInt(event.getNextUnit())*24);
        	zoneServiceImpl.update(e);
        	if (ev.getCurStatus().equals(Event.CUR_STATUS_CLOSED) && event.getCurStatus().equals(Event.CUR_STATUS_NORMAL)) {
        		ds.startScheduler(true);
        	}
        	msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }
}
