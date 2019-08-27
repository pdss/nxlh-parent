package com.nxlh.minapp.model.order;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class OrderItemVO extends BaseVO {

    private String productId;
    private int count;
}
