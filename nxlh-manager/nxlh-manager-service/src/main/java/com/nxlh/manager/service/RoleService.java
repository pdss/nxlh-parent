package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbRoleMapper;
import com.nxlh.manager.model.dbo.TbRole;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.model.dto.RoleDTO;

import java.util.List;

public interface RoleService extends BaseService<RoleDTO, TbRoleMapper, TbRole> {

    RoleDTO getById(String id);

    /**
     * 更新角色菜单表
     *
     * @param role
     * @return
     */
    boolean updateRoleMenu(RoleDTO role);


    List<RoleDTO> getALL();

    /**
     * 根据角色名称查找
     *
     * @param name
     * @return
     */
    List<RoleDTO> getByName(String name);


    //添加或更新角色 并同步后台管理员的角色数据
    boolean addOrUpdateRole(RoleDTO roleDTO);


    //删除角色 并同步后台管理员的角色数据
    boolean removeRoleByid(String id);

    List<AuthorizeDTO> getAuthorizeByRoleid(String roleid);

    boolean insertRoleAuthroizr(List<AuthorizeDTO> authorizeDTOS);
}