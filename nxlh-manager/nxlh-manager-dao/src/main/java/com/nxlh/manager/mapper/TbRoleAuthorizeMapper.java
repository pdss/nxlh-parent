package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbRoleAuthorize;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbRoleAuthorizeMapper extends Mapper<TbRoleAuthorize> {

    int insertRoleAuthorizes(String roleid, @Param("list") List<String> list);
}