package com.wisecode.model.model.sys.entity;


import com.google.common.collect.Lists;
import com.wisecode.model.common.com.BaseEntity;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 区域 Entity
 */
@Entity
@Table(name = "sys_area")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Area extends BaseEntity{

    private static final long serialVersionUID = 1L;
    private Long id;		// 编号
    private Area parent;	// 父级编号
    private String parentIds; // 所有父级编号
    private String name; 	// 区域名称
    private String code; 	// 区域编码
    private String remarks; // 备注
    private String delFlag; // 删除标记（0：正常；1：删除）

    private List<Office> officeList = Lists.newArrayList();   // 部门列表哦

    private List<Area> areaList = Lists.newArrayList();       // 拥有子区域列表

    public Area() {
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public Area(Long id) {
        this();
        this.id = id;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull
    public Area getParent() {
        return parent;
    }

    public void setParent(Area parent) {
        this.parent = parent;
    }

    @Length(min = 1,max = 255)
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @Length(min = 1,max = 100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Length(min = 0,max = 100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Length(min=0, max=255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Length(min=1, max=1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.LAZY,mappedBy = "parent")
    @Where(clause = "del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy(value = "code")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Office> getOfficeList() {
        return officeList;
    }

    public void setOfficeList(List<Office> officeList) {
        this.officeList = officeList;
    }


    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch=FetchType.LAZY,mappedBy="parent")
    @Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy(value="code")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Area> getAreaList() {
        return areaList;
    }

    public void setAreaList(List<Area> areaList) {
        this.areaList = areaList;
    }

    @Transient
    public static void sortList(List<Area> list,List<Area> sourceList,Long parentId){
        for (int i=0; i < sourceList.size(); i++){
            Area area = sourceList.get(i);
            if (area.getParent() != null && area.getParent().getId() != null
                    && area.getParent().getId().equals(parentId)){
                list.add(area);
                for (int j=0; j < sourceList.size(); j++){
                    Area child = sourceList.get(j);
                    if(child.getParent() != null && child.getParent().getId() != null
                            && child.getParent().getId().equals(parentId)){
                        sortList(list,sourceList,area.getId());
                        break;
                    }
                }
            }
        }
    }

    @Transient
    public boolean isAdmin(){
        return isAdmin(this.id);
    }

    @Transient
    public static boolean isAdmin(Long id){
        return id != null && id.equals(1L);
    }

}
