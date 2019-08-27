package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_coupons")
public class TbCoupons extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 领取方式1、手动  2、自动
     */
    private Integer gettype;

    /**
     * 有效期类型1、当月   2、指定日期   3、指定天数
     */
    private Integer vaildtype;

    /**
     * 指定日期
     */
    private Date vailddate;

    /**
     * 指定天数
     */
    private Integer vailddays;

    /**
     * 减免类型1、满减 2、免费
     */
    private Integer type;


    /**
     * 门槛金额
     */
    private BigDecimal limitmoney;

    /**
     * 减免金额
     */
    private BigDecimal price;

    /**
     * 有效范围 全部All  商品id
     */
    private String shopscope;

    /**
     * 领取条件1、限时  2、限量  3、限时限量   4、无
     */
    private Integer getcondition;

    /**
     * 限时 结束日期
     */
    private Date overdate;

    /**
     * 限量 数量
     */
    private Integer count;

    private TbShop shopinfo;

    /**
     * 优惠券标题
     */
    private String title;


    /**
     * 备注信息
     */
    private String remark;


    /**
     * 专属人群 0全部 1专属
     */
    private Integer usertype;

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public TbShop getShopinfo() {
        return shopinfo;
    }

    public void setShopinfo(TbShop shopinfo) {
        this.shopinfo = shopinfo;
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
     * 获取领取方式1、手动  2、自动
     *
     * @return gettype - 领取方式1、手动  2、自动
     */
    public Integer getGettype() {
        return gettype;
    }

    /**
     * 设置领取方式1、手动  2、自动
     *
     * @param gettype 领取方式1、手动  2、自动
     */
    public void setGettype(Integer gettype) {
        this.gettype = gettype;
    }

    /**
     * 获取有效期类型1、当月   2、指定日期   3、指定天数
     *
     * @return vaildtype - 有效期类型1、当月   2、指定日期   3、指定天数
     */
    public Integer getVaildtype() {
        return vaildtype;
    }

    /**
     * 设置有效期类型1、当月   2、指定日期   3、指定天数
     *
     * @param vaildtype 有效期类型1、当月   2、指定日期   3、指定天数
     */
    public void setVaildtype(Integer vaildtype) {
        this.vaildtype = vaildtype;
    }

    /**
     * 获取指定日期
     *
     * @return vailddate - 指定日期
     */
    public Date getVailddate() {
        return vailddate;
    }

    /**
     * 设置指定日期
     *
     * @param vailddate 指定日期
     */
    public void setVailddate(Date vailddate) {
        this.vailddate = vailddate;
    }

    /**
     * 获取指定天数
     *
     * @return vailddays - 指定天数
     */
    public Integer getVailddays() {
        return vailddays;
    }

    /**
     * 设置指定天数
     *
     * @param vailddays 指定天数
     */
    public void setVailddays(Integer vailddays) {
        this.vailddays = vailddays;
    }

    /**
     * 获取减免类型1、满减 2、免费
     *
     * @return type - 减免类型1、满减 2、免费
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置减免类型1、满减 2、免费
     *
     * @param type 减免类型1、满减 2、免费
     */
    public void setType(Integer type) {
        this.type = type;
    }


    /**
     * 获取减免金额
     *
     * @return price - 减免金额
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * 设置减免金额
     *
     * @param price 减免金额
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * 获取有效范围 全部All  商品id
     *
     * @return shopscope - 有效范围 全部All  商品id
     */
    public String getShopscope() {
        return shopscope;
    }

    /**
     * 设置有效范围 全部All  商品id
     *
     * @param shopscope 有效范围 全部All  商品id
     */
    public void setShopscope(String shopscope) {
        this.shopscope = shopscope == null ? null : shopscope.trim();
    }

    /**
     * 获取领取条件1、限时  2、限量  3、限时限量   4、无
     *
     * @return getcondition - 领取条件1、限时  2、限量  3、限时限量   4、无
     */
    public Integer getGetcondition() {
        return getcondition;
    }

    /**
     * 设置领取条件1、限时  2、限量  3、限时限量   4、无
     *
     * @param getcondition 领取条件1、限时  2、限量  3、限时限量   4、无
     */
    public void setGetcondition(Integer getcondition) {
        this.getcondition = getcondition;
    }

    /**
     * 获取限时 结束日期
     *
     * @return overdate - 限时 结束日期
     */
    public Date getOverdate() {
        return overdate;
    }

    /**
     * 设置限时 结束日期
     *
     * @param overdate 限时 结束日期
     */
    public void setOverdate(Date overdate) {
        this.overdate = overdate;
    }

    /**
     * 获取限量 数量
     *
     * @return count - 限量 数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置限量 数量
     *
     * @param count 限量 数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    public BigDecimal getLimitmoney() {
        return limitmoney;
    }

    public void setLimitmoney(BigDecimal limitmoney) {
        this.limitmoney = limitmoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}