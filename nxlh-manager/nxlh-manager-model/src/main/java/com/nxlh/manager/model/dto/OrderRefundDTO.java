package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbOrderRefund;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class OrderRefundDTO extends TbOrderRefund {

    OrderItemDTO orderItem;

}
