package com.nxlh.minapp.model;

import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class SearchVO extends BaseVO {
    private String searchkey;
}
