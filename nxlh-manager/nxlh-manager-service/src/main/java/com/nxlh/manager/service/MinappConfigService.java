package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbMinappConfigMapper;
import com.nxlh.manager.model.dbo.TbMinappConfig;
import com.nxlh.manager.model.dto.MinappConfigDTO;

import java.util.List;

public interface MinappConfigService extends BaseService<MinappConfigDTO, TbMinappConfigMapper, TbMinappConfig> {

    /**
     * 获取微信背景图的数据
     *
     * @return
     */
    MinappConfigDTO getbgImg();

    /**
     * 获取指定的背景图
     * @return
     */
    String getThemeBgImage();

}
