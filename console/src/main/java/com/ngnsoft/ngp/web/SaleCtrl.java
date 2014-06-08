package com.ngnsoft.ngp.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Sale;
import com.ngnsoft.ngp.service.AppService;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/sale")
public class SaleCtrl {

	private final String LIST_ACTION = "/web/sale";
	
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private AppService appService;

	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute Sale searchSale, Model model, String msg) {
		
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<Sale> sales = appService.findByNamedQuery("find_sale_with_app", searchSale, page, Sale.class);
		Long totalNum = appService.countTotalNum(searchSale);
		model.addAttribute("sales", sales);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("searchSale", searchSale);

		return "sale/sale_list";
	}
	
}
