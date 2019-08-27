package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

@Table(name = "tb_vip_shop")
public class TbVipShop extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 会员特价
     */
    private BigDecimal vipprice;


    /**
     * 排序
     */
    private Integer sort;

    /**
     * 库存数
     */
    private Integer stockcount;


    /**
     * 状态 0 下架 1上架
     */
    private Integer status;

    /**
     * 商品id
     */
    private String shopid;



    /**
     * 对应的秒杀活动id
     */
    private String activityid;

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
     * 获取会员特价
     *
     * @return vipprice - 会员特价
     */
    public BigDecimal getVipprice() {
        return vipprice;
    }

    /**
     * 设置会员特价
     *
     * @param vipprice 会员特价
     */
    public void setVipprice(BigDecimal vipprice) {
        this.vipprice = vipprice;
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
     * 获取库存数
     *
     * @return stockcount - 库存数
     */
    public Integer getStockcount() {
        return stockcount;
    }

    /**
     * 设置库存数
     *
     * @param stockcount 库存数
     */
    public void setStockcount(Integer stockcount) {
        this.stockcount = stockcount;
    }


    /**
     * 获取状态 0 下架 1上架
     *
     * @return status - 状态 0 下架 1上架
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态 0 下架 1上架
     *
     * @param status 状态 0 下架 1上架
     */
    public void setStatus(Integer status) {
        this.status = status;
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


    public String getActivityid() { return activityid; }

    public void setActivityid(String activityid) { this.activityid = activityid; }
}