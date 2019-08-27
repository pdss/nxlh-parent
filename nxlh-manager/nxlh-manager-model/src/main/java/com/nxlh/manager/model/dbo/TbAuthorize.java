package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_authorize")
public class TbAuthorize extends BaseDBO {
//    @Id
//    private String id;

    /**
     * 权限名
     */
    private String authorizename;

    /**
     * 访问地址
     */
    private String path;


    private String groupname;


    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    //    private Date addtime;
//
//    private Integer isdelete;

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
     * 获取权限名
     *
     * @return authorizename - 权限名
     */
    public String getAuthorizename() {
        return authorizename;
    }

    /**
     * 设置权限名
     *
     * @param authorizename 权限名
     */
    public void setAuthorizename(String authorizename) {
        this.authorizename = authorizename == null ? null : authorizename.trim();
    }

    /**
     * 获取访问地址
     *
     * @return path - 访问地址
     */
    public String getPath() {
        return path;
    }

    /**
     * 设置访问地址
     *
     * @param path 访问地址
     */
    public void setPath(String path) {
        this.path = path == null ? null : path.trim();
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
}