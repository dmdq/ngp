package com.ngnsoft.ngp.web;

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

import com.ngnsoft.ngp.component.slots.model.SlotsProduct;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.ZoneService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/product")
public class ProductCtrl {

    private final String LIST_ACTION = "/web/product";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private ZoneService zoneServiceImpl;
    
    @RequestMapping
    public String list(Pagination page, HttpServletRequest request, @ModelAttribute SlotsProduct searchSlotsProduct, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getProductPageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_id"))) {
        	searchSlotsProduct.setProductId(null);
        } else {
        	searchSlotsProduct.setProductId(requestMap.get("p_id"));
        	model.addAttribute("productId", requestMap.get("p_id"));
        }
        List<SlotsProduct> products = zoneServiceImpl.findMulti(searchSlotsProduct, SlotsProduct.class, page);
        Long totalNum = zoneServiceImpl.countTotalNum(searchSlotsProduct);
        model.addAttribute("msg", msg);
        model.addAttribute("products", products);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("page", page);
        return "product/product_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String toAdd(@ModelAttribute SlotsProduct searchSlotsProduct, Pagination page, HttpServletRequest request, Model model) {
        model.addAttribute("searchSlotsProduct", searchSlotsProduct);
        model.addAttribute("page", page);
        model.addAllAttributes(getProductPageMap(request));
        return "product/product_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute SlotsProduct searchSlotsProduct, Pagination page, HttpServletRequest request, Model model) {
        String msg = null;
        try {
        	zoneServiceImpl.save(searchSlotsProduct);
        	msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "exist";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(PageUtils.getPageToMap(page));
        model.addAllAttributes(getProductPageMap(request));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }
    
    @RequestMapping(value = "{productId}/toUpdate")
    public String update(@ModelAttribute SlotsProduct searchSlotsProduct, @PathVariable String productId, Model model, Pagination page, HttpServletRequest request) {
    	SlotsProduct product = zoneServiceImpl.get(productId, SlotsProduct.class);
        model.addAttribute("page", page);
        model.addAttribute("product", product);
        model.addAllAttributes(getProductPageMap(request));
        return "product/product_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute SlotsProduct searchSlotsProduct, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
        	zoneServiceImpl.update(searchSlotsProduct);
        	msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getProductPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }
    
    @RequestMapping(value = "{productId}/del")
    public ModelAndView del(@PathVariable String productId, Model model, Pagination page, HttpServletRequest request) {
        String msg = null;
        try {
            zoneServiceImpl.remove(productId, SlotsProduct.class);
            msg = "success";
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getProductPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }
    
    private Map<String, String> getProductPageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        return map;
    }
    
}
