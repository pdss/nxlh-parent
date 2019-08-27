package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.manager.mapper.TbGameCategoryMapper;
import com.nxlh.manager.model.dbo.TbGameCategory;
import com.nxlh.manager.model.dto.GameCategoryDTO;
import com.nxlh.manager.model.dto.ShopDTO;
import com.nxlh.manager.service.GameCategoryService;

import java.util.List;

@Service(interfaceClass = GameCategoryService.class)
public class GameCategoryServiceImpl extends BaseDbCURDSServiceImpl<TbGameCategoryMapper, TbGameCategory, GameCategoryDTO> implements GameCategoryService {

    @Override
    public Pagination<ShopDTO> queryShopsByCategoryName(PageParameter page, String keyword) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        Page list = (Page) this.baseMapper.queryShopsByCategoryName(keyword);
        var pagination = new Pagination<ShopDTO>(page.getPageIndex(),page.getPageSize(), (int) list.getTotal());
        pagination.setList(list.getResult());
        return pagination;
    }
}
