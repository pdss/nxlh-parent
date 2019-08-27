package com.nxlh.manager.mapper;


import com.nxlh.manager.model.dbo.TbIntervalSendcoupon;
import com.nxlh.manager.model.dto.IntervalSendcouponDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbIntervalSendcouponMapper extends Mapper<TbIntervalSendcoupon> {

    List<IntervalSendcouponDTO> selectBypage();

}