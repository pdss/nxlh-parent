package com.nxlh.manager.service;

import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.manager.mapper.TbMenuMapper;
import com.nxlh.manager.model.dbo.TbMenu;
import com.nxlh.manager.model.dto.MenuDTO;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

public interface MenuService extends BaseService<MenuDTO, TbMenuMapper, TbMenu> {

    List<MenuDTO> getALL();

    PageInfo<MenuDTO> pageMenu(PageParameter var1, Map<String, Object> parameters, String[] orderby, Integer isAsc);

    PageInfo<MenuDTO> pageMenu(PageParameter page, Example example);

    //根据id删除菜单并删除管理表
    boolean removeMenuById(String id);


    /**
     * 根据子集id查找父集
     *
     * @param ids
     * @return
     */
    List<MenuDTO> queryParentBySon(List<String> ids);

}
