package com.wisecode.model.common.com;

import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;

/**
 * DAO支持类
 */
public interface BaseDao<T> {

    // 获取实体工厂管理对象
    public EntityManager getEntityManager();

    // 获取 Session
    public Session getSession();

    // 强制与数据库同步
    public void flush();

    // 清除缓存数据
    public void clear();

    // ============== QL Query ===========

    // Ql 分页查询
    public Page<T> find(Page<T> page,String qlString,Object... parameter);

    // QL 查询
    public List<T> find(String qlString,Object... parameter);

    // QL 更新
    public int update(String qlString,Object... parameter);

    // 创建 QL 查询对象
    public Query createQuery(String qlString,Object... parameter);

    // ============ SQL ===============

    // SQL 分页查询
    public Page<T> findBySql(Page<T> page,String sqlString,Object... parameter);

    // SQL 查询
    public List<T> findBySql(String sqlString,Object... parameter);

}
