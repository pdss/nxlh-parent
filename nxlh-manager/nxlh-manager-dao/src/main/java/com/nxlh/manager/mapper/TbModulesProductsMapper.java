package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbModulesProducts;
import com.nxlh.manager.model.dto.ModulesProductsDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbModulesProductsMapper extends Mapper<TbModulesProducts> {

    List<ModulesProductsDTO> queryPage(@Param("moduleid") String moduleid);
}