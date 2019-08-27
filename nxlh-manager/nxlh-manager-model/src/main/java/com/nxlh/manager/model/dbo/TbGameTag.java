package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_game_tag")
public class TbGameTag extends BaseDBO {
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
     * 游戏分类id
     */
    private String tagid;

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
     * 获取游戏分类id
     *
     * @return tagid - 游戏分类id
     */
    public String getTagid() {
        return tagid;
    }

    /**
     * 设置游戏分类id
     *
     * @param tagid 游戏分类id
     */
    public void setTagid(String tagid) {
        this.tagid = tagid == null ? null : tagid.trim();
    }
}