package com.nxlh.manager.model.vo.order;

import com.nxlh.manager.model.vo.base.BaseQueryVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.math.BigDecimal;


/**
 * 退款审核请求
 */
@Data
@ToString
public class OrderRefundVO extends BaseQueryVO {

    /**
     * 退款记录id
     */
    @NotBlank
    private String refundId;

    /**
     * 退款金额
     */
    private BigDecimal refundprice;


    /**
     * 退货数量
     */
    private Integer refundcount;


    /**
     * 拒绝退款时的原因
     */
    private String reason;

}
