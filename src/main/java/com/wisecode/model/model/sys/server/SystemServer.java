package com.wisecode.model.model.sys.server;

import com.wisecode.model.common.utils.UserUtils;
import com.wisecode.model.model.sys.dao.MenuDao;
import com.wisecode.model.model.sys.dao.RoleDao;
import com.wisecode.model.model.sys.dao.UserDao;
import com.wisecode.model.model.sys.entity.Menu;
import com.wisecode.model.model.sys.entity.User;
import com.wisecode.model.model.sys.security.SystemRealm;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 系统管理，安全相关实体管理类，包括用户、角色、菜单
 */
@Service
@Transactional(readOnly = true)
public class SystemServer {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(SystemServer.class);

    public static final String HASH_1 = "SHA-1";
    public static final int HASH_NUM = 1024;
    public static final int SALT_SIZE = 8;

    @Autowired
    private UserDao userDao;
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private MenuDao menuDao;
    @Autowired
    private SystemRealm systemRealm;

    //--- User Service ---//

    public User getUserByLoginName(String loginName){
        return userDao.findByLoginName(loginName);
    }

    @Transactional
    public void updateUserLoginInfo(Long id){
        userDao.updateLoginInfo(SecurityUtils.getSubject().getSession().getHost(), new Date(), id);
    }


    //--- Menu Service ---//
    public List<Menu> findAllMenu(){
        return UserUtils.getMenuList();
    }

}
