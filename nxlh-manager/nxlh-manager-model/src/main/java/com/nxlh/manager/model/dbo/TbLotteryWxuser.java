package com.nxlh.manager.model.dbo;

import com.nxlh.manager.model.dto.LotteryWxUserDTO;
import com.nxlh.manager.model.dto.WxUserDTO;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "tb_lottery_wxuser")
public class TbLotteryWxuser extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 抽奖
     */
    private String lotteryid;

    /**
     * 可参与用户
     */
    private String wxuserid;

    /**
     * 参与的用户
     */
    private WxUserDTO joinuser;


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
     * 获取抽奖
     *
     * @return lotteryid - 抽奖
     */
    public String getLotteryid() {
        return lotteryid;
    }

    /**
     * 设置抽奖
     *
     * @param lotteryid 抽奖
     */
    public void setLotteryid(String lotteryid) {
        this.lotteryid = lotteryid == null ? null : lotteryid.trim();
    }

    /**
     * 获取可参与用户
     *
     * @return wxuserid - 可参与用户
     */
    public String getWxuserid() {
        return wxuserid;
    }

    /**
     * 设置可参与用户
     *
     * @param wxuserid 可参与用户
     */
    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid == null ? null : wxuserid.trim();
    }

    public WxUserDTO getJoinuser() {
        return joinuser;
    }

    public void setJoinuser(WxUserDTO joinuser) {
        this.joinuser = joinuser;
    }
}