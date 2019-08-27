package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbIntervalSendcoupon;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class IntervalSendcouponDTO extends TbIntervalSendcoupon {

    CouponsDTO coupon;

}
