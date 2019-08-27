package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbMinappConfigMapper;
import com.nxlh.manager.model.dbo.TbMinappConfig;
import com.nxlh.manager.model.dto.MinappConfigDTO;
import com.nxlh.manager.service.MinappConfigService;

@Service(interfaceClass = MinappConfigService.class)
public class MinappConfigServiceImpl extends BaseDbCURDSServiceImpl<TbMinappConfigMapper, TbMinappConfig, MinappConfigDTO> implements MinappConfigService {


    @Override
    public MinappConfigDTO getbgImg() {
        TbMinappConfig tbMinappConfig = this.baseMapper.selectOneByExample(null);
        MinappConfigDTO minappConfigDTO = this.beanMap(tbMinappConfig, this.currentDTOClass());
        return minappConfigDTO;
    }

    @Override
    public String getThemeBgImage() {
        var config = this.baseMapper.selectOneByExample(null);
        return config.getBackground();
    }
}
