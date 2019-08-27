package com.nxlh.manager.model.dbo;

import com.nxlh.manager.model.dto.ShopDTO;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_order_item")
public class TbOrderItem extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 订单id
     */
    private String orderid;

    /**
     * 订单号
     */
    private String orderno;

    /**
     * 商品id
     */
    private String productid;

    /**
     * 购买数量
     */
    private Integer buycount;

    /**
     * 商品单价
     */
    private BigDecimal productprice;

    /**
     * 商品名称
     */
    private String productname;

    /**
     * 快递单号
     */
    private String tracknumber;

    /**
     * 快递类型 0中通 1顺丰
     */
    private Integer express;

    /**
     * 发货时间,若有多个商品分开发货，则以最后一个发货时间为准
     */
    private Date deliverytime;

    /**
     * 确认收货时间，若有多个商品分开发货，以最后一个收货时间为准
     */
    private Date confirmtime;

    /**
     * 确认收货类型：1手动 2自动
     */
    private Integer confirmtype;

    /**
     * 是否发过货
     */
    private Integer istransited;

    /**
     * 商品的图片
     */
    private String productimage;

    /**
     * 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5退款中
     */
    private Integer status;

    /**
     * 第三方支付单号
     */
    private String thirdorderno;

    /**
     * 微信用户id
     */
    private String wxuserid;


    /**
     * 合计金额
     */
    private BigDecimal sumprice;


    /**
     * 积分兑换的商品，消费的积分
     */
    private BigDecimal exchangescore;

    /**
     * 订单结束时间
     */
    private Date closetime;

    /**
     * 租赁的成本
     */
    private BigDecimal rentcost;

    /**
     * 实际租赁金额,不包括成本
     */
    private BigDecimal rentprice;

    /**
     * 商品类型 1软件 2硬件
     */
    private Integer producttype;



    public Date getClosetime() {
        return closetime;
    }

    public void setClosetime(Date closetime) {
        this.closetime = closetime;
    }


    /**
     * 商品详情
     */
    private TbShop product;

    public TbShop getProduct() {
        return product;
    }

    public void setProduct(TbShop product) {
        this.product = product;
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
     * 获取购买数量
     *
     * @return buycount - 购买数量
     */
    public Integer getBuycount() {
        return buycount;
    }

    /**
     * 设置购买数量
     *
     * @param buycount 购买数量
     */
    public void setBuycount(Integer buycount) {
        this.buycount = buycount;
    }

    /**
     * 获取商品单价
     *
     * @return productprice - 商品单价
     */
    public BigDecimal getProductprice() {
        return productprice;
    }

    /**
     * 设置商品单价
     *
     * @param productprice 商品单价
     */
    public void setProductprice(BigDecimal productprice) {
        this.productprice = productprice;
    }

    /**
     * 获取商品名称
     *
     * @return productname - 商品名称
     */
    public String getProductname() {
        return productname;
    }

    /**
     * 设置商品名称
     *
     * @param productname 商品名称
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
    }

    /**
     * 获取快递单号
     *
     * @return tracknumber - 快递单号
     */
    public String getTracknumber() {
        return tracknumber;
    }

    /**
     * 设置快递单号
     *
     * @param tracknumber 快递单号
     */
    public void setTracknumber(String tracknumber) {
        this.tracknumber = tracknumber == null ? null : tracknumber.trim();
    }

    public Integer getProducttype() {
        return producttype;
    }

    public void setProducttype(Integer producttype) {
        this.producttype = producttype;
    }

    /**
     * 获取快递类型 0中通 1顺丰
     *
     * @return express - 快递类型 0中通 1顺丰
     */
    public Integer getExpress() {
        return express;
    }

    /**
     * 设置快递类型 0中通 1顺丰
     *
     * @param express 快递类型 0中通 1顺丰
     */
    public void setExpress(Integer express) {
        this.express = express;
    }

    /**
     * 获取发货时间,若有多个商品分开发货，则以最后一个发货时间为准
     *
     * @return deliverytime - 发货时间,若有多个商品分开发货，则以最后一个发货时间为准
     */
    public Date getDeliverytime() {
        return deliverytime;
    }

    /**
     * 设置发货时间,若有多个商品分开发货，则以最后一个发货时间为准
     *
     * @param deliverytime 发货时间,若有多个商品分开发货，则以最后一个发货时间为准
     */
    public void setDeliverytime(Date deliverytime) {
        this.deliverytime = deliverytime;
    }

    /**
     * 获取确认收货时间，若有多个商品分开发货，以最后一个收货时间为准
     *
     * @return confirmtime - 确认收货时间，若有多个商品分开发货，以最后一个收货时间为准
     */
    public Date getConfirmtime() {
        return confirmtime;
    }

    /**
     * 设置确认收货时间，若有多个商品分开发货，以最后一个收货时间为准
     *
     * @param confirmtime 确认收货时间，若有多个商品分开发货，以最后一个收货时间为准
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
     * 获取是否发过货
     *
     * @return istransited - 是否发过货
     */
    public Integer getIstransited() {
        return istransited;
    }

    /**
     * 设置是否发过货
     *
     * @param istransited 是否发过货
     */
    public void setIstransited(Integer istransited) {
        this.istransited = istransited;
    }

    /**
     * 获取商品的图片
     *
     * @return productimage - 商品的图片
     */
    public String getProductimage() {
        return productimage;
    }

    /**
     * 设置商品的图片
     *
     * @param productimage 商品的图片
     */
    public void setProductimage(String productimage) {
        this.productimage = productimage == null ? null : productimage.trim();
    }

    /**
     * 获取订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5退款中
     *
     * @return status - 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5退款中
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5退款中
     *
     * @param status 订单状态:1已支付，待发货,2.已发货，待收货 3已完成 4交易关闭 5退款中
     */
    public void setStatus(Integer status) {
        this.status = status;
    }


    public String getThirdorderno() {
        return thirdorderno;
    }

    public void setThirdorderno(String thirdorderno) {
        this.thirdorderno = thirdorderno;
    }

    public String getWxuserid() {
        return wxuserid;
    }

    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid;
    }

    public BigDecimal getSumprice() {
        return sumprice;
    }

    public void setSumprice(BigDecimal sumprice) {
        this.sumprice = sumprice;
    }

    public BigDecimal getExchangescore() {
        return exchangescore;
    }

    public void setExchangescore(BigDecimal exchangescore) {
        this.exchangescore = exchangescore;
    }

    public BigDecimal getRentcost() {
        return rentcost;
    }

    public void setRentcost(BigDecimal rentcost) {
        this.rentcost = rentcost;
    }

    public BigDecimal getRentprice() {
        return rentprice;
    }

    public void setRentprice(BigDecimal rentprice) {
        this.rentprice = rentprice;
    }


}