package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbRoleMenuMapper;
import com.nxlh.manager.model.dbo.TbRoleMenu;
import com.nxlh.manager.model.dto.RoleMenuDTO;

import java.util.List;

public interface RoleMenuService extends BaseService<RoleMenuDTO, TbRoleMenuMapper, TbRoleMenu> {

    //根据角色id删除
    boolean deleteByRoleId(String roleId);

    //根据菜单id删除
    boolean deleteByMenuId(String MenuId);

    //批量插入
    boolean insertList(String roleId, List<String> menuIdList);
}
