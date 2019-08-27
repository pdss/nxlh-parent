package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbOrderRefundMapper;
import com.nxlh.manager.model.dbo.TbOrderRefund;
import com.nxlh.manager.model.dto.OrderRefundDTO;

import java.util.List;

public interface OrderRefundService extends BaseService<OrderRefundDTO, TbOrderRefundMapper, TbOrderRefund> {


    /**
     * 获取指定商品的退款项的状态
     * @param orderid
     * @return
     */
    MyResult getOrderRefundItemsStatus(String orderid);

    /**
     * 根据订单id获取退款列表
     *
     * @param id
     * @return
     */
    List<OrderRefundDTO> getRefundById(String id);
}
