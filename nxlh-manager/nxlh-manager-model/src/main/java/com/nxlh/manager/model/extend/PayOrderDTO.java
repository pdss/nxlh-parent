package com.nxlh.manager.model.extend;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 支付订单的DTO
 */
@Data
@ToString
public class PayOrderDTO implements Serializable {

    /**
     * 订单号
     */
    @NotBlank
    private String orderno;

    /**
     * 备注信息
     */
    private String remark;

    /**
     * 快递：0默认 1顺丰
     */
    private Integer express = 0;

    /**
     * 用户的优惠券关联id
     */
    private String couponId;


    /**
     * 使用积分抵扣现金
     */
    private Integer isscore = 0;


    /**
     * 收货地址的Id
     */
    private String receiveAddressId;


    /**
     * 租赁订单，租赁周期
     */
    private Integer term;

    /**
     * 租赁天数
     */
    private Integer rentdays;

}
