package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_vip_lower")
public class TbVipLower extends BaseDBO {

    private String userid;

    /**
     * 现等级
     */
    private Integer beforelevel;

    /**
     * 更新后的等级
     */
    private Integer afterlevel;

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


    public Integer getBeforelevel() {
        return beforelevel;
    }

    public void setBeforelevel(Integer beforelevel) {
        this.beforelevel = beforelevel;
    }

    public Integer getAfterlevel() {
        return afterlevel;
    }

    public void setAfterlevel(Integer afterlevel) {
        this.afterlevel = afterlevel;
    }
}