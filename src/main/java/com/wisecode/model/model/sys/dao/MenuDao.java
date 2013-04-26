package com.wisecode.model.model.sys.dao;

import com.wisecode.model.common.com.BaseDao;
import com.wisecode.model.common.com.BaseDaoImpl;
import com.wisecode.model.model.sys.entity.Menu;
import com.wisecode.model.model.sys.entity.Role;
import com.wisecode.model.model.sys.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 菜单接口类
 */
public interface MenuDao extends MenuDaoCustom, CrudRepository<Menu,Long>{

    @Query("from Menu where delFlag='" + Menu.DEL_FLAG_NORMAL + "' order by sort")
    public List<Menu> findAllList();

    @Query("select distinct m from Menu m, Role r, User u where m in elements (r.menuList) and r in elements (u.roleList)" +
            " and m.delFlag='" + Menu.DEL_FLAG_NORMAL + "' and r.delFlag='" + Role.DEL_FLAG_NORMAL +
            "' and u.delFlag='" + User.DEL_FLAG_NORMAL + "' and u.id=?1 or (m.user.id=?1  and m.delFlag='" + Menu.DEL_FLAG_NORMAL +
            "') order by m.sort")
    public List<Menu> findbyUserId(Long userId);
}

interface MenuDaoCustom extends BaseDao<Menu>{}

@Repository
class MenuDaoImpl extends BaseDaoImpl<Menu> implements MenuDaoCustom{}
