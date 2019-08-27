package com.nxlh.manager.service;

import com.nxlh.common.model.MyResult;
import com.nxlh.manager.mapper.TbWxuserAddressMapper;
import com.nxlh.manager.mapper.TbWxuserMapper;
import com.nxlh.manager.model.dbo.TbWxuser;
import com.nxlh.manager.model.dbo.TbWxuserAddress;
import com.nxlh.manager.model.dto.WxUserAddressDTO;
import com.nxlh.manager.model.dto.WxUserDTO;

import java.util.List;

public interface WxUserAddressService extends BaseService<WxUserAddressDTO, TbWxuserAddressMapper, TbWxuserAddress> {


    /**
     * 获取微信用户的收货地址
     *
     * @param wxUid
     * @return
     */
    List<WxUserAddressDTO> getByWxUId(String wxUid);


    /**
     * 用户的默认收货地址
     *
     * @param wxUid
     * @param getFirst 如果没有默认地址，是否随机获取第一个
     * @return
     */
    WxUserAddressDTO getDefaultAddr(String wxUid, boolean getFirst);


    /**
     * 添加、编辑用户地址(每个用户只能有一个默认地址)
     * @param addressDTO
     * @return
     */
    boolean addOrUpdateAddress(WxUserAddressDTO addressDTO);


    /**
     * 取消用户的默认地址
     * @param wxUserId
     * @return
     */
    Integer removeDefaultAddrByUserId(String wxUserId);


}
