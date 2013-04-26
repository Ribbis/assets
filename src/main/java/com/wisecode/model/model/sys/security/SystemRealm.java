package com.wisecode.model.model.sys.security;

import com.wisecode.model.model.sys.entity.User;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 系统安全认证类
 */
public class SystemRealm extends AuthorizingRealm {


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        return null;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        return null;
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


