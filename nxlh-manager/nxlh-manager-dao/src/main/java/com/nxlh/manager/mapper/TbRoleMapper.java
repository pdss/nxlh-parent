package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbMenu;
import com.nxlh.manager.model.dbo.TbRole;
import com.nxlh.manager.model.dto.MenuDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbRoleMapper extends Mapper<TbRole> {

    //根据id查询角色
    TbRole queryByid(String id);


    List<MenuDTO> getMenuByRole(String id);
}