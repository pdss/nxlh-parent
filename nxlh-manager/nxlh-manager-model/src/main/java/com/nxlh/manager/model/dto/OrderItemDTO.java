package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbOrderItem;
import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

@Data
@ToString
public class OrderItemDTO extends TbOrderItem {

    /**
     * 该订单项最多可退款金额
     */
    private BigDecimal maxrefundprice;




}
