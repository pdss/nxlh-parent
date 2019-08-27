package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_category")
public class TbCategory extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 商品分类
     */
    private String categoryname;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 置顶
     */
    private Integer istop;

    /**
     * logo
     */
    private String logo;

    /**
     * 父级id
     */
    private String parentid;


    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
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
     * 获取商品分类
     *
     * @return categoryname - 商品分类
     */
    public String getCategoryname() {
        return categoryname;
    }

    /**
     * 设置商品分类
     *
     * @param categoryname 商品分类
     */
    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname == null ? null : categoryname.trim();
    }

    /**
     * 获取排序
     *
     * @return sort - 排序
     */
    public Integer getSort() {
        return sort;
    }

    /**
     * 设置排序
     *
     * @param sort 排序
     */
    public void setSort(Integer sort) {
        this.sort = sort;
    }

    /**
     * 获取置顶
     *
     * @return istop - 置顶
     */
    public Integer getIstop() {
        return istop;
    }

    /**
     * 设置置顶
     *
     * @param istop 置顶
     */
    public void setIstop(Integer istop) {
        this.istop = istop;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }
}