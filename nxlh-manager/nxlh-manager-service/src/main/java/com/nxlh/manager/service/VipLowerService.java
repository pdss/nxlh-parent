package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbVipLowerMapper;
import com.nxlh.manager.model.dbo.TbVipLower;
import com.nxlh.manager.model.dto.VipLowerDTO;

import java.util.Date;
import java.util.List;

public interface VipLowerService extends BaseService<VipLowerDTO, TbVipLowerMapper, TbVipLower> {

    /**
     * 获取用户最新降级记录
     *
     * @param date
     * @return
     */
    List<VipLowerDTO> getByUserIds(Date date);
}
