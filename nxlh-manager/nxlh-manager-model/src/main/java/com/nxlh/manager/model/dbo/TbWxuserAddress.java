package com.nxlh.manager.model.dbo;

import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_wxuser_address")
public class TbWxuserAddress extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 关联微信用户id
     */
    private String wxuserid;

    /**
     * 收货联系人电话
     */
    private String phone;

    /**
     * 收货人姓名
     */
    private String username;

    /**
     * 省份
     */
    private String province;

    /**
     * 城市
     */
    private String city;

    /**
     * 区县
     */
    private String area;

    /**
     * 详细地址
     */
    private String address;

    /**
     * 电话区号
     */
    private String phoneprefix;

    /**
     * 是否默认地址
     */
    private Integer isdefault;

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
     * 获取关联微信用户id
     *
     * @return wxuserid - 关联微信用户id
     */
    public String getWxuserid() {
        return wxuserid;
    }

    /**
     * 设置关联微信用户id
     *
     * @param wxuserid 关联微信用户id
     */
    public void setWxuserid(String wxuserid) {
        this.wxuserid = wxuserid == null ? null : wxuserid.trim();
    }

    /**
     * 获取收货联系人电话
     *
     * @return phone - 收货联系人电话
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置收货联系人电话
     *
     * @param phone 收货联系人电话
     */
    public void setPhone(String phone) {
        this.phone = phone == null ? null : phone.trim();
    }

    /**
     * 获取收货人姓名
     *
     * @return username - 收货人姓名
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置收货人姓名
     *
     * @param username 收货人姓名
     */
    public void setUsername(String username) {
        this.username = username == null ? null : username.trim();
    }

    /**
     * 获取省份
     *
     * @return province - 省份
     */
    public String getProvince() {
        return province;
    }

    /**
     * 设置省份
     *
     * @param province 省份
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * 获取城市
     *
     * @return city - 城市
     */
    public String getCity() {
        return city;
    }

    /**
     * 设置城市
     *
     * @param city 城市
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * 获取区县
     *
     * @return area - 区县
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置区县
     *
     * @param area 区县
     */
    public void setArea(String area) {
        this.area = area == null ? null : area.trim();
    }

    /**
     * 获取详细地址
     *
     * @return address - 详细地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置详细地址
     *
     * @param address 详细地址
     */
    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
    }

    /**
     * 获取电话区号
     *
     * @return phoneprefix - 电话区号
     */
    public String getPhoneprefix() {
        return phoneprefix;
    }

    /**
     * 设置电话区号
     *
     * @param phoneprefix 电话区号
     */
    public void setPhoneprefix(String phoneprefix) {
        this.phoneprefix = phoneprefix == null ? null : phoneprefix.trim();
    }

    public Integer getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(Integer isdefault) {
        this.isdefault = isdefault;
    }
}