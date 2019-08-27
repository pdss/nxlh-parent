package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_admin_menu")
public class TbAdminMenu extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 角色id
     */
    private String roleid;

    /**
     * 菜单id
     */
    private String menuid;

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
     * 获取菜单id
     *
     * @return menuid - 菜单id
     */
    public String getMenuid() {
        return menuid;
    }

    /**
     * 设置菜单id
     *
     * @param menuid 菜单id
     */
    public void setMenuid(String menuid) {
        this.menuid = menuid == null ? null : menuid.trim();
    }
}