package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.mapper.TbRoleMenuMapper;
import com.nxlh.manager.model.dbo.TbRoleMenu;
import com.nxlh.manager.model.dto.RoleMenuDTO;
import com.nxlh.manager.service.RoleMenuService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = RoleMenuService.class)
public class RoleMenuServiceImpl extends BaseDbCURDSServiceImpl<TbRoleMenuMapper, TbRoleMenu, RoleMenuDTO> implements RoleMenuService {


    //根据角色id删除
    @Override
    public boolean deleteByRoleId(String roleId) {
        boolean result = this.transactionUtils.transact((a) -> {
            Example exampleGameTag = Example.builder(TbRoleMenu.class).where(Sqls.custom().andEqualTo("roleid", roleId)).build();
            this.baseMapper.deleteByExample(exampleGameTag);
        });
        return result;
    }

    //根据菜单id删除
    @Override
    public boolean deleteByMenuId(String menuId) {
        boolean result = this.transactionUtils.transact((a) -> {
            Example exampleGameTag = Example.builder(TbRoleMenu.class).where(Sqls.custom().andEqualTo("menuid", menuId)).build();
            this.baseMapper.deleteByExample(exampleGameTag);
        });
        return result;
    }

    //批量插入
    @Override
    public boolean insertList(String roleId, List<String> menuIdList) {
        boolean result = this.transactionUtils.transact((a) -> {
            TbRoleMenu tbRoleMenu = new TbRoleMenu();
            tbRoleMenu.setRoleid(roleId);
            for (String menuId : menuIdList) {
                tbRoleMenu.setId(IDUtils.genUUID());
                tbRoleMenu.setIsdelete(0);
                tbRoleMenu.setAddtime(new Date());
                tbRoleMenu.setMenuid(menuId);

                this.baseMapper.insert(tbRoleMenu);
            }
        });
        return result;
    }
}
