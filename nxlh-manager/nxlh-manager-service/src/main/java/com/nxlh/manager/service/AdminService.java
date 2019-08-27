package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbAdminMapper;
import com.nxlh.manager.model.dbo.TbAdmin;
import com.nxlh.manager.model.dbo.TbMenu;
import com.nxlh.manager.model.dto.AdminDTO;

import java.util.List;


public interface AdminService extends BaseService<AdminDTO, TbAdminMapper, TbAdmin> {

    MyResult login(String loginname, String password);


    AdminDTO getByAdminName(String username);


    //插入或更新后台用户
    boolean addOrUpdateAdmin(AdminDTO adminDTO);

    /**
     * 根据角色id获取菜单
     *
     * @param roleid
     * @return
     */
    MyResult getMenuByRoleId(String roleid);

}
