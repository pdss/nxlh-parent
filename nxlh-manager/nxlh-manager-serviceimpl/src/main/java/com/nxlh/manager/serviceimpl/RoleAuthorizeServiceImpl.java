package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbRoleAuthorizeMapper;
import com.nxlh.manager.model.dbo.TbRoleAuthorize;
import com.nxlh.manager.model.dto.RoleAuthorizeDTO;
import com.nxlh.manager.service.RoleAuthorizeService;

import java.util.List;

@Service(interfaceClass = RoleAuthorizeService.class)
public class RoleAuthorizeServiceImpl extends BaseDbCURDSServiceImpl<TbRoleAuthorizeMapper, TbRoleAuthorize, RoleAuthorizeDTO> implements RoleAuthorizeService {


    @Override
    public boolean insertRoleAuthorizes(String roleid, List<String> authroizeIds) {
        if (authroizeIds != null && authroizeIds.size() > 0) {
            boolean result = this.transactionUtils.transact((a) -> {
                this.baseMapper.insertRoleAuthorizes(roleid, authroizeIds);
            });
            return result;
        }
        return false;
    }
}
