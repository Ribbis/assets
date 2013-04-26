package com.wisecode.model.common.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 *  Cookie 工具类
 */
public class CookieUtils {

    /**
     * 设置 Cookie 生成时间为一天
     * @param  name  名称
     * @param  value 值
     */
    public static void setCookie(HttpServletResponse response,String name,String value){
        setCookie(response, name, value,60*60*24);
    }

    /**
     * 设置 Cookie
     * @param name  名称
     * @param value 值
     * @param maxAge    生存时间（单位秒）
     */
    public static void setCookie(HttpServletResponse response,String name,String value,int maxAge){
        Cookie cookie = new Cookie(name,null);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        try {
            cookie.setValue(URLEncoder.encode(value,"utf-8"));  // 路径
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        response.addCookie(cookie);
    }

    /**
     * 获取指定Cookie的值
     * @param request
     * @param name
     * @return
     */
    public static String getCookie(HttpServletRequest request,String name){
        return getCookie(request, null,name,false);
    }

    public static String getCookie(HttpServletRequest request,HttpServletResponse response,String name){
        return getCookie(request,response,name,false);
    }

    public static String getCookie(HttpServletRequest request,HttpServletResponse response,String name,Boolean isRemove){
        String value = null;
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies){
                if(cookie.getName().equals(name)){
                    try {
                        value = URLEncoder.encode(cookie.getValue(),"utf-8");
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                    if(isRemove){
                        cookie.setMaxAge(0);
                        response.addCookie(cookie);
                    }
                }
            }
        }
        return value;
    }
}
