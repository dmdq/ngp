package com.ngnsoft.ngp.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.Promo;
import com.ngnsoft.ngp.service.PromoService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/promo")
public class PromoCtrl {

    private final String LIST_ACTION = "/web/promo";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private PromoService promoService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute Promo searchPromo, Model model, String msg) {

        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getPromoPageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_id"))) {
            searchPromo.setId(null);
        } else {
            searchPromo.setId(Long.parseLong(requestMap.get("p_id")));
        }
        List<Promo> promos = promoService.findMulti(searchPromo, page);
        List<Promo> ps = promoService.findMulti(new Promo());
        Long totalNum = promoService.countTotalNum(searchPromo);
        model.addAttribute("searchPromo", searchPromo);
        model.addAttribute("promos", promos);
        model.addAttribute("totalNum", totalNum);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("ps", ps);

        return "promo/promo_list";
    }

    @RequestMapping(value = "toAdd", method = RequestMethod.POST)
    public String toAdd(@ModelAttribute Promo searchPromo, Pagination page, HttpServletRequest request, Model model) {

        model.addAttribute("searchPromo", searchPromo);
        model.addAttribute("page", page);
        model.addAllAttributes(getPromoPageMap(request));
        return "promo/promo_add";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public ModelAndView add(@ModelAttribute Promo searchPromo, Pagination page, HttpServletRequest request, Model model) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile promoIcon = multipartRequest.getFile("icons");
        String msg = null;
        try {
            Promo p = new Promo();
            p.setIdx(searchPromo.getIdx());
            List<Promo> promos = promoService.findMulti(p);
            if (promos.size() == 0) {
                promoService.savePromo(searchPromo, promoIcon);
                msg = "success";
            } else {
                msg = "duplication";
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getPromoPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    @RequestMapping(value = "update/{promoId}/{order}")
    public void update(@PathVariable Long promoId, @PathVariable Integer order, HttpServletResponse response, Model model, Pagination page, HttpServletRequest request) {
        String msg = "";
        List<Promo> promos = promoService.findMulti(new Promo());
        boolean res = false;
        for (Promo promo : promos) {
            if (!promo.getId().equals(promoId) && promo.getIdx().equals(order)) {
                res = true;
                break;
            }
        }
        try {
            if (res == false) {
                Promo promo = promoService.get(promoId, Promo.class);
                promo.setIdx(order);
                promoService.update(promo);
                msg = "success";
            } else {
                msg = "duplication";
            }

        } catch (Exception e) {
            msg = "failed";
        }

        try {
            response.getWriter().write(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "{promoId}/toUpdate")
    public String update(@PathVariable Long promoId, Model model, Pagination page, HttpServletRequest request) {

        Promo promo = (Promo) promoService.get(promoId, Promo.class);
        model.addAttribute("promo", promo);
        model.addAttribute("page", page);
        model.addAllAttributes(getPromoPageMap(request));
        return "promo/promo_update";
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public ModelAndView update(@ModelAttribute Promo promo, String id, Model model, Pagination page, HttpServletRequest request) {
        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
        MultipartFile iconDoc = multipartRequest.getFile("icons");
        String msg = "";
        try {
            Promo p = new Promo();
            p.setIdx(promo.getIdx());
            List<Promo> promos = promoService.findMulti(p);

            if (promos.size() == 0) {
                promoService.updatePromo(promo, iconDoc);
                msg = "success";
            } else {
                if (promos.get(0).getId().equals(promo.getId())) {
                    promoService.updatePromo(promo, iconDoc);
                    msg = "success";
                } else {
                    msg = "duplication";
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            msg = "failed";
        }
        model.addAttribute("msg", msg);
        model.addAllAttributes(getPromoPageMap(request));
        model.addAllAttributes(PageUtils.getPageToMap(page));
        return new ModelAndView(new RedirectView(LIST_ACTION, true));
    }

    private Map<String, String> getPromoPageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        return map;
    }
}
