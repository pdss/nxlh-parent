package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbRole;

import java.util.List;

public class RoleDTO extends TbRole implements BaseDTO {

    List<String> menuIds;
}
