package com.nxlh.minapp.model;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class CouponStatusVO extends BaseVO {

    /**
     * 状态，详见:CouponStatusEnum
     */
    private Integer status;

}
