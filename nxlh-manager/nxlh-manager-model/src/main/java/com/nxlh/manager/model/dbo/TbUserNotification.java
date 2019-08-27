package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_user_notification")
public class TbUserNotification extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 用户id
     */
    private String userid;

    /**
     * 状态:0未读 1已读
     */
    private Integer status;

    /**
     * 消息id,如果是用户消息则可能为空
     */
    private String nid;

    /**
     * 消息类型 0系统消息 1用户消息
     */
    private Integer ntype;

    /**
     * 标题
     */
    private String title;

    /**
     * 二级标题
     */
    private String subtitle;

    /**
     * 额外参数
     */
    private String extra;

    /**
     * 用户消息类型，1发货通知
     */
    private String ntype2;


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
     * 获取状态:0未读 1已读
     *
     * @return status - 状态:0未读 1已读
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态:0未读 1已读
     *
     * @param status 状态:0未读 1已读
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取消息id,如果是用户消息则可能为空
     *
     * @return nid - 消息id,如果是用户消息则可能为空
     */
    public String getNid() {
        return nid;
    }

    /**
     * 设置消息id,如果是用户消息则可能为空
     *
     * @param nid 消息id,如果是用户消息则可能为空
     */
    public void setNid(String nid) {
        this.nid = nid == null ? null : nid.trim();
    }

    /**
     * 获取消息类型 0系统消息 1用户消息
     *
     * @return ntype - 消息类型 0系统消息 1用户消息
     */
    public Integer getNtype() {
        return ntype;
    }

    /**
     * 设置消息类型 0系统消息 1用户消息
     *
     * @param ntype 消息类型 0系统消息 1用户消息
     */
    public void setNtype(Integer ntype) {
        this.ntype = ntype;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getNtype2() {
        return ntype2;
    }

    public void setNtype2(String ntype2) {
        this.ntype2 = ntype2;
    }
}