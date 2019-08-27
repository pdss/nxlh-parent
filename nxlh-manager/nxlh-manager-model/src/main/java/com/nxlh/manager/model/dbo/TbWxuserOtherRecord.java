package com.nxlh.manager.model.dbo;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_wxuser_other_record")
public class TbWxuserOtherRecord extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    private String wxuserid;

    /**
     * 累计金额对应的积分，由用户当时的会员等级折算出来
     */
    private BigDecimal score;

    /**
     * 累计金额
     */
    private BigDecimal money;

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
     * 获取累计金额对应的积分，由用户当时的会员等级折算出来
     *
     * @return score - 累计金额对应的积分，由用户当时的会员等级折算出来
     */
    public BigDecimal getScore() {
        return score;
    }

    /**
     * 设置累计金额对应的积分，由用户当时的会员等级折算出来
     *
     * @param score 累计金额对应的积分，由用户当时的会员等级折算出来
     */
    public void setScore(BigDecimal score) {
        this.score = score;
    }

    /**
     * 获取累计金额
     *
     * @return money - 累计金额
     */
    public BigDecimal getMoney() {
        return money;
    }

    /**
     * 设置累计金额
     *
     * @param money 累计金额
     */
    public void setMoney(BigDecimal money) {
        this.money = money;
    }
}