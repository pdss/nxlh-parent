package com.nxlh.manager.model.dbo;

import com.alibaba.fastjson.annotation.JSONField;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;

import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.*;

@Table(name = "tb_wxuser")
public class TbWxuser extends BaseDBO {
//    @Id
//    private String id;
//
//    private Date addtime;
//
//    private Integer isdelete;

    /**
     * 微信昵称
     */
    private String nickname;

    /**
     * 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    private String avatarurl;



    /**
     * 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    private Integer gender;

    private String city;

    private String province;

    private String country;

    @JSONField(serialize = false)
    private String openid;

    private String unionid;


    /**
     * 会员类型,1金牌，2银牌，3铜牌  0普通
     */
    private Integer vipid;

    /**
     * 积分
     */
    private BigDecimal vscore;

    /**
     * 累计消费金额
     */
    private BigDecimal sumpay;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 会员等级经验
     */
    private BigDecimal exp;

    /**
     * 登录会话token
     */
    private String sessiontoken;





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
     * 获取微信昵称
     *
     * @return nickname - 微信昵称
     */
    public String getNickname() {
        return nickname;
    }

    /**
     * 设置微信昵称
     *
     * @param nickname 微信昵称
     */
    public void setNickname(String nickname) {
        this.nickname = nickname == null ? null : nickname.trim();
    }

    /**
     * 获取用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     *
     * @return avatarurl - 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    public String getAvatarurl() {
        return avatarurl;
    }

    /**
     * 设置用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     *
     * @param avatarurl 用户头像，最后一个数值代表正方形头像大小（有0、46、64、96、132数值可选，0代表132*132正方形头像），用户没有头像时该项为空。若用户更换头像，原有头像URL将失效。
     */
    public void setAvatarurl(String avatarurl) {
        this.avatarurl = avatarurl == null ? null : avatarurl.trim();
    }

    /**
     * 获取用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     *
     * @return gender - 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    public Integer getGender() {
        return gender;
    }

    /**
     * 设置用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     *
     * @param gender 用户的性别，值为1时是男性，值为2时是女性，值为0时是未知
     */
    public void setGender(Integer gender) {
        this.gender = gender;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    /**
     * @return province
     */
    public String getProvince() {
        return province;
    }

    /**
     * @param province
     */
    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    /**
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * @param country
     */
    public void setCountry(String country) {
        this.country = country == null ? null : country.trim();
    }

    /**
     * @return openid
     */
    public String getOpenid() {
        return openid;
    }

    /**
     * @param openid
     */
    public void setOpenid(String openid) {
        this.openid = openid == null ? null : openid.trim();
    }

    /**
     * 获取会员类型,1金牌，2银牌，3铜牌  0普通
     *
     * @return vipid - 会员类型,1金牌，2银牌，3铜牌  0普通
     */
    public Integer getVipid() {
        return vipid;
    }

    /**
     * 设置会员类型,1金牌，2银牌，3铜牌  0普通
     *
     * @param vipid 会员类型,1金牌，2银牌，3铜牌  0普通
     */
    public void setVipid(Integer vipid) {
        this.vipid = vipid;
    }

    public BigDecimal getVscore() {
        return vscore;
    }

    public void setVscore(BigDecimal vscore) {
        this.vscore = vscore;
    }

    public BigDecimal getSumpay() {
        return sumpay;
    }

    public void setSumpay(BigDecimal sumpay) {
        this.sumpay = sumpay;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public BigDecimal getExp() {
        return exp;
    }

    public void setExp(BigDecimal exp) {
        this.exp = exp;
    }

    public String getSessiontoken() {
        return sessiontoken;
    }

    public void setSessiontoken(String sessiontoken) {
        this.sessiontoken = sessiontoken;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }
}