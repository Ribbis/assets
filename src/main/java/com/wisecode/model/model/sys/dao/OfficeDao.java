package com.wisecode.model.model.sys.dao;

import com.wisecode.model.common.com.BaseDao;
import com.wisecode.model.common.com.BaseDaoImpl;
import com.wisecode.model.model.sys.entity.Office;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 *  部门DAO
 */
public interface OfficeDao extends OfficeDaoCustom, CrudRepository<Office,Long> {

    @Query("from Office where delFlag ='"+ Office.DEL_FLAG_NORMAL +"' order by code")
    public List<Office> findAllList();

    @Query("from Office where (id = ?1 or parent.id = ?1 or parentIds like ?2) and deFlag ='"+ Office.DEL_FLAG_NORMAL +"' order by code")
    public List<Office> findAllChild(Long parentId,String likeParentIds);
}


interface OfficeDaoCustom extends BaseDao<Office>{}

@Repository
class OfficeDaoImpl extends BaseDaoImpl<Office> implements OfficeDaoCustom{}