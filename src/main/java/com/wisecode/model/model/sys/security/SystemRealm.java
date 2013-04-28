package com.wisecode.model.model.sys.security;

import com.wisecode.model.common.utils.Encodes;
import com.wisecode.model.common.utils.UserUtils;
import com.wisecode.model.model.sys.entity.Menu;
import com.wisecode.model.model.sys.entity.User;
import com.wisecode.model.model.sys.server.SystemServer;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 系统安全认证类
 */
public class SystemRealm extends AuthorizingRealm {

    private SystemServer systemServer;


    /**
     * 授权查询回调函数，进行鉴权但缓存中无用户的授权信息时调用
     * @param principals
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Principal principal = (Principal) getAvailablePrincipal(principals);
        User user = systemServer.getUserByLoginName(principal.getLoginName());
        if (null != user){
            UserUtils.putCache("user",user);
            SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
            List<Menu> list = systemServer.findAllMenu();
            for (Menu menu : list){
                if (StringUtils.isNotBlank(menu.getPermission())){
                    // 添加基于Permission的权限信息
                    info.addStringPermission(menu.getPermission());
                }
            }
            // 更新登录IP和时间
            systemServer.updateUserLoginInfo(user.getId());
            return info;
        } else {
            return null;
        }
    }

    /**
     * 认证回调函数，登录时调用
     * @param token
     * @return
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        UsernamePasswordToken tokens = (UsernamePasswordToken) token;
        User user = systemServer.getUserByLoginName(tokens.getUsername());
        if (null != user){
                byte[] salt = Encodes.decodeHex(user.getPassword().substring(0,16));
                return new SimpleAuthenticationInfo(new Principal(user),
                        user.getPassword().substring(16), ByteSource.Util.bytes(salt), getName());
        } else {
            return null;
        }
    }

    public void setSystemServer(SystemServer systemServer) {
        this.systemServer = systemServer;
    }

    public static class Principal implements Serializable{
        private static final long serialVersionUID = 1L;
        private Long id;
        private String loginName;
        private String name;
        private Map<String,Object> cacheMap;

        public Principal(User user) {
            this.id = user.getId();
            this.loginName = user.getLoginName();
            this.name = user.getName();
        }

        public Long getId() {
            return id;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getName() {
            return name;
        }

        public Map<String, Object> getCacheMap() {
            if(cacheMap == null){
                cacheMap = new HashMap<String, Object>();
            }
            return cacheMap;
        }
    }
}


