package com.nxlh.manager.service;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbModulesProductsMapper;
import com.nxlh.manager.model.dbo.TbModulesProducts;
import com.nxlh.manager.model.dto.ModulesProductsDTO;
import com.nxlh.manager.model.dto.RecycleProductDTO;
import com.nxlh.manager.model.dto.ShopDTO;

import java.math.BigDecimal;

public interface ModulesProductsService extends BaseService<ModulesProductsDTO, TbModulesProductsMapper, TbModulesProducts> {

    Pagination<ModulesProductsDTO> page(PageParameter page, String moduleId);

    /**
     * 商品更新，栏目中的商品也更新
     *
     * @param entity
     * @return
     */
    boolean updateShopPrice(ShopDTO entity);


    /**
     * 判断是否重复添加
     *
     * @param entity
     * @return
     */
    boolean checkProduct(ModulesProductsDTO entity);


    boolean delProduct(String id);

}
