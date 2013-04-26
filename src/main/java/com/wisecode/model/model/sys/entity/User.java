package com.wisecode.model.model.sys.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.wisecode.model.common.com.BaseEntity;
import com.wisecode.model.common.utils.Collections;
import org.hibernate.annotations.*;
import org.hibernate.annotations.Cache;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * 用户 Entity
 */
@Entity
@Table(name = "sys_user")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;
    private Long id;		// 编号
    private Area area;		// 归属区域
    private Office office;	// 归属部门
    private String loginName;// 登录名
    private String password;// 密码
    private String name;	// 姓名
    private String email;	// 邮箱
    private String phone;	// 电话
    private String mobile;	// 手机
    private String remarks;	// 备注
    private String userType;// 用户类型
    private Date createDate;// 创建日期
    private String delFlag;	// 删除标记（0：正常；1：删除）
    private String loginIp;	// 最后登陆IP
    private Date loginDate;	// 最后登陆日期

    private List<Role> roleList = Lists.newArrayList();  // 拥有角色列表

    public User(){
        this.createDate = new Date();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public User(Long id) {
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

    @ManyToOne
    @JoinColumn(name = "area_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull(message = "所属区域不能为空")
    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    @ManyToOne
    @JoinColumn(name = "office_id")
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotNull(message = "所属部门不能为空")
    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    @Length(min = 1, max = 100)
    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    @Length(min=1, max=100)
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Length(min=1, max=100)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Email @Length(min=0, max=200)
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Length(min=0, max=200)
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Length(min=0, max=200)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Length(min=0, max=255)
    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Length(min=0, max=100)
    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @NotNull
    @JsonFormat (pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    @Length(min=1, max=1)
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+08:00")
    public Date getLoginDate() {
        return loginDate;
    }

    public void setLoginDate(Date loginDate) {
        this.loginDate = loginDate;
    }

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "sys_user_role", joinColumns = {@JoinColumn(name = "user_id")},inverseJoinColumns = {@JoinColumn(name = "role_id")})
    @Where(clause = "del_flag='"+DEL_FLAG_NORMAL+"'")
    @OrderBy("id")@Fetch(FetchMode.SUBSELECT)
    @NotFound(action = NotFoundAction.IGNORE)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @NotEmpty
    public List<Role> getRoleList() {
        return roleList;
    }

    public void setRoleList(List<Role> roleList) {
        this.roleList = roleList;
    }

    @Transient
    public List<Long> getRoleIdList(){
        List<Long> list = Lists.newArrayList();
        for (Role role : roleList){
            list.add(role.getId());
        }
        return list;
    }

    @Transient
    public void setRoleIdList(List<Long> roleIdList){
        roleIdList = Lists.newArrayList();
        for (Long roleId : roleIdList){
            Role role = new Role();
            role.setId(roleId);
            roleList.add(role);
        }
    }

    /**
     * 用户拥有角色名称字符串，多个角色名称用 ‘，’分割
     * @return
     * 非持久化属性
     */
    @Transient
    public String getRoleNames(){
        return Collections.extractToString(roleList,"name",",");
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
