package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.ObjectUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbAdminMapper;
import com.nxlh.manager.mapper.TbRoleMapper;
import com.nxlh.manager.model.dbo.*;
import com.nxlh.manager.model.dto.AuthorizeDTO;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.model.dto.RoleDTO;
import com.nxlh.manager.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.util.Sqls;

import java.util.*;
import java.util.stream.Collectors;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl extends BaseDbCURDSServiceImpl<TbRoleMapper, TbRole, RoleDTO> implements RoleService {


    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private TbAdminMapper adminMapper;

    @Autowired
    private MenuService menuService;

    @Autowired
    private AuthorizeService authorizeService;

    @Autowired
    private RoleAuthorizeService roleAuthorizeService;


    @Override
    public List<RoleDTO> getALL() {
        Example example = Example.builder(TbRole.class).where(Sqls.custom().andEqualTo("isdelete", 0)).build();
        List<TbRole> tbRoles = this.baseMapper.selectByExample(example);
        return this.beanListMap(tbRoles, this.currentDTOClass());
    }

    @Override
    public RoleDTO getById(String id) {
        var dbo = this.baseMapper.queryByid(id.toString());
        return this.beanMapper.map(dbo, this.currentDTOClass());
    }

    @Override
    public boolean updateRoleMenu(RoleDTO role) {

        List<String> menuIdList = role.getMenuList().stream().map(e -> e.getId()).collect(Collectors.toList());
        List<String> parentMenuList = new ArrayList<>();
        boolean isErgodic = true;
        while (isErgodic) {
            List<MenuDTO> menuDTOS = queryParentBySon(menuIdList);
            if (menuDTOS != null && menuDTOS.size() > 0) {
                List<String> collect = menuDTOS.stream().filter(e -> !e.getParentid().equals("0")).map(e -> e.getParentid()).collect(Collectors.toList());
                menuIdList = ObjectUtils.removeDuplicate(collect);
                parentMenuList.addAll(menuDTOS.stream().map(e -> e.getId()).collect(Collectors.toList()));
            } else {
                isErgodic = false;
            }

        }
        boolean result = this.transactionUtils.transact((a) -> {
            roleMenuService.deleteByRoleId(role.getId());
            roleMenuService.insertList(role.getId(), ObjectUtils.removeDuplicate(parentMenuList));
        });
        return result;
    }


    private List<MenuDTO> queryParentBySon(List<String> ids) {
        if (ids != null && ids.size() > 0) {
            return this.menuService.queryParentBySon(ids);
        }
        return null;
    }

    @Override
    public List<RoleDTO> getByName(String name) {
        if (!("".equals(name.trim())) && name != null) {
            Example example = new Example(TbRole.class);
            example.createCriteria().andLike("rolename", "%" + name + "%").andEqualTo("isdelete", 0);
            PageHelper.startPage(0, 5);
            List<TbRole> tbShops = this.baseMapper.selectByExample(example);
            return this.beanListMap(tbShops, this.currentDTOClass());
        }
        return null;
    }

    //添加或更新角色 并同步后台管理员的角色数据
    @Override
    public boolean addOrUpdateRole(RoleDTO roleDTO) {
        boolean result = this.transactionUtils.transact((a) -> {
            TbRole tbRole = this.beanMap(roleDTO, this.currentDBOClass());
            if (StringUtils.isEmpty(roleDTO.getId())) {
                tbRole.setId(IDUtils.genUUID());
                tbRole.setIsdelete(0);
                tbRole.setAddtime(new Date());
                this.baseMapper.insert(tbRole);
            } else {
//                return this.updateById(roleDTO) || this.add(roleDTO);
                this.baseMapper.updateByPrimaryKey(tbRole);
                TbAdmin tbAdmin = new TbAdmin();
                tbAdmin.setRoleid(tbRole.getId());
                tbAdmin.setRolename(tbRole.getRolename());
                this.adminMapper.updateAdminRolename(tbAdmin);
            }
        });
        return result;
    }

    //删除角色 并同步后台管理员的角色数据
    @Override
    public boolean removeRoleByid(String id) {
        boolean result = this.transactionUtils.transact((a) -> {
            this.baseMapper.deleteByPrimaryKey(id);
            this.roleMenuService.deleteByRoleId(id);
//            删除后台管理员角色
            Example example = new Example(TbAdmin.class);
            example.createCriteria().andEqualTo("roleid", id);
            List<TbAdmin> tbAdmins = this.adminMapper.selectByExample(example);
            tbAdmins.stream().forEach(e -> {
                e.setRoleid("");
                e.setRolename("");
                this.adminMapper.updateByPrimaryKey(e);
            });

//            Example example = new Example(TbAdmin.class);
//            example.createCriteria().andEqualTo("roleid", id);
//            TbAdmin tbAdmin = new TbAdmin();
//            tbAdmin.setRolename("/");
//            tbAdmin.setRoleid("/");
//            this.adminMapper.updateByExampleSelective(tbAdmin, example);
        });
        return result;
    }

    @Override
    public List<AuthorizeDTO> getAuthorizeByRoleid(String roleid) {
        List<AuthorizeDTO> allAuthorizeByRole = authorizeService.getAllAuthorizeByRole(roleid);
        allAuthorizeByRole.stream().forEach(e -> e.setIscheck(e.getIscheck() > 0 ? 1 : 0));
        return allAuthorizeByRole;
    }

    @Override
    public boolean insertRoleAuthroizr(List<AuthorizeDTO> authorizeDTOS) {
        List<String> collect = authorizeDTOS.stream().map(e -> e.getId()).collect(Collectors.toList());
        String roleid = authorizeDTOS.get(0).getRoleid();
        Example example = new Example(TbRoleAuthorize.class);
        example.createCriteria().andEqualTo("roleid", roleid);
        this.roleAuthorizeService.delete(example);
        return roleAuthorizeService.insertRoleAuthorizes(roleid, collect);
    }

}
