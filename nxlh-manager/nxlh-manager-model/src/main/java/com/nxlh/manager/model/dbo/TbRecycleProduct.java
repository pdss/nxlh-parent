package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_recycle_product")
public class TbRecycleProduct extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 商品名称
     */
    private String shopname;

    /**
     * 预估价
     */
    private BigDecimal price;

    /**
     * 缩略图
     */
    private String thumbnails;

    /**
     * 1主机 2卡碟
     */
    private Integer type;

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
     * 获取商品名称
     *
     * @return shopname - 商品名称
     */
    public String getShopname() {
        return shopname;
    }

    /**
     * 设置商品名称
     *
     * @param shopname 商品名称
     */
    public void setShopname(String shopname) {
        this.shopname = shopname == null ? null : shopname.trim();
    }

    /**
     * 获取预估价
     *
     * @return price - 预估价
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置预估价
     *
     * @param price 预估价
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取缩略图
     *
     * @return thumbnails - 缩略图
     */
    public String getThumbnails() {
        return thumbnails;
    }

    /**
     * 设置缩略图
     *
     * @param thumbnails 缩略图
     */
    public void setThumbnails(String thumbnails) {
        this.thumbnails = thumbnails == null ? null : thumbnails.trim();
    }

    /**
     * 获取1主机 2卡碟
     *
     * @return type - 1主机 2卡碟
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置1主机 2卡碟
     *
     * @param type 1主机 2卡碟
     */
    public void setType(Integer type) {
        this.type = type;
    }
}