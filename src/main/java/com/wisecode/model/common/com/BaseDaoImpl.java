package com.wisecode.model.common.com;

import com.wisecode.model.common.utils.Reflections;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * DAO支持实现类
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T> {

    /**
     * 获取实体工厂管理对象
     */
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * 实体类类型（由构造方法自动赋值）
     */
    private Class<?> entityClass;

    /**
     * 构造方法，根据实例类自动获取实体类类型
     */
    public BaseDaoImpl() {
        entityClass = Reflections.getClassGenricType(getClass());
    }

    /**
     * 获取实体工厂管理对象
     * @return
     */
    @Override
    public EntityManager getEntityManager() {
        return entityManager;
    }

    /**
     * 获取 Session
     */
    @Override
    public Session getSession() {
        return (Session) getEntityManager().getDelegate();
    }

    /**
     * 强制与数据库同步
     */
    @Override
    public void flush() {
        getSession().flush();
    }

    /**
     * 清除缓存数据
     */
    @Override
    public void clear() {
        getSession().clear();
    }

    @SuppressWarnings("unchecked")
    public List<T> find(String qlString, Object... parameter) {
        return createQuery(qlString,parameter).getResultList();
    }

    public int update(String qlString, Object... parameter) {
        return createQuery(qlString,parameter).executeUpdate();
    }


    @SuppressWarnings("unchecked")
    public Page<T> find(Page<T> page, String qlString, Object... parameter) {
        if (!page.isDisabled() && !page.isNotCount()){
            String countQlString = "select count(*)" + removeSelect(removeOrders(qlString));
            page.setCount((Long)createQuery(countQlString,parameter).getSingleResult());
                if (page.getCount() < 1){
                    return page;
                }
        }
        // order by
        String ql = qlString;
            if(StringUtils.isNotBlank(page.getOrderBy())){
                ql += " order by " + page.getOrderBy();
            }
        Query query = createQuery(ql,parameter);
        // set page
        if (!page.isDisabled()){
            query.setFirstResult(page.getFirstResult());
            query.setMaxResults(page.getMaxResults());
        }
        page.setList(query.getResultList());    return page;
    }


    @SuppressWarnings("unchecked")
    public Page<T> findBySql(Page<T> page, String sqlString, Object... parameter) {
        // get count
        if(!page.isDisabled() && !page.isNotCount()){
            String countSQLString = "select count(*)" + removeSelect(removeOrders(sqlString));
            page.setCount((Long)createQuery(countSQLString,parameter).getSingleResult());
                if (page.getCount() < 1){
                    return page;
                }
        }
        // order by
        String ql = sqlString;
            if (StringUtils.isNotBlank(page.getOrderBy())){
                ql += " order by " + page.getOrderBy();
            }
        Query query = createQuery(ql,parameter);
        // set page
        if(!page.isDisabled()){
            query.setFirstResult(page.getFirstResult());
            query.setMaxResults(page.getMaxResults());
        }
        page.setList(query.getResultList());    return page;
    }

    @SuppressWarnings("unchecked")
    public List<T> findBySql(String sqlString, Object... parameter) {
        return createQuery(sqlString,parameter).getResultList();
    }

    /**
     * 去除qlString的select字句
     * @param qlString
     * @return
     */
    private String removeSelect(String qlString){
        int beginPos = qlString.toLowerCase().indexOf("from");
        return qlString.substring(beginPos);
    }


    public Query createQuery(String sqlString, Object... parameter){
        Query query = getEntityManager().createQuery(sqlString);
        setParameter(query,parameter);
        return query;
    }

    /**
     * 设置查询参数
     */
    private void setParameter(Query query,Object... obj){
        if(obj != null){
            for (int i=0; i < obj.length; i++){
                query.setParameter(i+1,obj[i]);
            }
        }
    }

    /**
     * 去除hql的orderBy子句。
     * @param sqlString
     * @return
     */
    private String removeOrders(String sqlString){
        Pattern p = Pattern.compile("order\\\\s*by[\\\\w|\\\\W|\\\\s|\\\\S]*",Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(sqlString);
        StringBuffer sb = new StringBuffer();
        while (m.find()){
            m.appendReplacement(sb,"");
        }
        m.appendTail(sb);
        return sb.toString();
    }

}
