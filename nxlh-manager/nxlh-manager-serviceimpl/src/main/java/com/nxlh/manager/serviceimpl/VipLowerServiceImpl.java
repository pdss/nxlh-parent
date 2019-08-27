package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbVipLowerMapper;
import com.nxlh.manager.model.dbo.TbVipLower;
import com.nxlh.manager.model.dto.VipLowerDTO;
import com.nxlh.manager.service.VipLowerService;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;

@Service(interfaceClass = VipLowerService.class)
@Slf4j
public class VipLowerServiceImpl extends BaseDbCURDSServiceImpl<TbVipLowerMapper, TbVipLower, VipLowerDTO> implements VipLowerService {

    @Override
    public List<VipLowerDTO> getByUserIds(Date date) {
        List<TbVipLower> byUserIds = this.baseMapper.getByUserIds(date);
        return this.beanListMap(byUserIds, currentDTOClass());
    }
}
