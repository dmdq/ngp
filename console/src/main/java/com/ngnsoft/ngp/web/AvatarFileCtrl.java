package com.ngnsoft.ngp.web;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.service.AvatarFileService;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/avatarFile")
public class AvatarFileCtrl {

	private final String LIST_ACTION = "/web/avatarFile";
	
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private AvatarFileService avatarFileService;

	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute FileStorage searchFileStorage, Model model, String msg) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		List<FileStorage> FileStorages = avatarFileService.findMulti(new FileStorage(PhotoType.AVATAR_TYPE.value(), null), page);
		Long totalNum = avatarFileService.countTotalNum(new FileStorage(PhotoType.AVATAR_TYPE.value(), null));

		model.addAttribute("avatarFiles", FileStorages);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("searchFileStorage", searchFileStorage);

		return "avatar_file/avatar_file_list";
	}
	
	
	@RequestMapping(value = "add")
	public void add(@ModelAttribute FileStorage searchFileStorage, HttpServletRequest request, HttpServletResponse response) {
		String msg = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile uri = multipartRequest.getFile("urn");
		List<FileStorage> fs = avatarFileService.findMulti(new FileStorage(PhotoType.AVATAR_TYPE.value(), null));
		
		//判断图片是否存在
		for (int i = 0; i < fs.size(); i++) {
			if(uri.getOriginalFilename().equals(fs.get(i).getName())){
				msg = "exist";
			}
		}
		
		try {
			if(msg == null){
				avatarFileService.addAvatarFile(uri);
				msg = "success";
			}
		} catch (Exception e) {
			msg = "failed";
		}
		try {
			response.getWriter().print(msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@RequestMapping(value = "del/{id}")
	public ModelAndView del(@PathVariable String id, Model model,Pagination page, HttpServletRequest request) {
		String msg = null;
		try {
			avatarFileService.remove(new FileStorage(PhotoType.AVATAR_TYPE.value(), id));
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
		return new ModelAndView(new RedirectView(LIST_ACTION, true));
	}
	
	@RequestMapping(value = "selectPhotos")
	public String selectEngines(Pagination page,Model model, HttpServletRequest request) {
		return "avatar_file/avatar_file_add";
	}
}
