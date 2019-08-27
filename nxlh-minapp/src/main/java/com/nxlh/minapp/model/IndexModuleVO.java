package com.nxlh.minapp.model;

import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.model.vo.base.BaseVO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 小程序首页-栏目模型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class IndexModuleVO extends BaseVO {

    private String title;
    private List<ShopDTO> products;
}
