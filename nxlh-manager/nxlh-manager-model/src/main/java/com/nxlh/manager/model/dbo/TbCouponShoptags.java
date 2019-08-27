package com.nxlh.manager.model.dbo;

import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name = "tb_coupon_shoptags")
public class TbCouponShoptags extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 券id
     */
    private String couponid;

    /**
     * 商品标签id
     */
    private String tagid;

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
     * 获取商品标签id
     *
     * @return tagid - 商品标签id
     */
    public String getTagid() {
        return tagid;
    }

    /**
     * 设置商品标签id
     *
     * @param tagid 商品标签id
     */
    public void setTagid(String tagid) {
        this.tagid = tagid == null ? null : tagid.trim();
    }
}