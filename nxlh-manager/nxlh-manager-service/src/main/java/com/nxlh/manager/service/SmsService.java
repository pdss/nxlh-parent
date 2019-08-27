package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbSmsMapper;
import com.nxlh.manager.mapper.TbTagMapper;
import com.nxlh.manager.model.dbo.TbSms;
import com.nxlh.manager.model.dbo.TbTag;
import com.nxlh.manager.model.dto.SmsDTO;
import com.nxlh.manager.model.dto.TagDTO;

import java.util.List;

public interface SmsService extends BaseService<SmsDTO, TbSmsMapper, TbSms> {



}
