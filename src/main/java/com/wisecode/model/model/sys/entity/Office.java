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
@Table(name = "sys_office")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Office extends BaseEntity{

    private static final long serialVersionUID = 1L;
    private Long id;		// 编号
    private Office parent;	// 父级编号
    private String parentIds; // 所有父级编号
    private Area area;		// 归属区域
    private String code; 	// 区域编码
    private String name; 	// 区域名称
    private String remarks; // 备注
    private String delFlag; // 删除标记（0：正常；1：删除）

    private List<User> userList = Lists.newArrayList();   // 拥有用户列表
    private List<Office> childList = Lists.newArrayList();// 拥有子部门列表

    public Office(){
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public Office(Long id) {
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
    public Office getParent() {
        return parent;
    }

    public void setParent(Office parent) {
        this.parent = parent;
    }

    @Length(min=1, max=255)
    public String getParentIds() {
        return parentIds;
    }

    public void setParentIds(String parentIds) {
        this.parentIds = parentIds;
    }

    @ManyToOne
    @JoinColumn(name = "area_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @Length(min=0, max=100)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Length(min=1, max=100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.LAZY,mappedBy = "office")
    @Where(clause = "del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy(value = "id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @OneToMany(cascade = {CascadeType.PERSIST,CascadeType.MERGE,CascadeType.REMOVE},fetch = FetchType.LAZY,mappedBy = "parent")
    @Where(clause = "del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy(value = "code")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Office> getChildList() {
        return childList;
    }

    public void setChildList(List<Office> childList) {
        this.childList = childList;
    }

    @Transient
    public static void sortList(List<Office> list,List<Office> sourceList,Long parentId){
        for (int i=0; i < sourceList.size(); i++){
            Office off = sourceList.get(i);
            if(off.getParent() != null && off.getParent().getId() != null
                    && off.getParent().getId().equals(parentId)){
                list.add(off);
                // 判断是否还有子节点，有则继续取出子节点
                for (int j=0; j < sourceList.size(); j++){
                    Office child = sourceList.get(j);
                    if(child.getParent() != null && child.getParent().getId() != null
                            && child.getParent().getId().equals(parentId)){
                        sortList(list,sourceList,off.getId());
                        break;
                    }
                }
            }
        }
    }

    @Transient
    public boolean isRoot(){
        return isRoot(this.id);
    }

    @Transient
    public static boolean isRoot(Long id){
        return id != null && id.equals(1L);
    }
}
