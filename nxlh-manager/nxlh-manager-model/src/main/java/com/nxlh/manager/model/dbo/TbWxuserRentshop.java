package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_wxuser_rentshop")
public class TbWxuserRentshop extends BaseDBO {
    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    private String wxuserid;

    private String productid;

    /**
     * 状态: 1租赁中 2已归还 3未归还，自动买断
     */
    private Integer status;

    /**
     * 归还时间
     */
    private Date backtime;

    /**
     * 租赁订单id
     */
    private String orderid;

    /**
     * 逾期金额
     */
    private BigDecimal overdueprice;

    /**
     * 逾期天数
     */
    private Integer overduedays;

    /**
     * 商品类型
     */
    private Integer protype;

    private Date enddate;

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Integer getProtype() {
        return protype;
    }

    public void setProtype(Integer protype) {
        this.protype = protype;
    }

    public BigDecimal getOverdueprice() {
        return overdueprice;
    }

    public void setOverdueprice(BigDecimal overdueprice) {
        this.overdueprice = overdueprice;
    }

    public Integer getOverduedays() {
        return overduedays;
    }

    public void setOverduedays(Integer overduedays) {
        this.overduedays = overduedays;
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
     * @return wxuserid
     */
    public String getWxuserid() {
        return wxuserid;
    }

    /**
     * @param wxuserid
     */
    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid == null ? null : wxuserid.trim();
    }

    /**
     * @return productid
     */
    public String getProductid() {
        return productid;
    }

    /**
     * @param productid
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }

    /**
     * 获取状态: 1租赁中 2已归还 3未归还，自动买断
     *
     * @return status - 状态: 1租赁中 2已归还 3未归还，自动买断
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态: 1租赁中 2已归还 3未归还，自动买断
     *
     * @param status 状态: 1租赁中 2已归还 3未归还，自动买断
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取归还时间
     *
     * @return backtime - 归还时间
     */
    public Date getBacktime() {
        return backtime;
    }

    /**
     * 设置归还时间
     *
     * @param backtime 归还时间
     */
    public void setBacktime(Date backtime) {
        this.backtime = backtime;
    }

    /**
     * 获取租赁订单id
     *
     * @return orderid - 租赁订单id
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置租赁订单id
     *
     * @param orderid 租赁订单id
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }
}