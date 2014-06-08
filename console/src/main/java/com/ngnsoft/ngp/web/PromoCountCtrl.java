package com.ngnsoft.ngp.web;

import java.text.ParseException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Promo;
import com.ngnsoft.ngp.model.PromoCount;
import com.ngnsoft.ngp.service.PromoCountService;
import com.ngnsoft.ngp.util.PageUtils;



@Controller
@RequestMapping("/promoCount")
public class PromoCountCtrl {
	
	private static final String[] datePatterns = new String[]{"yyyy-MM-dd"};
	
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private PromoCountService promoCountService;

	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute PromoCount searchPromoCount, Model model) {
		
		if(request.getParameter("beginTime") != null && !"".equals(request.getParameter("beginTime"))){
			try {
				searchPromoCount.setCountDate((DateUtils.parseDate(request.getParameter("beginTime"), datePatterns)));
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<Promo> promos = promoCountService.findMulti(new Promo(), page);
		List<PromoCount> promoCounts = promoCountService.findMulti(searchPromoCount, page);
		Long totalNum = promoCountService.countTotalNum(searchPromoCount);
		model.addAttribute("searchPromoCount", searchPromoCount);
		model.addAttribute("promoCounts", promoCounts);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("promos", promos);
		model.addAttribute("page", page);

		return "promo/promo_count_list";
	}
}
