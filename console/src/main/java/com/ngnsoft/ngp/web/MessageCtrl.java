package com.ngnsoft.ngp.web;

import com.ngnsoft.ngp.Engine;
import com.ngnsoft.ngp.component.DictService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.ngnsoft.ngp.constant.PageConfig;
import com.ngnsoft.ngp.model.AppShow;
import com.ngnsoft.ngp.model.Message;
import com.ngnsoft.ngp.model.Pagination;
import com.ngnsoft.ngp.model.User;
import com.ngnsoft.ngp.model.UserProfile;
import com.ngnsoft.ngp.model.show.MessageShow;
import com.ngnsoft.ngp.service.MessageService;
import com.ngnsoft.ngp.util.PageUtils;

@Controller
@RequestMapping("/message")
public class MessageCtrl {

    private final String LIST_ACTION = "/web/message";
    @Autowired
    private PageConfig pageConfig;
    @Autowired
    private MessageService messageService;

    @RequestMapping
    public String list(Pagination page, HttpServletRequest request,
            @ModelAttribute Message searchMessage, Model model, String msg) {
        if (PageUtils.isEmpty(page)) {
            page = pageConfig.getDefaultPage();
        }
        Map<String, String> requestMap = getZonePageMap(request);
        if (!StringUtils.hasText(requestMap.get("p_id"))) {
            searchMessage.setId(null);
        } else {
            searchMessage.setId(Long.parseLong(requestMap.get("p_id")));
        }
        if (!StringUtils.hasText(requestMap.get("p_status"))) {
            searchMessage.setStatus(null);
        } else {
            searchMessage.setStatus(Integer.parseInt(requestMap.get("p_status")));
        }
        List<Message> messages = messageService.findMulti(searchMessage, page);

        for (Message message : messages) {
            MessageShow messageShow = new MessageShow();
            BeanUtils.copyProperties(message, messageShow);
            User fromUser = messageService.get(message.getFrom(), User.class);
            UserProfile fromUp = messageService.get(message.getFrom(), UserProfile.class);
            if (fromUser != null) {
                messageShow.setFromNick(fromUser.getNick());
            }
            if (fromUp != null) {
                String avatar = fromUp.getAvatar();
                if (avatar != null) {
                    if (!avatar.startsWith("http://")) {
                        avatar = DictService.getAbsolutePath(avatar);
                    }
                }
                messageShow.setFromAvatar(avatar);
            }
        }

        List<Message> mess = messageService.findMulti(new Message());

        Long totalNum = messageService.countTotalNum(searchMessage);
        model.addAttribute("mess", mess);
        model.addAttribute("messages", messages);
        model.addAttribute("msg", msg);
        model.addAttribute("page", page);
        model.addAttribute("totalNum", totalNum);

        return "message/message_list";
    }

    private Map<String, String> getZonePageMap(HttpServletRequest request) {
        String p_id = request.getParameter("p_id");
        String p_status = request.getParameter("p_status");
        Map<String, String> map = new HashMap<String, String>();
        map.put("p_id", p_id);
        map.put("p_status", p_status);
        return map;
    }
}
