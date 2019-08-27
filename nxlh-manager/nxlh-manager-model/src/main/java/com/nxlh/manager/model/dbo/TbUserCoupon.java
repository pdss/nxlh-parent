package com.nxlh.manager.model.dbo;

import com.nxlh.manager.model.dto.CouponsDTO;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_user_coupon")
public class TbUserCoupon extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 券id
     */
    private String couponid;

    /**
     * 使用状态:0未使用 1已使用 2已过期 
     */
    private Integer status;

    /**
     * 领取方式：1手动 2系统发放
     */
    private Integer gettype;

    /**
     * 到期日期
     */
    private Date overdate;

    /**
     * 到期天数
     */
    private Integer overdays;

    /**
     * 优惠券详情
     */
    private TbCoupons couponInfo;


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
     * 获取用户id
     *
     * @return userid - 用户id
     */
    public String getUserid() {
        return userid;
    }

    /**
     * 设置用户id
     *
     * @param userid 用户id
     */
    public void setUserid(String userid) {
        this.userid = userid == null ? null : userid.trim();
    }

    /**
     * 获取券id
     *
     * @return couponid - 券id
     */
    public String getCouponid() {
        return couponid;
    }

    /**
     * 设置券id
     *
     * @param couponid 券id
     */
    public void setCouponid(String couponid) {
        this.couponid = couponid == null ? null : couponid.trim();
    }

    /**
     * 获取使用状态:0未使用 1已使用 2已过期 
     *
     * @return status - 使用状态:0未使用 1已使用 2已过期 
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置使用状态:0未使用 1已使用 2已过期 
     *
     * @param status 使用状态:0未使用 1已使用 2已过期 
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取领取方式：1手动 2系统发放
     *
     * @return gettype - 领取方式：1手动 2系统发放
     */
    public Integer getGettype() {
        return gettype;
    }

    /**
     * 设置领取方式：1手动 2系统发放
     *
     * @param gettype 领取方式：1手动 2系统发放
     */
    public void setGettype(Integer gettype) {
        this.gettype = gettype;
    }

    /**
     * 获取到期日期
     *
     * @return overdate - 到期日期
     */
    public Date getOverdate() {
        return overdate;
    }

    /**
     * 设置到期日期
     *
     * @param overdate 到期日期
     */
    public void setOverdate(Date overdate) {
        this.overdate = overdate;
    }

    /**
     * 获取到期天数
     *
     * @return overdays - 到期天数
     */
    public Integer getOverdays() {
        return overdays;
    }

    /**
     * 设置到期天数
     *
     * @param overdays 到期天数
     */
    public void setOverdays(Integer overdays) {
        this.overdays = overdays;
    }

    public TbCoupons getCouponInfo() {
        return couponInfo;
    }

    public void setCouponInfo(TbCoupons couponInfo) {
        this.couponInfo = couponInfo;
    }
}