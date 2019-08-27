package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_sms")
public class TbSms extends BaseDBO {
    @Id
    private String id;

    private Date addtime;

    private Integer isdelete;

    /**
     * 电话
     */
    private String phone;

    /**
     * 短信码
     */
    private String message;

    /**
     * 类型 1短信码 2消息通知,如:发货 
     */
    private Integer type;

    /**
     * 微信用户
     */
    private String wxuserid;

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
     * 获取电话
     *
     * @return phone - 电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置电话
     *
     * @param phone 电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取短信码
     *
     * @return message - 短信码
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置短信码
     *
     * @param message 短信码
     */
    public void setMessage(String message) {
        this.message = message == null ? null : message.trim();
    }

    /**
     * 获取类型 1短信码 2消息通知,如:发货 
     *
     * @return type - 类型 1短信码 2消息通知,如:发货 
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置类型 1短信码 2消息通知,如:发货 
     *
     * @param type 类型 1短信码 2消息通知,如:发货 
     */
    public void setType(Integer type) {
        this.type = type;
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
}