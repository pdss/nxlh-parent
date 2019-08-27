package com.nxlh.manager.model;

import com.nxlh.manager.model.vo.base.BaseQueryVO;
import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.Data;
import lombok.ToString;

import java.util.List;


@Data
@ToString
public class WxUserQueryVO extends BaseQueryVO {

    private String searchFilter;

    private List<Integer> vipType;


}
