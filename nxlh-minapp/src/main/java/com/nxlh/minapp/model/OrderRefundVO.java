package com.nxlh.minapp.model;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;

/**
 * 退款申请
 */
@Data
@ToString
public class OrderRefundVO extends BaseVO {
    /**
     * 退款原因
     */
    @Min(0)
    private int refundreason;

    /**
     * 退款说明
     */
    private String remark;

    /**
     * 订单id
     */
    @NotBlank
    private String orderid;

    /**
     * 退款金额
     */
    @DecimalMin("0.01")
    private BigDecimal refundprice;

}
