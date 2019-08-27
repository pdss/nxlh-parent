package com.nxlh.manager.model.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nxlh.manager.model.dbo.TbShop;
import com.nxlh.manager.model.dbo.TbWxuser;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class WxUserDTO extends TbWxuser {

    /**
     * 给指定用户发放优惠券
     */
    String couponsId;

    /**
     * 优惠券数量
     */
    Integer couponCount;

    /**
     * 登录用户的sessionkey
     */
    @JSONField(serialize = false)
    private String sessionKey;


    /**
     * 用户目前正在租赁的商品id
     * 如果没有租赁商品，则为空
     */
    private String rentProductId;

}
