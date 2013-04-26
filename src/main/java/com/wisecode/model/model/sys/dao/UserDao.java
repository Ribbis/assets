package com.wisecode.model.model.sys.dao;


import com.wisecode.model.common.com.BaseDao;
import com.wisecode.model.common.com.BaseDaoImpl;
import com.wisecode.model.model.sys.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * 用户接口
 */
public interface UserDao extends UserDaoCustom,CrudRepository<User,Long> {

    @Query("from User where loginName = ?1 and delFlag = '" + User.DEL_FLAG_NORMAL + "'")
    public User findByLoginName(String loginName);

}


interface UserDaoCustom extends BaseDao<User> {}

@Repository
class UserDaoImpl extends BaseDaoImpl<User> implements UserDaoCustom{}