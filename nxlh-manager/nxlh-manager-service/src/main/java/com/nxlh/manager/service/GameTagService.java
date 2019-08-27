package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbBannerMapper;
import com.nxlh.manager.mapper.TbGameTagMapper;
import com.nxlh.manager.model.dbo.TbBanner;
import com.nxlh.manager.model.dbo.TbGameTag;
import com.nxlh.manager.model.dto.BannerDTO;
import com.nxlh.manager.model.dto.GameTagDTO;

public interface GameTagService extends BaseService<GameTagDTO, TbGameTagMapper, TbGameTag> {
}
