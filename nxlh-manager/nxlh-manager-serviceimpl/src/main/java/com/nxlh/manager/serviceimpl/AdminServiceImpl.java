package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nxlh.common.enums.HttpResponseEnums;
import com.nxlh.common.model.MyResult;
import com.nxlh.common.model.PageParameter;
import com.nxlh.common.model.Pagination;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.StringUtils;
import com.nxlh.manager.mapper.TbAdminMapper;
import com.nxlh.manager.mapper.TbMenuMapper;
import com.nxlh.manager.model.dbo.TbAdmin;
import com.nxlh.manager.model.dto.AdminDTO;
import com.nxlh.manager.model.dto.MenuDTO;
import com.nxlh.manager.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.util.DigestUtils;
import com.nxlh.manager.service.AdminService;
import tk.mybatis.mapper.entity.Example;
import tk.mybatis.mapper.weekend.WeekendSqls;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service(interfaceClass = AdminService.class)
public class AdminServiceImpl extends BaseDbCURDSServiceImpl<TbAdminMapper, TbAdmin, AdminDTO> implements AdminService {


    @Autowired
    private TbMenuMapper menuMapper;

    @Autowired
    @Lazy
    private RoleService roleService;

    private final long ADMIN_LOGIN_EXPIRE = 30L;


    @Override
    public MyResult login(String loginname, String password) {
        //加密规则：password = md5(md5(password+loginname))
        password = DigestUtils.md5DigestAsHex((DigestUtils.md5DigestAsHex(password.getBytes()) + loginname).getBytes());
        var filter = this.sqlBuilder().where(WeekendSqls.<TbAdmin>custom().andEqualTo(TbAdmin::getLoginname, loginname).andEqualTo(TbAdmin::getLoginpassword, password)).build();
        var dbo = this.getOne(filter);
        if (dbo == null) {
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "用户名或密码错误");
        }
        var dto = this.beanMap(dbo, this.currentDTOClass());
//        var token = JWTUtils.sign(dto.getLoginname(), dto.getLoginpassword());
        var token = String.format("ADMIN_%s",IDUtils.genUUID());
        dto.setAuthorization(token);
        //密码一定要为Null
        dto.setLoginpassword(null);
        this.redisService.set(token, dto, ADMIN_LOGIN_EXPIRE, TimeUnit.DAYS);
        return MyResult.build(HttpResponseEnums.Ok, dto);
    }

    @Override
    public AdminDTO getByAdminName(String username) {
        var filter = new HashMap<String, Object>();
        filter.put("username", username);
        var dbo = this.getOne(filter);
        return this.beanMap(dbo, this.currentDTOClass());
    }

    //插入或更新后台用户
    @Override
    public boolean addOrUpdateAdmin(AdminDTO adminDTO) {

        var result = false;
        TbAdmin tbAdmin = this.beanMap(adminDTO, this.currentDBOClass());
        var role = this.roleService.getById(tbAdmin.getRoleid());
        tbAdmin.setRolename(role.getRolename());

        if (StringUtils.isEmpty(tbAdmin.getId())) {
            tbAdmin.setId(IDUtils.genUUID());
            tbAdmin.setIsdelete(0);
            tbAdmin.setAddtime(new Date());
            String pwd = DigestUtils.md5DigestAsHex(
                    (DigestUtils.md5DigestAsHex(adminDTO.getLoginpassword().getBytes()) + adminDTO.getLoginname()).getBytes());
            tbAdmin.setLoginpassword(pwd);
            result = this.baseMapper.insert(tbAdmin) > 0;

        } else {
            TbAdmin tbAdminEdit = this.baseMapper.selectByPrimaryKey(tbAdmin.getId());
            tbAdminEdit.setRoleid(tbAdmin.getRoleid());
            tbAdminEdit.setRolename(tbAdmin.getRolename());
            tbAdminEdit.setAdminname(tbAdmin.getAdminname());
            if (StringUtils.isNotEmpty(adminDTO.getLoginpassword())) {
                String pwd = DigestUtils.md5DigestAsHex(
                        (DigestUtils.md5DigestAsHex(adminDTO.getLoginpassword().getBytes()) + adminDTO.getLoginname()).getBytes());
                tbAdminEdit.setLoginpassword(pwd);
            }
            result = this.baseMapper.updateByPrimaryKey(tbAdminEdit) > 0;

        }


        return result;
    }

    /**
     * 根据用户角色获取菜单
     *
     * @param roleid
     * @return
     */
    @Override
    public MyResult getMenuByRoleId(String roleid) {
        if (StringUtils.isNotEmpty(roleid)) {
            List<MenuDTO> menuByRole = menuMapper.getMenuByRole(roleid);
            if (menuByRole != null && menuByRole.size() > 0) {
                List<MenuDTO> parent = menuByRole.stream().filter(e -> e.getParentid().trim().equals("0")).collect(Collectors.toList());

                parent.forEach(e -> {
                    List<MenuDTO> sub = menuByRole.stream().filter(ele -> ele.getParentid().trim().equals(e.getId())).collect(Collectors.toList());
                    e.setChildren(sub);
                    e.setMenuurl("");
                    e.setType("collapsable");
                    sub.forEach(element -> {
                        List<MenuDTO> children = menuByRole.stream().filter(ele -> ele.getParentid().trim().equals(element.getId())).collect(Collectors.toList());
                        if (children.size() > 0) {
                            element.setType("collapsable");
                            element.setMenuurl("");
                            children.stream().forEach(ele -> ele.setType("item"));
                            element.setChildren(children);
                        } else {
                            element.setType("item");
                        }
                    });
                });
                return MyResult.ok(parent);
            }
            return MyResult.build(HttpResponseEnums.BadRequest.getValue(), "参数无效");
        }
        return MyResult.error("缺少登录信息");
    }

    @Override
    public Pagination<AdminDTO> page(PageParameter page, Example example) {
        PageHelper.startPage(page.getPageIndex(), page.getPageSize());
        var list = this.baseMapper.selectByExample(example);
        list.stream().forEach(e -> e.setLoginpassword(null));
        PageInfo<TbAdmin> pageInfo = new PageInfo<TbAdmin>(list);

        return mapPageInfo(pageInfo, this.currentDTOClass());
    }

}
