package com.nxlh.manager.model.vo.order;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * 订单发货请求
 */
@Data
@ToString
public class OrderTransitVO extends BaseVO {

    /**
     * 订单的商品关联id
     */
    @NotBlank
    private String orderitemid;

    /**
     * 快递:ExpressEnums
     */
    private int express;

    /**
     * 运单号
     */
    @NotBlank
    private String tracknumber;

}
