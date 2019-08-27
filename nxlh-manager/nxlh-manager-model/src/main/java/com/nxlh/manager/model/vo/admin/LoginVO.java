package com.nxlh.manager.model.vo.admin;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginVO extends BaseVO {

    @NotBlank
    private String loginname;

    @NotBlank
    private String loginpassword;
}
