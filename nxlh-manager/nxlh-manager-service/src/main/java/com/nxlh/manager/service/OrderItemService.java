package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbOrderItemMapper;
import com.nxlh.manager.mapper.TbShopMapper;
import com.nxlh.manager.model.dbo.TbOrderItem;
import com.nxlh.manager.model.dbo.TbShop;
import com.nxlh.manager.model.dto.OrderItemDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.extend.ScoreProductMinDTO;

import java.util.List;

public interface OrderItemService extends BaseService<OrderItemDTO, TbOrderItemMapper, TbOrderItem> {


    /**
     * 获取订单项详情
     * @param id
     * @return
     */
    MyResult getOrderItemDetail(String id);


}
