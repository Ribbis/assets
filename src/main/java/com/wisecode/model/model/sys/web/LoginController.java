package com.wisecode.model.model.sys.web;

import com.wisecode.model.common.config.Global;
import com.wisecode.model.common.utils.CookieUtils;
import com.wisecode.model.common.utils.UserUtils;
import com.wisecode.model.common.web.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 登录 Controller
 */
@Controller
@RequestMapping(value = Global.Assets_PATH)
public class LoginController extends BaseController {

    @RequestMapping(value = "login",method = RequestMethod.GET)
    public String login(HttpServletRequest request,HttpServletResponse response,Model model){
        if (UserUtils.getUser().getId() != null){
            return "redirect:" +Global.Assets_PATH;
        }
        model.addAttribute("theme",getTheme(request,response));
        return "";
    }


    private String getTheme(HttpServletRequest request, HttpServletResponse response){
        String theme = request.getParameter("theme");
        if (StringUtils.isNotBlank(theme)){
            CookieUtils.setCookie(response,"theme",theme);
        }else{
            theme = CookieUtils.getCookie(request,"theme");
        }
        return StringUtils.isNotBlank(theme)?theme:"default";
    }
}
