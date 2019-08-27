package com.nxlh.minapp.model.order;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import java.util.List;

/**
 * 预生成订单请求
 */
@Data
@ToString
public class PreviewOrderVO extends BaseVO {

    /**
     * 下单商品
     */
    private List<OrderItemVO> products;


}
