package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbShoppingcar;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbShoppingcarMapper extends Mapper<TbShoppingcar> {

    List<TbShoppingcar> getCarsByUserId(String userId);
}