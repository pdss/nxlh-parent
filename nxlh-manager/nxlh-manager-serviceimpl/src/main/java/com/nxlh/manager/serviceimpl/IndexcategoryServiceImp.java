package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbIndexcategoryMapper;
import com.nxlh.manager.model.dbo.TbIndexcategory;
import com.nxlh.manager.model.dto.IndexcategoryDTO;
import com.nxlh.manager.service.IndexcategoryService;

@Service(interfaceClass = IndexcategoryService.class)
public class IndexcategoryServiceImp extends BaseDbCURDSServiceImpl<TbIndexcategoryMapper, TbIndexcategory, IndexcategoryDTO> implements IndexcategoryService {
    
}
