package com.nxlh.manager.model.vo.wxuserRecycles;


import com.nxlh.manager.model.vo.base.BaseQueryVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class WxuserRecyclesVO extends BaseQueryVO {
    //搜索条件
    private String filter;
}
