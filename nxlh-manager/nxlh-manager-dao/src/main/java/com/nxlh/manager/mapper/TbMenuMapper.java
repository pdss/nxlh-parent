package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbMenu;
import com.nxlh.manager.model.dto.MenuDTO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TbMenuMapper extends Mapper<TbMenu> {

    /**
     * 添加了父级名称的queryall
     *
     * @return
     */
    List<TbMenu> queryAll();

    /**
     * 根据roleid 查询 菜单数据 并关联子集
     *
     * @param id
     * @return
     */
    List<MenuDTO> getMenuByRole(String id);

    /**
     * 根据子集查找父集
     * @param list
     * @return
     */
    List<MenuDTO> queryParentBySon(List<String> list);
}