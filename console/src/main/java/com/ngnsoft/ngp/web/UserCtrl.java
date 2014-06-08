package com.ngnsoft.ngp.web;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.NumberUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ngnsoft.ngp.component.coc.CocComponent;
import com.ngnsoft.ngp.component.coc.model.UserData;
import com.ngnsoft.ngp.component.coc.model.UserDataBak;
import com.ngnsoft.ngp.component.coc.model.UserDataShow;
import com.ngnsoft.ngp.component.dragon.model.DragonData;
import com.ngnsoft.ngp.component.dragon.model.DragonUserFriend;
import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserAccount;
import com.ngnsoft.ngp.model.UserActivity;
import com.ngnsoft.ngp.model.UserProfile;
import com.ngnsoft.ngp.model.show.UserShow;
import com.ngnsoft.ngp.service.AppService;
import com.ngnsoft.ngp.service.UserSessionManager;
import com.ngnsoft.ngp.service.impl.DragonDataManagerImpl;
import com.ngnsoft.ngp.sp.redis.RedisImpl;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/user")
public class UserCtrl {

	private final String LIST_ACTION = "/web/user/forbid";

	private final String LIST_ACTION_ROBOT = "/web/user/robot";

	private final String LIST_ACTION_GAMEDATA = "/web/user/gameData";
	
	private final String LIST_ACTION_TOGAMEDATA = "/web/user/toAddGameData";

	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private AppService appService;
	
	@Autowired
	private DragonDataManagerImpl dragonDataMi;
	
	@Autowired
	private RedisImpl redisImpl;
	
	@Autowired
	private UserSessionManager userSessionManager;

	public static final String USER_INFO_KEY = "MainSoldier";
	
	public static final String USER_DRAGON_INFO_KEY = "MyBaseInfor";

	public static final String USER_DATA_INFO_KEY = "Key00000";

	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute User searchUser, Model model, String msg) {

		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<User> users = appService.findByNamedQuery("find_user_with_account", searchUser, page, User.class);
		Long totalNum = appService.countTotalNum(searchUser);
		model.addAttribute("users", users);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("searchUser", searchUser);

		return "user/user_list";
	}

	@RequestMapping(value = "tab")
	public String tab() {
		return "user/user_tab";
	}

	@RequestMapping(value = "toAddGameData")
	public String toAddGameData(@ModelAttribute UserDataShow searchUserDataShow, Pagination page, HttpServletRequest request, Model model, String msg) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<UserDataShow> userDataShows = new ArrayList<UserDataShow>();
		Long totalNum = 0L;
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("page", page);
		model.addAttribute("userDataShows", userDataShows);
		model.addAttribute("msg", msg);
		model.addAttribute("searchUserDataShow", searchUserDataShow);
		return "user/game_data_list";
	}

	@RequestMapping(value = "gameData")
	public String gameDataList(Pagination page, @ModelAttribute UserDataShow searchUserDataShow, HttpServletRequest request, Model model, String msg, Long userId) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		if (searchUserDataShow.getNick() == null || searchUserDataShow.getNick().equals("")) {
			searchUserDataShow.setNick(null);
		} else {
			model.addAttribute("nick", searchUserDataShow.getNick());
		}
		if (userId != null) {
			searchUserDataShow.setUserId(userId);
			model.addAttribute("userId", userId);
		}
		if (searchUserDataShow.getUserId() != null && !"".equals(searchUserDataShow.getUserId())) {
			model.addAttribute("userId", userId);
		}
		if (searchUserDataShow.getTrophy() != null && !"".equals(searchUserDataShow.getTrophy())) {
			model.addAttribute("trophy", searchUserDataShow.getTrophy());
		}
		if (searchUserDataShow.getLevel() != null && !"".equals(searchUserDataShow.getLevel())) {
			model.addAttribute("level", searchUserDataShow.getLevel());
		}
		List<UserDataShow> userDataShows = appService.findByNamedQuery("find_userData_by_userId_trophy_nick_level", searchUserDataShow, page, UserDataShow.class);
		for (UserDataShow userData : userDataShows) {
			JSONObject jsonData = JSON.parseObject(userData.getJsonData());
			Map jsonMap = JSON.parseObject(jsonData.getJSONObject(USER_DATA_INFO_KEY).toJSONString(), Map.class);
			userData.setJsonMap(jsonMap);
		}

		Long totalNum = appService.countByNameQuery("count_userData_by_userId_trophy_nick_level", searchUserDataShow, UserDataShow.class);
		model.addAttribute("userDataShows", userDataShows);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("searchUserDataShow", searchUserDataShow);
		return "user/game_data_list";
	}

	@RequestMapping(value = "{userId}/toUpdateGameData")
	public String updateGameData(@PathVariable Long userId, Model model, Pagination page, HttpServletRequest request) {
		UserData userData = appService.get(userId, UserData.class);
		JSONObject jsonData = JSON.parseObject(userData.getJsonData());
		Map jsonMap = JSON.parseObject(jsonData.getJSONObject(USER_DATA_INFO_KEY).toJSONString(), Map.class);
		model.addAttribute("jsonMap", jsonMap);
		model.addAttribute("page", page);
		model.addAttribute("userData", userData);
		return "user/game_data_update";
	}
	
	@RequestMapping(value = "{userId}/delGameData")
	public ModelAndView delGameData(@PathVariable Long userId, Model model, Pagination page, HttpServletRequest request) {
		String msg = "";
		UserData userData = appService.get(userId, UserData.class);
		UserDataBak userDataBak = new UserDataBak();
		
		if (userData != null) {
			try {
				BeanUtils.copyProperties(userData, userDataBak);
				appService.remove(userData);
				
				appService.save(userDataBak);
				
				msg = "success";
			} catch (BeansException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				msg = "failed";
			}
		}
		model.addAttribute("msg", msg);
		return new ModelAndView(new RedirectView(LIST_ACTION_TOGAMEDATA, true));
	}

	@RequestMapping(value = "updateGameData", method = RequestMethod.POST)
	public ModelAndView updateGameData(@ModelAttribute UserData searchUserData, Model model, Pagination page, HttpServletRequest request) {
		String msg = "";

		try {
			String gem = request.getParameter("gem");

			UserData userData = appService.get(searchUserData.getUserId(), UserData.class);

			JSONObject jsonData = JSON.parseObject(userData.getJsonData());
			Map jsonMap = JSON.parseObject(jsonData.getJSONObject(USER_DATA_INFO_KEY).toJSONString(), Map.class);
			jsonMap.put("Gem", gem);
			jsonMap.put("Level", searchUserData.getLevel());
			jsonMap.put("Cup", searchUserData.getTrophy());
			jsonMap.put("NickName", searchUserData.getNick());

			jsonData.put(USER_DATA_INFO_KEY, jsonMap);

			searchUserData.setJsonData(jsonData.toString());

			appService.update(searchUserData);
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("searchUserData", searchUserData);
		model.addAttribute("msg", msg);
        model.addAllAttributes(PageUtils.getPageToMap(page));
		model.addAttribute("userId", searchUserData.getUserId());
		return new ModelAndView(new RedirectView(LIST_ACTION_GAMEDATA, true));
	}

	@RequestMapping(value = "forbid")
	public String list(Pagination page, HttpServletRequest request, Model model, String id){
		Long userId = null;
		if (!NumberUtils.isDigits(id)) {
			model.addAttribute("uid", 0);
			return "user/user_forbid";
		} else {
				userId = Long.parseLong(id);
		}
		DragonData dragonData = appService.get(userId, DragonData.class);
		if (dragonData != null) {
			JSONObject jsonData = JSON.parseObject(dragonData.getJsonData());
			
			dragonDataMi.resetUserDataFieldFromJsonData(dragonData, new org.json.JSONObject(dragonData.getJsonData()));
			
			if (userId >= 100000) {
				if (jsonData.getJSONObject(USER_DRAGON_INFO_KEY) != null && !"".equals(jsonData.getJSONObject(USER_DRAGON_INFO_KEY))) {
					Map jMap = JSON.parseObject(jsonData.getJSONObject(USER_DRAGON_INFO_KEY).toJSONString(), Map.class);
					model.addAttribute("coppernum", jMap.get("coppernum"));
				}
			}
			model.addAttribute("uid", 0);
			model.addAttribute("userId", userId);
			model.addAttribute("dragonData", dragonData);
		} else {
			model.addAttribute("userId", userId);
			model.addAttribute("uid", null);
		}
		return "user/user_forbid";
	}
	
	@RequestMapping(value = "forbid/{userId}/del")
	public String delDragon(HttpServletRequest request, Model model, @PathVariable Long userId){
		DragonData dragonData = appService.get(userId, DragonData.class);
		appService.remove(dragonData);
		
		if (userId < 100000) {
			User user = appService.get(userId, User.class);
			if (user != null) {
				appService.remove(user);
			}
		}
		
		DragonUserFriend duf = new DragonUserFriend();
		Map paramMap = new HashMap();
		paramMap.put("userId", userId);
		appService.remove("remove_userFriend_by_fa_and_fb", paramMap, DragonUserFriend.class);
		model.addAttribute("msg", "success");
		return "user/user_forbid";
	}

	@RequestMapping(value = "robot")
	public String listRobot(Pagination page, HttpServletRequest request,
			@ModelAttribute User searchUser, Model model, String msg){
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<UserShow> users = appService.findByNamedQuery("find_user_by_userId", new UserShow(), page, UserShow.class);
		for (UserShow user : users) {
			DragonData dragonData = appService.get(user.getId(), DragonData.class);
			if (dragonData != null) {
				JSONObject jsonData = JSON.parseObject(dragonData.getJsonData());
				Map jsonMap = JSON.parseObject(jsonData.getJSONObject(USER_INFO_KEY).toJSONString(), Map.class);
				user.setJsonMap(jsonMap);
			}
		}

		Long totalNum = appService.countByNameQuery("count_user_by_userId", searchUser, User.class);
		model.addAttribute("users", users);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("msg", msg);
		return "user/robot_list";
	}

	@RequestMapping(value = "toAddRobot")
	public String toAdd(@ModelAttribute DragonData searchDragonData, Pagination page, HttpServletRequest request, Model model) {
		model.addAttribute("page", page);
		model.addAttribute("searchDragonData", searchDragonData);
		return "user/robot_add";
	}

	@RequestMapping(value = "addRobot", method = RequestMethod.POST)
	public ModelAndView add(@ModelAttribute DragonData searchDragonData, Pagination page, HttpServletRequest request, Model model) {
		String msg = "";

		String monsterId = request.getParameter("monsterId");
		String curLev = request.getParameter("curLev");


		String robotName = request.getParameter("robotName");
		String robotType = request.getParameter("robotType");

		try {
			User user = new User();
			appService.save("saveRobot", user, User.class);

			JSONObject jo = new JSONObject();
			jo.put("ID", monsterId);
			jo.put("curLev", curLev);
			jo.put("logintime", new Date().getTime());
			jo.put("roleid", user.getId());
			jo.put("rolelev", searchDragonData.getLevel());
			jo.put("rolename", robotName);
			jo.put("roletype", robotType);

			JSONObject jsonData = new JSONObject();
			jsonData.put("MainSoldier", jo);

			searchDragonData.setJsonData(jsonData.toString());
			searchDragonData.setUserId(user.getId());
			searchDragonData.setStatus(0);
			searchDragonData.setCoin(0L);

			appService.save(searchDragonData);

			UserProfile userProfile = new UserProfile();
			userProfile.setUserId(user.getId());
			appService.save(userProfile);

			UserAccount userAccount = new UserAccount();
			userAccount.setCoin(0L);
			userAccount.setCoinReset(0L);
			userAccount.setUserId(user.getId());
			appService.save(userAccount);

			msg = "success";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
        model.addAllAttributes(PageUtils.getPageToMap(page));
		return new ModelAndView(new RedirectView(LIST_ACTION_ROBOT, true));
	}

	@RequestMapping(value = "{id}/toUpdateRobot")
	public String update(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
		DragonData dragonData = appService.get(id, DragonData.class);
		JSONObject jsonData = JSON.parseObject(dragonData.getJsonData());
		Map jsonMap = JSON.parseObject(jsonData.getJSONObject(USER_INFO_KEY).toJSONString(), Map.class);
		model.addAttribute("jsonMap", jsonMap);
		model.addAttribute("id", id);
		model.addAttribute("page", page);
		return "user/robot_update";
	}

	@RequestMapping(value = "updateRobot", method = RequestMethod.POST)
	public ModelAndView update(@ModelAttribute DragonData searchDragonData, Model model, Pagination page, HttpServletRequest request) {
		String msg = "";

		List<DragonData> dragonDatas = appService.findMulti(new DragonData());
		
		String monsterId = request.getParameter("monsterId");
		String curLev = request.getParameter("curLev");

		String uId = request.getParameter("u_id");
		String robotName = request.getParameter("robotName");
		String robotType = request.getParameter("robotType");

		try {
			JSONObject jo = new JSONObject();
			jo.put("ID", monsterId);
			jo.put("curLev", curLev);
			jo.put("logintime", new Date().getTime());
			jo.put("roleid", uId);
			jo.put("rolelev", searchDragonData.getLevel());
			jo.put("rolename", robotName);
			jo.put("roletype", robotType);

			JSONObject jsonData = new JSONObject();
			jsonData.put("MainSoldier", jo);

			DragonData dragonData = appService.get(Long.parseLong(uId), DragonData.class);

			dragonData.setJsonData(jsonData.toString());
			dragonData.setLevel(searchDragonData.getLevel());
			appService.update(dragonData);
			msg = "success";
		} catch (NumberFormatException e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
        model.addAllAttributes(PageUtils.getPageToMap(page));
		return new ModelAndView(new RedirectView(LIST_ACTION_ROBOT, true));
	}

	@RequestMapping(value = "delRobot/{id}")
	public ModelAndView delRobot(@PathVariable Long id, Model model, Pagination page, HttpServletRequest request) {
		String msg = null;
		try {
			appService.remove(appService.get(id, DragonData.class));
			appService.remove(appService.get(id, User.class));
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
        model.addAllAttributes(PageUtils.getPageToMap(page));
		return new ModelAndView(new RedirectView(LIST_ACTION_ROBOT, true));
	}

	@RequestMapping(value = "{userId}/lists", method = RequestMethod.POST)
	public String listActivity(Pagination page, HttpServletRequest request, @PathVariable Long userId,
			@ModelAttribute UserActivity searchActivity, Model model, String msg) {

		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		searchActivity.setUserId(userId);
		List<UserActivity> activities = appService.findByNamedQuery("find_userActivity_with_app", searchActivity, page, UserActivity.class);
		Long totalNum = appService.countTotalNum(searchActivity);
		model.addAttribute("activities", activities);
		model.addAttribute("msg", msg);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("userId", userId);
		model.addAttribute("page", page);
		model.addAttribute("searchActivity", searchActivity);

		return "user/user_activity_list";
	}

	@RequestMapping(value = "updateBan")
	public ModelAndView updateBans(Pagination page, HttpServletRequest request,
			@ModelAttribute User searchUser, Model model, String msg) {
		String desc = request.getParameter("desc");
		
		if (desc.equals("coc")) {
			//update coc
			UserData userData = appService.get(searchUser.getId(), UserData.class);
			if (userData != null) {
				userData.setStatus(searchUser.getStatus());
				userData.setStatusDetail(searchUser.getStatusDetail());
				appService.update(userData);
				
				userSessionManager.expireUser(userData.getUserId(), "100002");
				
				try {
					redisImpl.del(CocComponent.REDIS_COC_BASE_ID_TOP_KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if(desc.equals("dragon")) {
			//update dragon
			DragonData dragonData = appService.get(searchUser.getId(), DragonData.class);
			if(dragonData != null) {
				dragonData.setStatus(searchUser.getStatus());
				dragonData.setStatusDetail(searchUser.getStatusDetail());
				appService.update(dragonData);
			}
		} else {
			//update user
			appService.update(searchUser);
			//update coc
			UserData userData = appService.get(searchUser.getId(), UserData.class);
			if (userData != null) {
				userData.setStatus(searchUser.getStatus());
				userData.setStatusDetail(searchUser.getStatusDetail());
				appService.update(userData);
				
				userSessionManager.expireUser(userData.getUserId(), "100002");
				
				try {
					redisImpl.del(CocComponent.REDIS_COC_BASE_ID_TOP_KEY);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			//update dragon
			DragonData dragonData = appService.get(searchUser.getId(), DragonData.class);
			if(dragonData != null) {
				dragonData.setStatus(searchUser.getStatus());
				dragonData.setStatusDetail(searchUser.getStatusDetail());
				appService.update(dragonData);
			}
			model.addAllAttributes(PageUtils.getPageToMap(page));
			return new ModelAndView(new RedirectView("/web/user/", true));
		}
		model.addAllAttributes(PageUtils.getPageToMap(page));
		model.addAttribute("userId", searchUser.getId());
		return new ModelAndView(new RedirectView(LIST_ACTION_GAMEDATA, true));
	}

	@RequestMapping(value = "{userId}/{desc}/ban", method = RequestMethod.POST)
	public String ban(Pagination page, HttpServletRequest request, @PathVariable String desc, @PathVariable Long userId,
			@ModelAttribute User searchUser, Model model, String msg) {
		model.addAttribute("userId", userId);
		model.addAttribute("desc", desc);
		model.addAttribute("page", page);
		return "user/user_ban";
	}
	
}
