package com.wisecode.model.common.utils;

import com.wisecode.model.model.sys.dao.*;
import com.wisecode.model.model.sys.entity.Menu;
import com.wisecode.model.model.sys.entity.Office;
import com.wisecode.model.model.sys.entity.User;
import com.wisecode.model.model.sys.security.SystemRealm;
import org.apache.shiro.SecurityUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *  用户工具类
 */
@Service
public class UserUtils implements ApplicationContextAware{

    private static UserDao userDao;
    private static AreaDao areaDao;
    private static MenuDao menuDao;
    private static OfficeDao officeDao;

    public static User getUser(){
        User user = (User) getCache("user");
        if(user == null){
            SystemRealm.Principal principal = (SystemRealm.Principal) SecurityUtils.getSubject().getPrincipal();
            if (principal != null){
                user = userDao.findByLoginName(principal.getLoginName());
                putCache("user",user);
            }
        }
        if (user == null){
            user = new User();
            SecurityUtils.getSubject().logout();
        }
        return user;
    }


    public static List<Menu> getMenuList(){
        @SuppressWarnings("unchecked")
        List<Menu> menuList = (List<Menu>) getCache("menuList");
        if(null == menuList){
            User user = getUser();
            if (user.isAdmin()){
                menuList =menuDao.findAllList();
            }else{
                menuList = menuDao.findbyUserId(user.getId());
            }
            putCache("menuList",menuList);
        }
        return menuList;
    }


    public static List<Office> getOfficeList(){
        @SuppressWarnings("unchecked")
        List<Office> officeList = (List<Office>) getCache("officeList");
        if (null == officeList){
            User user = getUser();
            if(user.isAdmin()){
                officeList = officeDao.findAllList();
            }else{
                officeList = officeDao.findAllChild(user.getOffice().getId(), "%,"+user.getOffice().getId()+",%");
            }
            putCache("officeList",officeList);
        }
        return officeList;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        userDao = (UserDao) applicationContext.getBean("userDao");
        areaDao = (AreaDao) applicationContext.getBean("areaDao");
        menuDao = (MenuDao) applicationContext.getBean("menuDao");
        officeDao = (OfficeDao) applicationContext.getBean("officeDao");
    }

    //    User Cache
    public static Object getCache(String key){
        Object obj = getCacheMap().get(key);
        return obj == null?null:obj;
    }

    public static void putCache(String key,Object value){
        getCacheMap().put(key,value);
    }

    public static void removeCache(String key){
        getCacheMap().remove(key);
    }

    private static Map<String,Object> getCacheMap(){
        SystemRealm.Principal principal = (SystemRealm.Principal) SecurityUtils.getSubject().getPrincipal();
        return principal != null?principal.getCacheMap():new HashMap<String, Object>();
    }
}
