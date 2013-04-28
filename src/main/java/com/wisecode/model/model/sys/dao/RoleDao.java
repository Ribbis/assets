package com.wisecode.model.model.sys.dao;

import com.wisecode.model.common.com.BaseDao;
import com.wisecode.model.common.com.BaseDaoImpl;
import com.wisecode.model.model.sys.entity.Role;
import com.wisecode.model.model.sys.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


/**
 * 角色 DAO 类
 */
public interface RoleDao extends RoleDaoCustom, CrudRepository<Role,Long> {

    @Query("from Role where name = ?1 and deFlag ='"+ Role.DEL_FLAG_NORMAL +"'")
    Role findByName(String name);

    @Query("from Role where deFlag = '"+ Role.DEL_FLAG_NORMAL +"' order by name")
    public List<Role> findAllList();

    @Query("select distinct r from Role r, User u where r in elements (u.roleList) and r.delFlag = '"+ Role.DEL_FLAG_NORMAL +
            "' and u.delFlag ='"+ User.DEL_FLAG_NORMAL +"' and u.id = ?1 or (r.user.id = ?1 and r.delFlag ='"+
            Role.DEL_FLAG_NORMAL +"') order by r.name")
    public List<Role> findByUserId(Long userId);
}

interface RoleDaoCustom extends BaseDao<Role>{}

class RoleDaoImpl extends BaseDaoImpl<Role> implements RoleDaoCustom{}
