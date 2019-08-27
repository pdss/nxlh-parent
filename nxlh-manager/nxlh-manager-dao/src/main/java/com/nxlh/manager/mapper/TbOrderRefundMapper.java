package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbOrderRefund;
import com.nxlh.manager.model.dto.OrderRefundDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbOrderRefundMapper extends Mapper<TbOrderRefund> {

    List<OrderRefundDTO> getRefundItemsByOId(String id);
}