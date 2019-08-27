package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_menu")
public class TbMenu extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 菜单名
     */
    private String menuname;

    /**
     * 菜单地址
     */
    private String menuurl;

    /**
     * 菜单图标
     */
    private String menuicon;

    /**
     * 父级id
     */
    private String parentid;

    /**
     * 排序
     */
    private Integer sort;


    /**
     * 菜单父类
     */
    private TbMenu parent;

    public TbMenu getParent() {
        return parent;
    }

    public void setParent(TbMenu parent) {
        this.parent = parent;
    }

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
     * 获取菜单名
     *
     * @return menuname - 菜单名
     */
    public String getMenuname() {
        return menuname;
    }

    /**
     * 设置菜单名
     *
     * @param menuname 菜单名
     */
    public void setMenuname(String menuname) {
        this.menuname = menuname == null ? null : menuname.trim();
    }

    /**
     * 获取菜单地址
     *
     * @return menuurl - 菜单地址
     */
    public String getMenuurl() {
        return menuurl;
    }

    /**
     * 设置菜单地址
     *
     * @param menuurl 菜单地址
     */
    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl == null ? null : menuurl.trim();
    }

    /**
     * 获取菜单图标
     *
     * @return menuicon - 菜单图标
     */
    public String getMenuicon() {
        return menuicon;
    }

    /**
     * 设置菜单图标
     *
     * @param menuicon 菜单图标
     */
    public void setMenuicon(String menuicon) {
        this.menuicon = menuicon == null ? null : menuicon.trim();
    }


    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}