package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbAuthorize;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class AuthorizeDTO extends TbAuthorize implements BaseDTO {

    /**
     * 角色是否拥有
     */
    private Integer ischeck;

    private String roleid;
}
