package com.nxlh.manager.model.dbo;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_web_order")
@NoArgsConstructor
@AllArgsConstructor
public class TbWebOrder extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    private String orderno;

    private Long orderid;

    private Long orderitemid;

    private BigDecimal orderprice;

    private BigDecimal orderpayprice;

    private BigDecimal itempayprice;

    private Integer ordertype;

    private Integer ordertype2;

    private String unionid;

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public Integer getOrdertype2() {
        return ordertype2;
    }

    public void setOrdertype2(Integer ordertype2) {
        this.ordertype2 = ordertype2;
    }

    /**
     * 7、交易取消 1、待发货 2、已发货 4、关闭
     */
    private Integer status;

    private Integer producttype;

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
     * @return orderno
     */
    public String getOrderno() {
        return orderno;
    }

    /**
     * @param orderno
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    /**
     * @return orderid
     */
    public Long getOrderid() {
        return orderid;
    }

    /**
     * @param orderid
     */
    public void setOrderid(Long orderid) {
        this.orderid = orderid;
    }

    /**
     * @return orderitemid
     */
    public Long getOrderitemid() {
        return orderitemid;
    }

    /**
     * @param orderitemid
     */
    public void setOrderitemid(Long orderitemid) {
        this.orderitemid = orderitemid;
    }

    /**
     * @return orderprice
     */
    public BigDecimal getOrderprice() {
        return orderprice;
    }

    /**
     * @param orderprice
     */
    public void setOrderprice(BigDecimal orderprice) {
        this.orderprice = orderprice;
    }

    /**
     * @return orderpayprice
     */
    public BigDecimal getOrderpayprice() {
        return orderpayprice;
    }

    /**
     * @param orderpayprice
     */
    public void setOrderpayprice(BigDecimal orderpayprice) {
        this.orderpayprice = orderpayprice;
    }

    /**
     * @return itempayprice
     */
    public BigDecimal getItempayprice() {
        return itempayprice;
    }

    /**
     * @param itempayprice
     */
    public void setItempayprice(BigDecimal itempayprice) {
        this.itempayprice = itempayprice;
    }

    /**
     * @return ordertype
     */
    public Integer getOrdertype() {
        return ordertype;
    }

    /**
     * @param ordertype
     */
    public void setOrdertype(Integer ordertype) {
        this.ordertype = ordertype;
    }

    /**
     * 获取7、交易取消 1、待发货 2、已发货 4、关闭
     *
     * @return status - 7、交易取消 1、待发货 2、已发货 4、关闭
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置7、交易取消 1、待发货 2、已发货 4、关闭
     *
     * @param status 7、交易取消 1、待发货 2、已发货 4、关闭
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return producttype
     */
    public Integer getProducttype() {
        return producttype;
    }

    /**
     * @param producttype
     */
    public void setProducttype(Integer producttype) {
        this.producttype = producttype;
    }
}