package com.nxlh.minapp.model;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WxLoginVO extends BaseVO {
    private String code;
}
