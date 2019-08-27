package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbVipShop;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class VipShopDTO extends TbVipShop {

    private ShopDTO shop;

    private SeckillDTO seckill;

    //库存占用数
    private int stockInUsing;
}
