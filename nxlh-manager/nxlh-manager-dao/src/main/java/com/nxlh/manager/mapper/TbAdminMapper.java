package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbAdmin;
import tk.mybatis.mapper.common.Mapper;

public interface TbAdminMapper extends Mapper<TbAdmin> {

    /**
     * 更新后台用户角色名称
     * @param tbAdmin
     * @return
     */
    boolean updateAdminRolename(TbAdmin tbAdmin);
}