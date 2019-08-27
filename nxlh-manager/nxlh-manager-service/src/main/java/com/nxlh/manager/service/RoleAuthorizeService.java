package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbRoleAuthorizeMapper;
import com.nxlh.manager.model.dbo.TbRoleAuthorize;
import com.nxlh.manager.model.dto.RoleAuthorizeDTO;

import java.util.List;

public interface RoleAuthorizeService extends BaseService<RoleAuthorizeDTO, TbRoleAuthorizeMapper, TbRoleAuthorize> {


    /**
     * 根据角色id批量插入用户数据
     *
     * @param roleid
     * @param authroizeIds
     * @return
     */
    boolean insertRoleAuthorizes(String roleid, List<String> authroizeIds);

}
