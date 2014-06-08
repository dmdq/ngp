package com.ngnsoft.ngp.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.enums.PhotoType;
import com.ngnsoft.ngp.model.FileStorage;
import com.ngnsoft.ngp.service.AppService;

@Controller
@RequestMapping("/download")
public class DownloadCtrl {

    public static final Logger log = Logger.getLogger(DownloadCtrl.class);
    @Autowired
    private AppService appManager;

    @RequestMapping("/file/app/{id}")
    public void iamgeView(@PathVariable String id,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        downloadImage(response, id, PhotoType.APP_TYPE);
    }

    @RequestMapping("/file/avatar/{id}")
    public void iamgeView1(@PathVariable String id,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        downloadImage(response, id, PhotoType.AVATAR_TYPE);
    }

    @RequestMapping("/file/res/{id}")
    public void iamgeView2(@PathVariable String id,
            Model model,
            HttpServletRequest request,
            HttpServletResponse response) {
        downloadImage(response, id, PhotoType.RES_TYPE);
    }

    private void downloadImage(HttpServletResponse response, String id, PhotoType photoType) {
        try {
            FileStorage fileStorage = new FileStorage(photoType.value(), id);
            fileStorage = appManager.findObject(fileStorage);
            if (fileStorage != null) {
                response.reset();
                response.setContentType("application/octet-stream");
                response.setHeader("Content-Disposition",
                        "attachment; filename=" + fileStorage.getName());
                FileCopyUtils.copy(fileStorage.getData(), response.getOutputStream());
                return;
            }
        } catch (Exception e) {
            log.error(e);
        }
        response.reset();
        response.setStatus(486);
    }
}
