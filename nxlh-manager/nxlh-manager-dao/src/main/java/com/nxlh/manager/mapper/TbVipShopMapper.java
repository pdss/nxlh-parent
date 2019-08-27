package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbVipShop;
import com.nxlh.manager.model.dto.VipShopDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbVipShopMapper extends Mapper<TbVipShop> {

    List<VipShopDTO> queryPage(@Param("activityid") String activityid);

}