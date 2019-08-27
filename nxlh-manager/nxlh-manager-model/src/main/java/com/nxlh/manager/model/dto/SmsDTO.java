package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbSms;
import com.nxlh.manager.model.dbo.TbTag;
import lombok.Data;
import lombok.ToString;

import java.util.Map;

@Data
@ToString
public class SmsDTO extends TbSms implements BaseDTO {

    private String signName;

    private String tempCode;

    private Map<String,String> tempParams;
}
