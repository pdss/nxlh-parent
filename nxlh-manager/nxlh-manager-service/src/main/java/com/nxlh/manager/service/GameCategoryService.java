package com.nxlh.manager.service;

import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbGameCategoryMapper;
import com.nxlh.manager.model.dbo.TbGameCategory;
import com.nxlh.manager.model.dto.GameCategoryDTO;
import com.nxlh.manager.model.dto.ShopDTO;

import java.util.List;

public interface GameCategoryService extends BaseService<GameCategoryDTO, TbGameCategoryMapper, TbGameCategory> {

    /**
     * 查询分类名下的商品
     * @param keyword
     * @return
     */
    Pagination<ShopDTO> queryShopsByCategoryName(PageParameter page, String keyword);
}
