package com.nxlh.manager.model.dbo;

import com.nxlh.manager.model.dto.OrderItemDTO;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_order_refund")
public class TbOrderRefund extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 订单号
     */
    private String orderno;

    /**
     * 第三方平台订单号
     */
    private String thirdorderno;

    /**
     * 退货原因
     */
    private Integer refundreason;

    /**
     * 退款金额
     */
    private BigDecimal refundprice;

    /**
     * 第三方退款单号
     */
    private String thirdrefundno;

    /**
     * 退货数量
     */
    private Integer refundcount;

    /**
     * 订单id
     */
    private String orderid;

    /**
     * 状态：0待审核 1同意退款 2拒绝退款
     */
    private Integer status;

    /**
     * 拒绝退款原因
     */
    private String refuse;

    /**
     * 审核时间
     */
    private Date verifytime;


    /**
     * 微信用户id
     */
    private String wxuserid;


    /**
     * 退款说明
     */
    private String refundremark;

    /**
     * 订单的商品关联
     */
    private String orderitemid;

    /**
     * 退款单号
     */
    private String refundno;

    /**
     * 返还用户积分
     */
    private BigDecimal refundscore;


    /**
     * 租赁商品的成本，如果是租赁订单
     */
    private BigDecimal rentcost;


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
     * 获取订单号
     *
     * @return orderno - 订单号
     */
    public String getOrderno() {
        return orderno;
    }

    /**
     * 设置订单号
     *
     * @param orderno 订单号
     */
    public void setOrderno(String orderno) {
        this.orderno = orderno == null ? null : orderno.trim();
    }

    /**
     * 获取第三方平台订单号
     *
     * @return thirdorderno - 第三方平台订单号
     */
    public String getThirdorderno() {
        return thirdorderno;
    }

    /**
     * 设置第三方平台订单号
     *
     * @param thirdorderno 第三方平台订单号
     */
    public void setThirdorderno(String thirdorderno) {
        this.thirdorderno = thirdorderno == null ? null : thirdorderno.trim();
    }

    /**
     * 获取退货原因
     *
     * @return refundreason - 退货原因
     */
    public Integer getRefundreason() {
        return refundreason;
    }

    /**
     * 设置退货原因
     *
     * @param refundreason 退货原因
     */
    public void setRefundreason(Integer refundreason) {
        this.refundreason = refundreason;
    }

    /**
     * 获取退款金额
     *
     * @return refundprice - 退款金额
     */
    public BigDecimal getRefundprice() {
        return refundprice;
    }

    /**
     * 设置退款金额
     *
     * @param refundprice 退款金额
     */
    public void setRefundprice(BigDecimal refundprice) {
        this.refundprice = refundprice;
    }

    /**
     * 获取第三方退款单号
     *
     * @return thirdrefundno - 第三方退款单号
     */
    public String getThirdrefundno() {
        return thirdrefundno;
    }

    /**
     * 设置第三方退款单号
     *
     * @param thirdrefundno 第三方退款单号
     */
    public void setThirdrefundno(String thirdrefundno) {
        this.thirdrefundno = thirdrefundno == null ? null : thirdrefundno.trim();
    }

    /**
     * 获取退货数量
     *
     * @return refundcount - 退货数量
     */
    public Integer getRefundcount() {
        return refundcount;
    }

    /**
     * 设置退货数量
     *
     * @param refundcount 退货数量
     */
    public void setRefundcount(Integer refundcount) {
        this.refundcount = refundcount;
    }

    /**
     * 获取订单id
     *
     * @return orderid - 订单id
     */
    public String getOrderid() {
        return orderid;
    }

    /**
     * 设置订单id
     *
     * @param orderid 订单id
     */
    public void setOrderid(String orderid) {
        this.orderid = orderid == null ? null : orderid.trim();
    }

    /**
     * 获取状态：0待审核 1同意退款 2拒绝退款
     *
     * @return status - 状态：0待审核 1同意退款 2拒绝退款
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0待审核 1同意退款 2拒绝退款
     *
     * @param status 状态：0待审核 1同意退款 2拒绝退款
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取拒绝退款原因
     *
     * @return refuse - 拒绝退款原因
     */
    public String getRefuse() {
        return refuse;
    }

    /**
     * 设置拒绝退款原因
     *
     * @param refuse 拒绝退款原因
     */
    public void setRefuse(String refuse) {
        this.refuse = refuse == null ? null : refuse.trim();
    }

    /**
     * 获取审核时间
     *
     * @return verifytime - 审核时间
     */
    public Date getVerifytime() {
        return verifytime;
    }

    /**
     * 设置审核时间
     *
     * @param verifytime 审核时间
     */
    public void setVerifytime(Date verifytime) {
        this.verifytime = verifytime;
    }


    public String getWxuserid() {
        return wxuserid;
    }

    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid;
    }

    public String getRefundremark() {
        return refundremark;
    }

    public void setRefundremark(String refundremark) {
        this.refundremark = refundremark;
    }

    public String getOrderitemid() {
        return orderitemid;
    }

    public void setOrderitemid(String orderitemid) {
        this.orderitemid = orderitemid;
    }

    public String getRefundno() {
        return refundno;
    }

    public void setRefundno(String refundno) {
        this.refundno = refundno;
    }

    public BigDecimal getRefundscore() {
        return refundscore;
    }

    public void setRefundscore(BigDecimal refundscore) {
        this.refundscore = refundscore;
    }

    public BigDecimal getRentcost() {
        return rentcost;
    }

    public void setRentcost(BigDecimal rentcost) {
        this.rentcost = rentcost;
    }

}