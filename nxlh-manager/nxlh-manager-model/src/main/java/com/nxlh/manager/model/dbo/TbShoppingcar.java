package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_shoppingcar")
public class TbShoppingcar extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 微信用户id
     */
    private String wxuserid;

    /**
     * 商品id
     */
    private String productid;

    /**
     * 商品信息
     */
    private TbShop productInfo;

    /**
     * 数量
     */
    private Integer count;

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
     * 获取微信用户id
     *
     * @return wxuserid - 微信用户id
     */
    public String getWxuserid() {
        return wxuserid;
    }

    /**
     * 设置微信用户id
     *
     * @param wxuserid 微信用户id
     */
    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid == null ? null : wxuserid.trim();
    }

    /**
     * 获取商品id
     *
     * @return productid - 商品id
     */
    public String getProductid() {
        return productid;
    }

    /**
     * 设置商品id
     *
     * @param productid 商品id
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * 获取数量
     *
     * @return count - 数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置数量
     *
     * @param count 数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    public TbShop getProductInfo() {
        return productInfo;
    }

    public void setProductInfo(TbShop productInfo) {
        this.productInfo = productInfo;
    }
}