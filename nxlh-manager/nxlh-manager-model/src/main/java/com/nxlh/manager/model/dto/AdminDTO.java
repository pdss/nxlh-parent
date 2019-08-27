package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbAdmin;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class AdminDTO extends TbAdmin {

    private String authorization;


}
