package com.nxlh.manager.service;

import com.nxlh.manager.mapper.TbLogMapper;
import com.nxlh.manager.mapper.TbMqFailMapper;
import com.nxlh.manager.model.dbo.TbLog;
import com.nxlh.manager.model.dbo.TbMqFail;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.model.dto.MqFailDTO;

public interface MqFailService extends BaseService<MqFailDTO, TbMqFailMapper, TbMqFail> {

    void send(MqFailDTO model);

    void send(String error);


}
