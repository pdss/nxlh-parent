package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbLogMapper;
import com.nxlh.manager.mapper.TbSmsMapper;
import com.nxlh.manager.model.dbo.TbLog;
import com.nxlh.manager.model.dbo.TbSms;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.model.dto.SmsDTO;

public interface LogService extends BaseService<LogDTO, TbLogMapper, TbLog> {



}
