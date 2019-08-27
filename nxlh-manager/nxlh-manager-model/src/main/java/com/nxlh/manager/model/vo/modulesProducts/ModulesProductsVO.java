package com.nxlh.manager.model.vo.modulesProducts;


import com.nxlh.manager.model.vo.base.BaseQueryVO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ModulesProductsVO extends BaseQueryVO {

    /**
     * 栏目id
     */
    private String filter;
}
