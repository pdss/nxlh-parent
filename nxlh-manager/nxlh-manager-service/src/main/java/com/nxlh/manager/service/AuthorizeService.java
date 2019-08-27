package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbAuthorizeMapper;
import com.nxlh.manager.model.dbo.TbAuthorize;
import com.nxlh.manager.model.dto.AuthorizeDTO;

import java.util.List;


public interface AuthorizeService extends BaseService<AuthorizeDTO, TbAuthorizeMapper, TbAuthorize> {

    List<AuthorizeDTO> getAllAuthorizeByRole(String id);
}
