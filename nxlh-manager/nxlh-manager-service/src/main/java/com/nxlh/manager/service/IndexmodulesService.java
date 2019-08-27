package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbIndexmodulesMapper;
import com.nxlh.manager.model.dbo.TbIndexmodules;
import com.nxlh.manager.model.dto.IndexmodulesDTO;

import java.util.List;
import java.util.Map;

public interface IndexmodulesService extends BaseService<IndexmodulesDTO, TbIndexmodulesMapper, TbIndexmodules> {

    Map<String,List<IndexmodulesDTO>> indexModules();
}
