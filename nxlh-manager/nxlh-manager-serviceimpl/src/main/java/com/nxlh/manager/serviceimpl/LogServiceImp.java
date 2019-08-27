package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbLogMapper;
import com.nxlh.manager.model.dbo.TbLog;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.service.LogService;

@Service(interfaceClass = LogService.class)
public class LogServiceImp extends BaseDbCURDSServiceImpl<TbLogMapper, TbLog, LogDTO> implements LogService {

}
