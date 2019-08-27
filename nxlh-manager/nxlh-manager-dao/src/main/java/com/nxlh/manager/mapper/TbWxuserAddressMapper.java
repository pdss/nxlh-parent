package com.nxlh.manager.mapper;

import com.nxlh.manager.model.dbo.TbWxuserAddress;
import tk.mybatis.mapper.common.Mapper;

public interface TbWxuserAddressMapper extends Mapper<TbWxuserAddress> {

    /**
     * 取消用户的默认收货地址
     * @param wxuserid
     * @return
     */
    Integer removeDefaultAddrByUserId(String wxuserid);
}