package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_game_category")
public class TbGameCategory extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 商品id
     */
    private String shopid;

    /**
     * 商品分类id
     */
    private String categoryid;

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
     * 获取商品id
     *
     * @return shopid - 商品id
     */
    public String getShopid() {
        return shopid;
    }

    /**
     * 设置商品id
     *
     * @param shopid 商品id
     */
    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }

    /**
     * 获取商品分类id
     *
     * @return categoryid - 商品分类id
     */
    public String getCategoryid() {
        return categoryid;
    }

    /**
     * 设置商品分类id
     *
     * @param categoryid 商品分类id
     */
    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid == null ? null : categoryid.trim();
    }
}