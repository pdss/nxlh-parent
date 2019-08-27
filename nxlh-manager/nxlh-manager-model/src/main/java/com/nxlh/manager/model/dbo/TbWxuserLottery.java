package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_wxuser_lottery")
public class TbWxuserLottery extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 微信用户
     */
    private String wxuserid;

    /**
     * 参与的抽奖
     */
    private String lotteryid;

    /**
     * 中奖商品
     */
    private String shopid;

    /**
     * 奖品关联id
     */
    private String lotteryitemid;

    /**
     * 兑换码
     */
    private String activecode;

    /**
     * 兑换状态  0未兑换 1已兑换
     */
    private Integer status;

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
     * 获取微信用户
     *
     * @return wxuserid - 微信用户
     */
    public String getWxuserid() {
        return wxuserid;
    }

    /**
     * 设置微信用户
     *
     * @param wxuserid 微信用户
     */
    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid == null ? null : wxuserid.trim();
    }

    /**
     * 获取参与的抽奖
     *
     * @return lotteryid - 参与的抽奖
     */
    public String getLotteryid() {
        return lotteryid;
    }

    /**
     * 设置参与的抽奖
     *
     * @param lotteryid 参与的抽奖
     */
    public void setLotteryid(String lotteryid) {
        this.lotteryid = lotteryid == null ? null : lotteryid.trim();
    }

    /**
     * 获取中奖商品
     *
     * @return shopid - 中奖商品
     */
    public String getShopid() {
        return shopid;
    }

    /**
     * 设置中奖商品
     *
     * @param shopid 中奖商品
     */
    public void setShopid(String shopid) {
        this.shopid = shopid == null ? null : shopid.trim();
    }

    /**
     * 获取兑换码
     *
     * @return activecode - 兑换码
     */
    public String getActivecode() {
        return activecode;
    }

    /**
     * 设置兑换码
     *
     * @param activecode 兑换码
     */
    public void setActivecode(String activecode) {
        this.activecode = activecode == null ? null : activecode.trim();
    }

    /**
     * 获取兑换状态  0未兑换 1已兑换
     *
     * @return status - 兑换状态  0未兑换 1已兑换
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置兑换状态  0未兑换 1已兑换
     *
     * @param status 兑换状态  0未兑换 1已兑换
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getLotteryitemid() {
        return lotteryitemid;
    }

    public void setLotteryitemid(String lotteryitemid) {
        this.lotteryitemid = lotteryitemid;
    }
}