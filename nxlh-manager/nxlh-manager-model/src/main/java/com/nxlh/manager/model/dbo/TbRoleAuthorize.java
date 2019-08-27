package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_role_authorize")
public class TbRoleAuthorize extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 角色id
     */
    private String roleid;

    /**
     * 权限id
     */
    private String authorizeid;

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
     * 获取权限id
     *
     * @return authorizeid - 权限id
     */
    public String getAuthorizeid() {
        return authorizeid;
    }

    /**
     * 设置权限id
     *
     * @param authorizeid 权限id
     */
    public void setAuthorizeid(String authorizeid) {
        this.authorizeid = authorizeid == null ? null : authorizeid.trim();
    }
}