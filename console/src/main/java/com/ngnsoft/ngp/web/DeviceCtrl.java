package com.ngnsoft.ngp.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Device;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.AppService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/device")
public class DeviceCtrl {
	
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private AppService appService;

	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute Device searchDevice, Model model, String msg) {
		
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<Device> devices = (List<Device>)appService.findMulti(searchDevice, page);
		Long totalNum = appService.countTotalNum(searchDevice);
		model.addAttribute("devices", devices);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("searchDevice", searchDevice);

		return "device/device_list";
	}
	
}
