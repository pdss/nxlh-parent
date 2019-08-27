package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbOrderItem;
import tk.mybatis.mapper.common.Mapper;

public interface TbOrderItemMapper extends Mapper<TbOrderItem> {

    /**
     * 获取订单项详情
     * @param id 订单项id
     * @return
     */
    TbOrderItem getOrderItemDetail(String id);
}