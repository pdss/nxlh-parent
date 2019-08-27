package com.nxlh.manager.model.dbo;

import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Table(name = "tb_lottery")
public class TbLottery extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 标题
     */
    private String title;

    /**
     * 二级标题
     */
    private String subtitle;

    /**
     * 是否开启 0没有 1开启
     */
    private Integer status;

    /**
     * 参与人数 0不限制
     */
    private Integer userlimit;

    /**
     * banner图
     */
    private String image;

    /**
     * 限制的会员类型
     */
    private Integer usertype;

    /**
     * 是否显示
     */
    private Integer isshow;


    /**
     * 抽奖方式
     */
    private Integer type;


    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getUsertype() {
        return usertype;
    }

    public void setUsertype(Integer usertype) {
        this.usertype = usertype;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getIsshow() {
        return isshow;
    }

    public void setIsshow(Integer isshow) {
        this.isshow = isshow;
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
     * 获取标题
     *
     * @return title - 标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    /**
     * 获取二级标题
     *
     * @return subtitle - 二级标题
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * 设置二级标题
     *
     * @param subtitle 二级标题
     */
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle == null ? null : subtitle.trim();
    }

    /**
     * 获取是否开启 0没有 1开启
     *
     * @return status - 是否开启 0没有 1开启
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置是否开启 0没有 1开启
     *
     * @param status 是否开启 0没有 1开启
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取参与人数 0不限制
     *
     * @return userlimit - 参与人数 0不限制
     */
    public Integer getUserlimit() {
        return userlimit;
    }

    /**
     * 设置参与人数 0不限制
     *
     * @param userlimit 参与人数 0不限制
     */
    public void setUserlimit(Integer userlimit) {
        this.userlimit = userlimit;
    }
}