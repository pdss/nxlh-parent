package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbCategory;
import com.nxlh.manager.model.dbo.TbGameCategory;
import com.nxlh.manager.model.dto.ShopDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbGameCategoryMapper extends Mapper<TbGameCategory> {

//    List<TbCategory> selectByShopId(String shopid);
//
//    int insertCategoryList(List<TbGameCategory> list);

    /**
     * 查询分类名下的商品
     * @param categoryname
     * @return
     */
    List<ShopDTO> queryShopsByCategoryName(String categoryname);

}