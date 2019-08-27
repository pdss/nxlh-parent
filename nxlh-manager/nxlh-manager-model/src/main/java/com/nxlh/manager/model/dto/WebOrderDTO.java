package com.nxlh.manager.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nxlh.manager.model.dbo.TbWebOrder;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class WebOrderDTO extends TbWebOrder implements BaseDTO {

    private WxUserDTO wxuser;
}
