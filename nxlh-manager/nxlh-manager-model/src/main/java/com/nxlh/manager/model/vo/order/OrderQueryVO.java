package com.nxlh.manager.model.vo.order;

import com.nxlh.manager.model.vo.base.BaseQueryVO;
import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;

@Data
public class OrderQueryVO extends BaseQueryVO {

    /**
     * 订单分类
     * wait : 待发货
     * refund: 待退款
     * receive:待收货
     * seckill:秒杀订单
     * all: 所有订单
     */
    private String filter;

    private String searchFilter;
}
