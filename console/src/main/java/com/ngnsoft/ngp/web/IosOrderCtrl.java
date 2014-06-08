package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.component.coc.model.CocIosOrder;
import com.ngnsoft.ngp.component.slots.SlotsComponent;
import com.ngnsoft.ngp.component.slots.model.SlotsSaleHistory;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.App;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.SaleHistory;
import com.ngnsoft.ngp.model.Zone;
import com.ngnsoft.ngp.service.IosOrderService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/order")
public class IosOrderCtrl {

	private final String LIST_ACTION = "/order/order";
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private IosOrderService orderService;

	public static final String COC_GEM = "gem";

	@RequestMapping(value = "order")
	public String order(Pagination page, HttpServletRequest request, Model model) {
		if (PageUtils.isEmpty(page)) {
            page = pageConfig.getRechargePage();
        }
		model.addAttribute("page", page);
		model.addAttribute("totalNum", 0L);
		return "order/ios_order_list";
	}

	@RequestMapping(value = "slots")
	public String slotsOrder(Pagination page, HttpServletRequest request, Model model) {
		if (PageUtils.isEmpty(page)) {
            page = pageConfig.getRechargePage();
        }
		App app = new App();
		app.setBaseId(SlotsComponent.APP_BASE_ID);
		List<App> apps = orderService.findMulti(app);
		model.addAttribute("page", page);
		model.addAttribute("apps", apps);
		model.addAttribute("totalNum", 0L);
		return "order/slots_order_list";
	}
	
	@RequestMapping(value = "{time}/order")
	public String list(@PathVariable String time, Pagination page, HttpServletRequest request, @ModelAttribute Zone searchZone, Model model, String msg) {
		if (PageUtils.isEmpty(page)) {
            page = pageConfig.getRechargePage();
        }
		Map paramMap = new HashMap();
		if ("null".equals(time) || time == null) {
			PageUtils.setMapFromRequest(paramMap, request, null);
		} else {
			model.addAttribute("a", time);
			PageUtils.setDetailsTime(paramMap, time);
		}
		if (request.getParameter("appId") != null && !"".equals(request.getParameter("appId"))) {
			paramMap.put("appId", request.getParameter("appId"));
		}
		List<Map> orders = orderService.findByNamedQuery("find_cocIosOrder_by_time_and_appId", paramMap, page, CocIosOrder.class, Map.class);
		Long totalNum = orderService.countByNameQuery("count_cocIosOrder_by_time", paramMap, CocIosOrder.class);
		Long population = orderService.countByNameQuery("count_cocIosOrder_by_population", paramMap, CocIosOrder.class);
		List<Map> sumIosOrderByApp = orderService.findByNamedQuery("sum_cocIosOrder_by_time_and_appId", paramMap, CocIosOrder.class, Map.class);
		
		List<Map> sumIosOrder = orderService.findByNamedQuery("sum_cocIosOrder", new CocIosOrder(), CocIosOrder.class, Map.class);
		
		List jsonMap = new ArrayList();
		
		if (sumIosOrderByApp.size() != 0) {
			model.addAttribute("sumIosOrderByApp", sumIosOrderByApp.get(0).get("amount"));
		}
		if (sumIosOrder.size() != 0) {
			model.addAttribute("sumIosOrder", sumIosOrder.get(0).get("amount"));
		}
		
		model.addAttribute("beginTime", paramMap.get("beginTime"));
		model.addAttribute("endTime", paramMap.get("endTimes"));
		
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("population", population);
		model.addAttribute("page", page);
		model.addAttribute("orders", orders);
		model.addAttribute("appId", request.getParameter("appId"));
		return "order/ios_order_list";
	}
	
	@RequestMapping(value = "{userId}/byUserId")
	public String listByUserId(@PathVariable String userId, Pagination page, HttpServletRequest request, @ModelAttribute Zone searchZone, Model model, String msg) {
		Map paramMap = new HashMap();
		PageUtils.setMapFromRequest(paramMap, request, userId);
		List<Map> orders = orderService.findByNamedQuery("find_cocIosOrder_by_time_and_appId", paramMap, page, CocIosOrder.class, Map.class);
		Long totalNum = orderService.countByNameQuery("count_cocIosOrder_by_time", paramMap, CocIosOrder.class);
		Long population = orderService.countByNameQuery("count_cocIosOrder_by_population", paramMap, CocIosOrder.class);
		List<Map> sumIosOrderByApp = orderService.findByNamedQuery("sum_cocIosOrder_by_time_and_appId", paramMap, CocIosOrder.class, Map.class);
		
		List<Map> sumIosOrder = orderService.findByNamedQuery("sum_cocIosOrder", new CocIosOrder(), CocIosOrder.class, Map.class);
		
		List jsonMap = new ArrayList();
		
		if (sumIosOrderByApp.size() != 0) {
			model.addAttribute("sumIosOrderByApp", sumIosOrderByApp.get(0).get("amount"));
		}
		if (sumIosOrder.size() != 0) {
			model.addAttribute("sumIosOrder", sumIosOrder.get(0).get("amount"));
		}
		
		model.addAttribute("beginTime", paramMap.get("beginTime"));
		model.addAttribute("endTime", paramMap.get("endTimes"));
		
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("population", population);
		model.addAttribute("page", page);
		model.addAttribute("orders", orders);
		model.addAttribute("appId", request.getParameter("appId"));
		return "order/ios_order_list";
	}
	
	@RequestMapping(value = "{time}/slotsOrder")
	public String slotsList(@PathVariable String time, Pagination page, HttpServletRequest request, @ModelAttribute Zone searchZone, Model model, String msg) {
		if (PageUtils.isEmpty(page)) {
            page = pageConfig.getRechargePage();
        }
		Map paramMap = new HashMap();
		if ("null".equals(time) || time == null) {
			PageUtils.setMapFromRequest(paramMap, request, null);
		} else {
			model.addAttribute("a", time);
			PageUtils.setDetailsTime(paramMap, time);
		}
		if (request.getParameter("appId") != null && !"".equals(request.getParameter("appId"))) {
			paramMap.put("appId", request.getParameter("appId"));
		}
		List<Map> orders = orderService.findByNamedQuery("find_saleHistory_by_time_and_appId", paramMap, page, SlotsSaleHistory.class, Map.class);
		Long totalNum = orderService.countByNameQuery("count_saleHistory_by_time", paramMap, SlotsSaleHistory.class);
		Long population = orderService.countByNameQuery("count_saleHistory_by_population", paramMap, SlotsSaleHistory.class);
		List<Map> sumIosOrderByApp = orderService.findByNamedQuery("sum_saleHistory_by_time_and_appId", paramMap, SlotsSaleHistory.class, Map.class);
		
		List<Map> sumIosOrder = orderService.findByNamedQuery("sum_saleHistory", new SlotsSaleHistory(), SlotsSaleHistory.class, Map.class);
		App app = new App();
		app.setBaseId(SlotsComponent.APP_BASE_ID);
		List<App> apps = orderService.findMulti(app);
		
		List jsonMap = new ArrayList();
		
		if (sumIosOrderByApp.size() != 0) {
			model.addAttribute("sumIosOrderByApp", sumIosOrderByApp.get(0).get("amount"));
		}
		if (sumIosOrder.size() != 0) {
			model.addAttribute("sumIosOrder", sumIosOrder.get(0).get("amount"));
		}
		
		model.addAttribute("beginTime", paramMap.get("beginTime"));
		model.addAttribute("endTime", paramMap.get("endTimes"));
		
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("population", population);
		model.addAttribute("page", page);
		model.addAttribute("orders", orders);
		model.addAttribute("apps", apps);
		model.addAttribute("appId", request.getParameter("appId"));
		return "order/slots_order_list";
	}
	
	@RequestMapping(value = "{userId}/byUserIdSlots")
	public String slotsListByUserId(@PathVariable String userId, Pagination page, HttpServletRequest request, @ModelAttribute Zone searchZone, Model model, String msg) {
		Map paramMap = new HashMap();
		PageUtils.setMapFromRequest(paramMap, request, userId);
		List<Map> orders = orderService.findByNamedQuery("find_saleHistory_by_time_and_appId", paramMap, page, SlotsSaleHistory.class, Map.class);
		Long totalNum = orderService.countByNameQuery("count_saleHistory_by_time", paramMap, SlotsSaleHistory.class);
		Long population = orderService.countByNameQuery("count_saleHistory_by_population", paramMap, SlotsSaleHistory.class);
		List<Map> sumIosOrderByApp = orderService.findByNamedQuery("sum_saleHistory_by_time_and_appId", paramMap, SlotsSaleHistory.class, Map.class);
		
		List<Map> sumIosOrder = orderService.findByNamedQuery("sum_saleHistory", new SlotsSaleHistory(), SlotsSaleHistory.class, Map.class);
		App app = new App();
		app.setBaseId(SlotsComponent.APP_BASE_ID);
		List<App> apps = orderService.findMulti(app);
		List jsonMap = new ArrayList();
		
		if (sumIosOrderByApp.size() != 0) {
			model.addAttribute("sumIosOrderByApp", sumIosOrderByApp.get(0).get("amount"));
		}
		if (sumIosOrder.size() != 0) {
			model.addAttribute("sumIosOrder", sumIosOrder.get(0).get("amount"));
		}
		
		model.addAttribute("beginTime", paramMap.get("beginTime"));
		model.addAttribute("endTime", paramMap.get("endTimes"));
		
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("population", population);
		model.addAttribute("page", page);
		model.addAttribute("orders", orders);
		model.addAttribute("apps", apps);
		model.addAttribute("appId", request.getParameter("appId"));
		return "order/slots_order_list";
	}

}
