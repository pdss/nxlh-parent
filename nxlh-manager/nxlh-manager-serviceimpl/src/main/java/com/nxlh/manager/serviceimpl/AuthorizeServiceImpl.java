package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbAuthorizeMapper;
import com.nxlh.manager.model.dbo.TbAuthorize;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.service.AuthorizeService;

import java.util.List;

@Service(interfaceClass = AuthorizeService.class)
public class AuthorizeServiceImpl extends BaseDbCURDSServiceImpl<TbAuthorizeMapper, TbAuthorize, AuthorizeDTO> implements AuthorizeService {

    @Override
    public List<AuthorizeDTO> getAllAuthorizeByRole(String id) {
        return this.baseMapper.getAuthorizeByRole(id);
    }
}
