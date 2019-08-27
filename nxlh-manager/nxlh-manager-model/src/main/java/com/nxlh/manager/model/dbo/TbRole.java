package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Table(name = "tb_role")
public class TbRole extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    private String rolename;

    private List<TbMenu> menuList;

    private List<TbMenu> allMenuList;

    public List<TbMenu> getAllMenuList() {
        return allMenuList;
    }

    public void setAllMenuList(List<TbMenu> allMenuList) {
        this.allMenuList = allMenuList;
    }

    public List<TbMenu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<TbMenu> menuList) {
        this.menuList = menuList;
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
     * @return rolename
     */
    public String getRolename() {
        return rolename;
    }

    /**
     * @param rolename
     */
    public void setRolename(String rolename) {
        this.rolename = rolename == null ? null : rolename.trim();
    }


}