package com.nxlh.manager.model.dto;

import com.nxlh.manager.model.dbo.TbModulesProducts;
import lombok.Data;
import lombok.ToString;


@ToString
@Data
public class ModulesProductsDTO extends TbModulesProducts {

    private ShopDTO shop;

    private IndexmodulesDTO indexmodules;


}
