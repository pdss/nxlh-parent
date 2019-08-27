package com.nxlh.manager.model.dbo;

import com.alibaba.fastjson.annotation.JSONField;
import com.nxlh.manager.model.dto.CouponsDTO;
import com.nxlh.manager.model.dto.OrderItemDTO;
import com.nxlh.manager.model.dto.UserCouponDTO;
import com.nxlh.manager.model.dto.WxUserDTO;
import com.nxlh.manager.model.enums.ExpressEnums;
import com.nxlh.manager.model.enums.OrderEnums;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "tb_order")
public class TbOrder extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    public TbOrder() {
        this.orderno = "";
        this.thirdorderno = "";
        this.orderprice = BigDecimal.ZERO;
        this.payprice = BigDecimal.ZERO;
        this.wxuserid = "";
        this.status = OrderEnums.OrderStatusEnum.nopay.getValue();
        this.paytime = null;
        this.wxopenid = "";
        this.banktype = "";
        this.cheap = BigDecimal.ZERO;
        this.couponid = "";
        this.refund = 0;
        this.remark = "";
        this.express = ExpressEnums.Express.zto.getValue();
        this.receiveaddress = "";
        this.receivearea = "";
        this.receivecity = "";
        this.receivephoneprefix = "";
        this.receiveprovince = "";
        this.receivername = "";
        this.receiverphone = "";
        this.confirmtype = 0;
        this.score = BigDecimal.ZERO;
        this.expressprice = BigDecimal.ZERO;
        this.ordertype = 0;
        this.istransited = 0;
        this.coupontype = 0;
        this.exchangescore = BigDecimal.ZERO;
        this.payscore = BigDecimal.ZERO;
        this.nowprice = BigDecimal.ZERO;

    }


    /**
     * 订单号
     */
    private String orderno;

    /**
     * 第三方平台订单号
     */
    private String thirdorderno;

    /**
     * 订单金额
     */
    private BigDecimal orderprice;

    /**
     * 实际支付金额
     */
    private BigDecimal payprice;

    /**
     * 微信用户id
     */
    private String wxuserid;

    /**
     * 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5部分退款中 6退款中 7部分退款完成
     */
    private Integer status;

    /**
     * 实际支付时间
     */
    private Date paytime;

    @JsonIgnore
    private String wxopenid;

    /**
     * 银行
     */
    private String banktype;

    /**
     * 优惠金额
     */
    private BigDecimal cheap;

    /**
     * 优惠券id
     */
    private String couponid;

    /**
     * 退款标记 0无退款 1部分退款 2全部退款
     */
    private Integer refund;


    /**
     * 备注信息
     */
    private String remark;


    /**
     * 快递类型
     */
    private Integer express;

    /**
     * 发货时间
     */
    private Date deliverytime;

    /**
     * 收货详细地址
     */
    private String receiveaddress;

    /**
     * 收货人
     */
    private String receivername;

    /**
     * 收货人电话
     */
    private String receiverphone;

    /**
     * 收货省份
     */
    private String receiveprovince;

    /**
     * 收货城市
     */
    private String receivecity;

    /**
     * 收货区县
     */
    private String receivearea;

    /**
     * 收货电话区号
     */
    private String receivephoneprefix;

    /**
     * 确认收货时间
     */
    private Date confirmtime;

    /**
     * 确认收货类型：1手动 2自动
     */
    private Integer confirmtype;

    /**
     * 对应的积分，消费所得积分
     */
    private BigDecimal score;

    /**
     * 运费
     */
    private BigDecimal expressprice;

    /**
     * 订单类型 1消费订单 2.积分兑换 3.免费赠送 4出租
     */
    private Integer ordertype;

    /**
     * 是否已经发过货(已生成运单号)
     */
    private Integer istransited;

    /**
     * 优惠券类型 1满减 2免费券 0没使用
     */
    private Integer coupontype;


    /**
     * 兑换商品花费的积分
     */
    private BigDecimal exchangescore;


    /**
     * 积分抵扣部分金额，积分数
     */
    private BigDecimal payscore;


    /**
     * 交易关闭时间
     */
    private Date closetime;

    /**
     * 微信用户
     */

    @JSONField(serialize = false)
    private WxUserDTO wxuser;

    /**
     * 商品详情
     */
    private List<OrderItemDTO> orderItems;

    /**
     * 优惠券
     */
    private TbCoupons coupon;

    /**
     * 用户关联的优惠券
     */
    private UserCouponDTO usercoupon;

    /**
     * 退款记录
     */
    private List<TbOrderRefund> refundItems;

    /**
     * 除去退款金额的实际支付金额
     */
    private BigDecimal nowprice;

    /**
     * 每日租金
     */
    private  BigDecimal rentpricebyday;

    /**
     * 租金周期
     */
    private Integer rentterm;

    /**
     * 租赁周期对应的天数
     */
    private  Integer rentdays;


    public Integer getRentdays() {
        return rentdays;
    }

    public void setRentdays(Integer rentdays) {
        this.rentdays = rentdays;
    }

    public Integer getRentterm() {
        return rentterm;
    }

    public void setRentterm(Integer rentterm) {
        this.rentterm = rentterm;
    }

    public BigDecimal getNowprice() {
        return nowprice;
    }

    public BigDecimal getRentpricebyday() {
        return rentpricebyday;
    }

    public void setRentpricebyday(BigDecimal rentpricebyday) {
        this.rentpricebyday = rentpricebyday;
    }

    public void setNowprice(BigDecimal nowprice) {
        this.nowprice = nowprice;
    }

    public TbCoupons getCoupon() {
        return coupon;
    }

    public void setCoupon(TbCoupons coupon) {
        this.coupon = coupon;
    }

    public List<OrderItemDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemDTO> orderItems) {
        this.orderItems = orderItems;
    }

    public Integer getIstransited() {
        return istransited;
    }

    public void setIstransited(Integer istransited) {
        this.istransited = istransited;
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
     * 获取订单金额
     *
     * @return orderprice - 订单金额
     */
    public BigDecimal getOrderprice() {
        return orderprice;
    }

    /**
     * 设置订单金额
     *
     * @param orderprice 订单金额
     */
    public void setOrderprice(BigDecimal orderprice) {
        this.orderprice = orderprice;
    }

    /**
     * 获取实际支付金额
     *
     * @return payprice - 实际支付金额
     */
    public BigDecimal getPayprice() {
        return payprice;
    }

    /**
     * 设置实际支付金额
     *
     * @param payprice 实际支付金额
     */
    public void setPayprice(BigDecimal payprice) {
        this.payprice = payprice;
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
     * 获取订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5部分退款中 6退款中 7部分退款完成
     *
     * @return status - 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5部分退款中 6退款中 7部分退款完成
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5部分退款中 6退款中 7部分退款完成
     *
     * @param status 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5部分退款中 6退款中 7部分退款完成
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取实际支付时间
     *
     * @return paytime - 实际支付时间
     */
    public Date getPaytime() {
        return paytime;
    }

    /**
     * 设置实际支付时间
     *
     * @param paytime 实际支付时间
     */
    public void setPaytime(Date paytime) {
        this.paytime = paytime;
    }

    /**
     * @return wxopenid
     */
    public String getWxopenid() {
        return wxopenid;
    }

    /**
     * @param wxopenid
     */
    public void setWxopenid(String wxopenid) {
        this.wxopenid = wxopenid == null ? null : wxopenid.trim();
    }

    /**
     * 获取银行
     *
     * @return banktype - 银行
     */
    public String getBanktype() {
        return banktype;
    }

    /**
     * 设置银行
     *
     * @param banktype 银行
     */
    public void setBanktype(String banktype) {
        this.banktype = banktype == null ? null : banktype.trim();
    }

    /**
     * 获取优惠金额
     *
     * @return cheap - 优惠金额
     */
    public BigDecimal getCheap() {
        return cheap;
    }

    /**
     * 设置优惠金额
     *
     * @param cheap 优惠金额
     */
    public void setCheap(BigDecimal cheap) {
        this.cheap = cheap;
    }

    /**
     * 获取优惠券id
     *
     * @return couponid - 优惠券id
     */
    public String getCouponid() {
        return couponid;
    }

    /**
     * 设置优惠券id
     *
     * @param couponid 优惠券id
     */
    public void setCouponid(String couponid) {
        this.couponid = couponid == null ? null : couponid.trim();
    }

    /**
     * 获取退款标记 0无退款 1部分退款 2全部退款
     *
     * @return refund - 退款标记 0无退款 1部分退款 2全部退款
     */
    public Integer getRefund() {
        return refund;
    }

    /**
     * 设置退款标记 0无退款 1部分退款 2全部退款
     *
     * @param refund 退款标记 0无退款 1部分退款 2全部退款
     */
    public void setRefund(Integer refund) {
        this.refund = refund;
    }


    /**
     * 获取备注信息
     *
     * @return remark - 备注信息
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注信息
     *
     * @param remark 备注信息
     */
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    /**
     * 获取快递类型
     *
     * @return express - 快递类型
     */
    public Integer getExpress() {
        return express;
    }

    /**
     * 设置快递类型
     *
     * @param express 快递类型
     */
    public void setExpress(Integer express) {
        this.express = express;
    }

    /**
     * 获取发货时间
     *
     * @return deliverytime - 发货时间
     */
    public Date getDeliverytime() {
        return deliverytime;
    }

    /**
     * 设置发货时间
     *
     * @param deliverytime 发货时间
     */
    public void setDeliverytime(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    /**
     * 获取收货详细地址
     *
     * @return receiveaddress - 收货详细地址
     */
    public String getReceiveaddress() {
        return receiveaddress;
    }

    /**
     * 设置收货详细地址
     *
     * @param receiveaddress 收货详细地址
     */
    public void setReceiveaddress(String receiveaddress) {
        this.receiveaddress = receiveaddress == null ? null : receiveaddress.trim();
    }

    /**
     * 获取收货人
     *
     * @return receivername - 收货人
     */
    public String getReceivername() {
        return receivername;
    }

    /**
     * 设置收货人
     *
     * @param receivername 收货人
     */
    public void setReceivername(String receivername) {
        this.receivername = receivername == null ? null : receivername.trim();
    }

    /**
     * 获取收货人电话
     *
     * @return receiverphone - 收货人电话
     */
    public String getReceiverphone() {
        return receiverphone;
    }

    /**
     * 设置收货人电话
     *
     * @param receiverphone 收货人电话
     */
    public void setReceiverphone(String receiverphone) {
        this.receiverphone = receiverphone == null ? null : receiverphone.trim();
    }

    /**
     * 获取收货省份
     *
     * @return receiveprovince - 收货省份
     */
    public String getReceiveprovince() {
        return receiveprovince;
    }

    /**
     * 设置收货省份
     *
     * @param receiveprovince 收货省份
     */
    public void setReceiveprovince(String receiveprovince) {
        this.receiveprovince = receiveprovince == null ? null : receiveprovince.trim();
    }

    /**
     * 获取收货城市
     *
     * @return receivecity - 收货城市
     */
    public String getReceivecity() {
        return receivecity;
    }

    /**
     * 设置收货城市
     *
     * @param receivecity 收货城市
     */
    public void setReceivecity(String receivecity) {
        this.receivecity = receivecity == null ? null : receivecity.trim();
    }

    /**
     * 获取收货区县
     *
     * @return receivearea - 收货区县
     */
    public String getReceivearea() {
        return receivearea;
    }

    /**
     * 设置收货区县
     *
     * @param receivearea 收货区县
     */
    public void setReceivearea(String receivearea) {
        this.receivearea = receivearea == null ? null : receivearea.trim();
    }

    /**
     * 获取收货电话区号
     *
     * @return receivephoneprefix - 收货电话区号
     */
    public String getReceivephoneprefix() {
        return receivephoneprefix;
    }

    /**
     * 设置收货电话区号
     *
     * @param receivephoneprefix 收货电话区号
     */
    public void setReceivephoneprefix(String receivephoneprefix) {
        this.receivephoneprefix = receivephoneprefix == null ? null : receivephoneprefix.trim();
    }

    /**
     * 获取确认收货时间
     *
     * @return confirmtime - 确认收货时间
     */
    public Date getConfirmtime() {
        return confirmtime;
    }

    /**
     * 设置确认收货时间
     *
     * @param confirmtime 确认收货时间
     */
    public void setConfirmtime(Date confirmtime) {
        this.confirmtime = confirmtime;
    }

    /**
     * 获取确认收货类型：1手动 2自动
     *
     * @return confirmtype - 确认收货类型：1手动 2自动
     */
    public Integer getConfirmtype() {
        return confirmtype;
    }

    /**
     * 设置确认收货类型：1手动 2自动
     *
     * @param confirmtype 确认收货类型：1手动 2自动
     */
    public void setConfirmtype(Integer confirmtype) {
        this.confirmtype = confirmtype;
    }

    /**
     * 获取对应的积分
     *
     * @return score - 对应的积分
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * 设置对应的积分
     *
     * @param score 对应的积分
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 获取运费
     *
     * @return expressprice - 运费
     */
    public BigDecimal getExpressprice() {
        return expressprice;
    }

    /**
     * 设置运费
     *
     * @param expressprice 运费
     */
    public void setExpressprice(BigDecimal expressprice) {
        this.expressprice = expressprice;
    }

    /**
     * 获取订单类型 1消费订单 2.积分兑换 3.免费赠送 4出租
     *
     * @return ordertype - 订单类型 1消费订单 2.积分兑换 3.免费赠送 4出租
     */
    public Integer getOrdertype() {
        return ordertype;
    }

    /**
     * 设置订单类型 1消费订单 2.积分兑换 3.免费赠送 4出租
     *
     * @param ordertype 订单类型 1消费订单 2.积分兑换 3.免费赠送 4出租
     */
    public void setOrdertype(Integer ordertype) {
        this.ordertype = ordertype;
    }


    public WxUserDTO getWxuser() {
        return wxuser;
    }

    public void setWxuser(WxUserDTO wxuser) {
        this.wxuser = wxuser;
    }


    public List<TbOrderRefund> getRefundItems() {
        return refundItems;
    }

    public void setRefundItems(List<TbOrderRefund> refundItems) {
        this.refundItems = refundItems;
    }


    public Integer getCoupontype() {
        return coupontype;
    }

    public void setCoupontype(Integer coupontype) {
        this.coupontype = coupontype;
    }

    public UserCouponDTO getUsercoupon() {
        return usercoupon;
    }

    public void setUsercoupon(UserCouponDTO usercoupon) {
        this.usercoupon = usercoupon;
    }

    public BigDecimal getExchangescore() {
        return exchangescore;
    }

    public void setExchangescore(BigDecimal exchangescore) {
        this.exchangescore = exchangescore;
    }

    public BigDecimal getPayscore() {
        return payscore;
    }

    public void setPayscore(BigDecimal payscore) {
        this.payscore = payscore;
    }


    public Date getClosetime() {
        return closetime;
    }

    public void setClosetime(Date closetime) {
        this.closetime = closetime;
    }
}