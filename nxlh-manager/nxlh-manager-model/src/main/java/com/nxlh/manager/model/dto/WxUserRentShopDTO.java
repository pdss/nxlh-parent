package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbTag;
import com.nxlh.manager.model.dbo.TbWxuserRentshop;
import lombok.Data;
import lombok.ToString;


@Data
@ToString
public class WxUserRentShopDTO extends TbWxuserRentshop implements BaseDTO {

    private OrderDTO order;

    private OrderItemDTO orderItemDTO;
}
