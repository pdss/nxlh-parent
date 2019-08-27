package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbCoupons;
import com.nxlh.manager.model.dto.CouponsDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbCouponsMapper extends Mapper<TbCoupons> {

    //获取全部
    List<CouponsDTO> queryAll();

}