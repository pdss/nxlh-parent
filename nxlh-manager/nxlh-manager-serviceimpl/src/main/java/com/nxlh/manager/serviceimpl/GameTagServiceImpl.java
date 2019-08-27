package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbGameTagMapper;
import com.nxlh.manager.model.dbo.TbGameTag;
import com.nxlh.manager.model.dto.GameTagDTO;
import com.nxlh.manager.service.GameTagService;

@Service(interfaceClass = GameTagService.class)
public class GameTagServiceImpl extends BaseDbCURDSServiceImpl<TbGameTagMapper, TbGameTag, GameTagDTO> implements GameTagService {

}
