package com.ngnsoft.ngp.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
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
import com.ngnsoft.ngp.model.ResFileShow;
import com.ngnsoft.ngp.model.ResUpdateFile;
import com.ngnsoft.ngp.service.ResFileService;
import com.ngnsoft.ngp.service.ResUpdateFileService;
import com.ngnsoft.ngp.util.PageUtils;


@Controller
@RequestMapping("/resFile")
public class ResFileCtrl {

	private final String LIST_ACTION = "/web/resFile";

	private FileStorage fs = new FileStorage(PhotoType.RES_TYPE.value(), null);
	
	@Autowired
	private PageConfig pageConfig;
	@Autowired
	private ResFileService resFileService;
	@Autowired
	private ResUpdateFileService resUpdateFileService;
	
	@RequestMapping
	public String list(Pagination page, HttpServletRequest request,
			@ModelAttribute FileStorage searchFileStorage, Model model, String msg) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getDefaultPage();
		}
		FileStorage fs = new FileStorage(PhotoType.RES_TYPE.value(), null);
		if (StringUtils.hasText(searchFileStorage.getName())) {
			fs.setName(searchFileStorage.getName());
			model.addAttribute("name", searchFileStorage.getName());
		}
		List<FileStorage> FileStorages = resFileService.findMulti(fs, page);
		Long totalNum = resFileService.countTotalNum(fs);

		model.addAttribute("resFiles", FileStorages);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("msg", msg);
		model.addAttribute("page", page);
		model.addAttribute("searchFileStorage", searchFileStorage);

		return "res/res_file_list";
	}


	@RequestMapping(value = "add")
	public void add(@ModelAttribute FileStorage searchFileStorage, HttpServletResponse response, HttpServletRequest request) {
		String msg = null;
		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
		MultipartFile uri = multipartRequest.getFile("uri");
		List<FileStorage> fs = resFileService.findMulti(new FileStorage(PhotoType.RES_TYPE.value(), null));

		//file exist
		for (int i = 0; i < fs.size(); i++) {
			if(uri.getOriginalFilename().equals(fs.get(i).getName())){
				FileStorage resFile = new FileStorage(PhotoType.RES_TYPE.value(), null);
				resFile.setId(fs.get(i).getId());
				resFile.setName(uri.getOriginalFilename());
				try {
					resFile.setData(FileCopyUtils.copyToByteArray(uri.getInputStream()));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				resFileService.update(resFile);
				msg = "success";
			}
		}

		try {
			if(msg == null){
				resFileService.addResFile(uri);
				msg = "success";
			}
		} catch (Exception e) {
			msg = "failed";
			e.printStackTrace();
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
		List<FileStorage> rFile = resFileService.findMulti(fs);
		List<ResUpdateFile> ruFiles = resUpdateFileService.findMulti(new ResUpdateFile());
		try {
			// remove ResUpdateFile info by resFileId
			for (FileStorage fileStorage : rFile) {
				if(fileStorage.getId().equals(id)){
					for (ResUpdateFile resUpdateFile : ruFiles) {
						if(resUpdateFile.getFileUrn().equals(fileStorage.getUrn())){
							resUpdateFileService.remove(resUpdateFile);
						}
					}
				}
			}
			resFileService.removeResFile(id);
			msg = "success";
		} catch (Exception e) {
			e.printStackTrace();
			msg = "failed";
		}
		model.addAttribute("msg", msg);
		return new ModelAndView(new RedirectView(LIST_ACTION, true));
	}

	@RequestMapping(value = "selectPhotos")
	public String selectPhotos(Pagination page,Model model, HttpServletRequest request) {
		return "res/res_file_add";
	}

	@RequestMapping(value = "selectResFiles")
	public String selectResFiles(@ModelAttribute FileStorage searchFileStorage, Pagination page,Model model, HttpServletRequest request) {
		if(PageUtils.isEmpty(page)){
			page = pageConfig.getWindowPage();
		}
		fs.setName(searchFileStorage.getName());
		if(!StringUtils.hasText(searchFileStorage.getName()))
			searchFileStorage.setName(null);
		String[] names = request.getParameter("resUpdateNames") == null ? new String[]{} : request.getParameter("resUpdateNames").split(",");
		List<FileStorage> resFiles = resFileService.findMulti(fs, page);
		List<ResFileShow> resFileShows = new ArrayList<ResFileShow>();
		for(FileStorage fs : resFiles) {
			ResFileShow show = new ResFileShow();
			BeanUtils.copyProperties(fs, show);
			show.setUrn(fs.getUrn());
			if(Arrays.asList(names).contains(String.valueOf(fs.getUrn()))) {
				show.setSelect(true);
			} else {
				show.setSelect(false);
			}
			resFileShows.add(show);
		}
		
		Long totalNum= resFileService.countTotalNum(fs);
		model.addAttribute("resFileShows", resFileShows);
		model.addAttribute("page", page);
		model.addAttribute("totalNum", totalNum);
		model.addAttribute("searchFileStorage", searchFileStorage);
		return "res/res_file_select";
	}
}