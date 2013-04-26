package com.wisecode.model.model.sys.entity;

import com.google.common.collect.Lists;
import com.wisecode.model.common.com.BaseEntity;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.util.List;

/**
 * 角色 Entity
 */
@Entity
@Table(name = "sys_role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Role extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long id;	 // 编号
    private String name; // 角色名称
    private User user;		// 创建者
    private String delFlag; // 删除标记（0：正常；1：删除）

    private List<User> userList = Lists.newArrayList(); // 拥有用户列表
    private List<Menu> menuList = Lists.newArrayList(); // 拥有菜单列表

    public Role() {
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public Role(Long id, String name) {
        this();
        this.id = id;
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Length(min=1, max=100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_user_role",joinColumns = {@JoinColumn(name = "role_id")},inverseJoinColumns = {@JoinColumn(name = "user_id")})
    @Where(clause = "del_flag='"+ DEL_FLAG_NORMAL +"'")
    @OrderBy("id")@Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Length(min=1, max=1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "sys_role_menu", joinColumns = { @JoinColumn(name = "role_id") }, inverseJoinColumns = { @JoinColumn(name = "menu_id") })
    @Where(clause="del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy("id") @Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }


    /**
     * 获取菜单ID列表
     * @return
     */
    @Transient
    public List<Long> getMenuIdList(){
        List<Long> list = Lists.newArrayList();
        for (Menu menu : menuList){
            list.add(menu.getId());
        }
        return list;
    }

    @Transient
    public void setMenuuIdList(List<Long> list){
        menuList = Lists.newArrayList();
        for (Long menuId : list){
            Menu menu = new Menu();
            menu.setId(menuId);
            menuList.add(menu);
        }
    }
}
