package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.model.PageParameter;
import com.nxlh.manager.mapper.TbMenuMapper;
import com.nxlh.manager.model.dbo.TbMenu;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.service.MenuService;
import com.nxlh.manager.service.RoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl extends BaseDbCURDSServiceImpl<TbMenuMapper, TbMenu, MenuDTO> implements MenuService {


    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public List<MenuDTO> getALL() {
//        Example example = Example.builder(TbMenu.class).where(Sqls.custom().andEqualTo("isdelete", 0)).build();
        List<TbMenu> tbMenus = this.baseMapper.queryAll();
//        List<TbMenu> tbMenus = menuMapper.queryAll();
        return this.beanListMap(tbMenus, this.currentDTOClass());
    }


    @Override
    public PageInfo<MenuDTO> pageMenu(PageParameter page, Map<String, Object> parameters, String[] orderby, Integer isAsc) {
        var example = this.columnsMapToSqlBuilder(parameters);
        if (orderby.length > 0) {
            if (isAsc == 1) {
                example.orderBy(orderby);
            } else {
                example.orderByDesc(orderby);
            }
        }
        return pageMenu(page, example.build());
    }

    @Override
    public PageInfo<MenuDTO> pageMenu(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        List<TbMenu> tbMenus = this.baseMapper.queryAll();
        PageInfo<TbMenu> pageInfo = new PageInfo<TbMenu>(tbMenus);
        var dtos = this.beanListMap(pageInfo.getList(), this.currentDTOClass());
        var dtoPage = this.beanMap(pageInfo, PageInfo.class);
        dtoPage.setList(dtos);
        return dtoPage;
    }

    //根据id删除菜单并删除管理表
    @Override
    public boolean removeMenuById(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            int delete = this.baseMapper.deleteByPrimaryKey(id);
            roleMenuService.deleteByMenuId(id);
            Example example = new Example(TbMenu.class);
            example.createCriteria().andEqualTo("parentid", id);
            TbMenu tbMenu = new TbMenu();
            tbMenu.setParentid("");
            this.baseMapper.updateByExampleSelective(tbMenu, example);
        });
        return result;

    }

    @Override
    public List<MenuDTO> queryParentBySon(List<String> ids) {
        List<MenuDTO> menuDTOS = this.baseMapper.queryParentBySon(ids);
        if (menuDTOS.size() > 0) return menuDTOS;
        return null;
    }


}
