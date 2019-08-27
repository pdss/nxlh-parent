package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_wxuser_recycles")
public class TbWxuserRecycles extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    private String userid;

    private String productname;

    private String orderno;

    private String express;
    private Integer accounttype;

    private String account;

    public Integer getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(Integer accounttype) {
        this.accounttype = accounttype;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    /**
     * 预估价
     */
    private BigDecimal price;

    /**
     * 最终成交价
     */
    private BigDecimal finalprice;

    /**
     * 商品缩略图
     */
    private String productimage;

    /**
     * 商品类型 1主机 2卡碟
     */
    private Integer producttype;

    /**
     * 破损度
     */
    private String damage;

    /**
     * 快递单号
     */
    private String expressno;

    /**
     * 状态：0询价中  1交易审核中  2交易成功  3交易结束
     */
    private Integer status;

    /**
     * 收款账号
     */
    private String payaccount;

    /**
     * 微信1 支付宝2
     */
    private Integer paytype;

    /**
     * 回收商品id
     */
    private String productid;

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
     * @return userid
     */
    public String getUserid() {
        return userid;
    }

    /**
     * @param userid
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * @return productname
     */
    public String getProductname() {
        return productname;
    }

    /**
     * @param productname
     */
    public void setProductname(String productname) {
        this.productname = productname == null ? null : productname.trim();
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
     * 获取最终成交价
     *
     * @return finalprice - 最终成交价
     */
    public BigDecimal getFinalprice() {
        return finalprice;
    }

    /**
     * 设置最终成交价
     *
     * @param finalprice 最终成交价
     */
    public void setFinalprice(BigDecimal finalprice) {
        this.finalprice = finalprice;
    }

    /**
     * 获取商品缩略图
     *
     * @return productimage - 商品缩略图
     */
    public String getProductimage() {
        return productimage;
    }

    /**
     * 设置商品缩略图
     *
     * @param productimage 商品缩略图
     */
    public void setProductimage(String productimage) {
        this.productimage = productimage == null ? null : productimage.trim();
    }

    /**
     * 获取商品类型 1主机 2卡碟
     *
     * @return producttype - 商品类型 1主机 2卡碟
     */
    public Integer getProducttype() {
        return producttype;
    }

    /**
     * 设置商品类型 1主机 2卡碟
     *
     * @param producttype 商品类型 1主机 2卡碟
     */
    public void setProducttype(Integer producttype) {
        this.producttype = producttype;
    }

    /**
     * 获取破损度
     *
     * @return damage - 破损度
     */
    public String getDamage() {
        return damage;
    }

    /**
     * 设置破损度
     *
     * @param damage 破损度
     */
    public void setDamage(String damage) {
        this.damage = damage == null ? null : damage.trim();
    }

    /**
     * 获取快递单号
     *
     * @return expressno - 快递单号
     */
    public String getExpressno() {
        return expressno;
    }

    /**
     * 设置快递单号
     *
     * @param expressno 快递单号
     */
    public void setExpressno(String expressno) {
        this.expressno = expressno == null ? null : expressno.trim();
    }

    /**
     * 获取状态：0询价中  1交易审核中  2交易成功  3交易结束
     *
     * @return status - 状态：0询价中  1交易审核中  2交易成功  3交易结束
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0询价中  1交易审核中  2交易成功  3交易结束
     *
     * @param status 状态：0询价中  1交易审核中  2交易成功  3交易结束
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取收款账号
     *
     * @return payaccount - 收款账号
     */
    public String getPayaccount() {
        return payaccount;
    }

    /**
     * 设置收款账号
     *
     * @param payaccount 收款账号
     */
    public void setPayaccount(String payaccount) {
        this.payaccount = payaccount == null ? null : payaccount.trim();
    }

    /**
     * 获取微信1 支付宝2
     *
     * @return paytype - 微信1 支付宝2
     */
    public Integer getPaytype() {
        return paytype;
    }

    /**
     * 设置微信1 支付宝2
     *
     * @param paytype 微信1 支付宝2
     */
    public void setPaytype(Integer paytype) {
        this.paytype = paytype;
    }

    /**
     * 获取回收商品id
     *
     * @return productid - 回收商品id
     */
    public String getProductid() {
        return productid;
    }

    /**
     * 设置回收商品id
     *
     * @param productid 回收商品id
     */
    public void setProductid(String productid) {
        this.productid = productid == null ? null : productid.trim();
    }


    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }
}