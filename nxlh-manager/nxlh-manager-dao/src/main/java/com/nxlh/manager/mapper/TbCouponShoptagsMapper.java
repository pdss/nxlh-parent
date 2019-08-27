package com.nxlh.manager.mapper;


import com.nxlh.manager.model.dbo.TbCouponShoptags;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbCouponShoptagsMapper extends Mapper<TbCouponShoptags> {

    boolean insetCouponShoptagsList(List<TbCouponShoptags> list);
}