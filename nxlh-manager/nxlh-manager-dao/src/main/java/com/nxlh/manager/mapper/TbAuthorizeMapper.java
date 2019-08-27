package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbAuthorize;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbAuthorizeMapper extends Mapper<TbAuthorize> {

    List<AuthorizeDTO> getAuthorizeByRole(@Param("roleid") String id);
}