package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_seckill")
public class TbSeckill extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    private String title;

    /**
     * 0全场 1仅vip
     */
    private Integer usertype;

    /**
     * 是否计入累计消费
     */
    private Integer joinsumprice;

    /**
     * 是否计入积分
     */
    private Integer joinscore;

    /**
     * 0下线 1上线
     */
    private Integer status;

    /**
     * 1 秒杀 2 特价 3 预售
     */
    private Integer type;

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
     * @return title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取0全场 1仅vip
     *
     * @return usertype - 0全场 1仅vip
     */
    public Integer getUsertype() {
        return usertype;
    }

    /**
     * 设置0全场 1仅vip
     *
     * @param usertype 0全场 1仅vip
     */
    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    /**
     * 获取是否计入累计消费
     *
     * @return joinsumprice - 是否计入累计消费
     */
    public Integer getJoinsumprice() {
        return joinsumprice;
    }

    /**
     * 设置是否计入累计消费
     *
     * @param joinsumprice 是否计入累计消费
     */
    public void setJoinsumprice(Integer joinsumprice) {
        this.joinsumprice = joinsumprice;
    }

    /**
     * 获取是否计入积分
     *
     * @return joinscore - 是否计入积分
     */
    public Integer getJoinscore() {
        return joinscore;
    }

    /**
     * 设置是否计入积分
     *
     * @param joinscore 是否计入积分
     */
    public void setJoinscore(Integer joinscore) {
        this.joinscore = joinscore;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}