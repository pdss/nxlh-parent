package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbOrderRefundMapper;
import com.nxlh.manager.model.dbo.TbOrderRefund;
import com.nxlh.manager.model.dto.OrderRefundDTO;
import com.nxlh.manager.service.OrderRefundService;

import java.util.List;
import tk.mybatis.mapper.util.Sqls;

@Service(interfaceClass = OrderRefundService.class)
public class OrderRefundServiceImp extends BaseDbCURDSServiceImpl<TbOrderRefundMapper, TbOrderRefund, OrderRefundDTO> implements OrderRefundService {


    @Override
    public MyResult getOrderRefundItemsStatus(String orderid) {
        var result = this.list(this.sqlBuilder().where(Sqls.custom().andEqualTo("orderid", orderid)).build());
        return MyResult.ok(result);
    }

    @Override
    public List<OrderRefundDTO> getRefundById(String id) {
        List<OrderRefundDTO> refundItemsByOId = this.baseMapper.getRefundItemsByOId(id);
        return refundItemsByOId;
    }
}
