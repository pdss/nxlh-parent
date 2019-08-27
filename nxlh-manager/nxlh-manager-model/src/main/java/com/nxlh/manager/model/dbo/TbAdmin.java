package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_admin")
public class TbAdmin extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 登录名
     */
    private String loginname;

    /**
     * 登录密码
     */
    private String loginpassword;

    /**
     * 姓名
     */
    private String username;

    /**
     * 角色id
     */
    private String roleid;

    /**
     * 角色名
     */
    private String rolename;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    /**
     * @return addtime
     */
    public Date getAddtime() {
        return addtime;
    }

    /**
     * @param addtime
     */
    public void setAddtime(Date addtime) {
        this.addtime = addtime;
    }

    /**
     * @return isdelete
     */
    public Integer getIsdelete() {
        return isdelete;
    }

    /**
     * @param isdelete
     */
    public void setIsdelete(Integer isdelete) {
        this.isdelete = isdelete;
    }

    /**
     * 获取登录名
     *
     * @return loginname - 登录名
     */
    public String getLoginname() {
        return loginname;
    }

    /**
     * 设置登录名
     *
     * @param loginname 登录名
     */
    public void setLoginname(String loginname) {
        this.loginname = loginname == null ? null : loginname.trim();
    }

    /**
     * 获取登录密码
     *
     * @return loginpassword - 登录密码
     */
    public String getLoginpassword() {
        return loginpassword;
    }

    /**
     * 设置登录密码
     *
     * @param loginpassword 登录密码
     */
    public void setLoginpassword(String loginpassword) {
        this.loginpassword = loginpassword == null ? null : loginpassword.trim();
    }

    /**
     * 获取姓名
     *
     * @return username - 姓名
     */
    public String getAdminname() {
        return username;
    }

    /**
     * 设置姓名
     *
     * @param username 姓名
     */
    public void setAdminname(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取角色id
     *
     * @return roleid - 角色id
     */
    public String getRoleid() {
        return roleid;
    }

    /**
     * 设置角色id
     *
     * @param roleid 角色id
     */
    public void setRoleid(String roleid) {
        this.roleid = roleid == null ? null : roleid.trim();
    }

    /**
     * 获取角色名
     *
     * @return rolename - 角色名
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * 设置角色名
     *
     * @param rolename 角色名
     */
    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }
}