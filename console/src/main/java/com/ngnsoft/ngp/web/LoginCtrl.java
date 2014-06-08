package com.ngnsoft.ngp.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.model.LoginUser;
import com.ngnsoft.ngp.service.LoginUserService;
import com.ngnsoft.ngp.util.MD5Utils;

@Controller
@RequestMapping("/system")
public class LoginCtrl {
	
	@Autowired
	private LoginUserService loginUserService;
	
	@RequestMapping(value = "index")
	public String login(HttpServletRequest request) {
		if(request.getSession().getAttribute("user") != null) {
			return "/index";
		}
		return "login";
	}
	
	@RequestMapping(value = "/login")
	public String logon(HttpServletRequest request, @ModelAttribute LoginUser u) {
		if(StringUtils.hasText(u.getUsername()) && StringUtils.hasText(u.getPassword())) {
			u.setPassword(MD5Utils.getMD5(u.getPassword()));
			LoginUser user = loginUserService.getLoginUser(u);
			if(user != null) {
				request.getSession().setAttribute("user", user);
				return "/index";
			}
		}
		request.setAttribute("error", "yes");
		return "index";
	}
	
	@RequestMapping(value = "/logout")
	public String logout(HttpServletRequest request) {
		request.getSession().removeAttribute("user");
		return "login";
	}

}
