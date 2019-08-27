package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_interval_sendcoupon")
public class TbIntervalSendcoupon extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 券id
     */
    private String couponid;

    /**
     * 发放人群 0全体 1指定单个用户 2指定会员等级
     */
    private Integer usertype;

    /**
     * 每次发放数量
     */
    private Integer count;

    /**
     * 会员等级
     */
    private Integer vipid;

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
     * 获取发放人群 0全体 1指定单个用户 2指定会员等级
     *
     * @return usertype - 发放人群 0全体 1指定单个用户 2指定会员等级
     */
    public Integer getUsertype() {
        return usertype;
    }

    /**
     * 设置发放人群 0全体 1指定单个用户 2指定会员等级
     *
     * @param usertype 发放人群 0全体 1指定单个用户 2指定会员等级
     */
    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    /**
     * 获取每次发放数量
     *
     * @return count - 每次发放数量
     */
    public Integer getCount() {
        return count;
    }

    /**
     * 设置每次发放数量
     *
     * @param count 每次发放数量
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * 获取会员等级
     *
     * @return vipid - 会员等级
     */
    public Integer getVipid() {
        return vipid;
    }

    /**
     * 设置会员等级
     *
     * @param vipid 会员等级
     */
    public void setVipid(Integer vipid) {
        this.vipid = vipid;
    }
}