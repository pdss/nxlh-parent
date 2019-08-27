package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.manager.mapper.TbSmsMapper;
import com.nxlh.manager.model.dbo.TbSms;
import com.nxlh.manager.model.dto.SmsDTO;
import com.nxlh.manager.service.SmsService;

@Service(interfaceClass = SmsService.class)
public class SmsServiceImp extends BaseDbCURDSServiceImpl<TbSmsMapper, TbSms, SmsDTO> implements SmsService {

}
