package com.nxlh.manager.serviceimpl;

import com.alibaba.dubbo.config.annotation.Service;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.amqp.queue.LogQueue;
import com.nxlh.manager.mapper.TbMqFailMapper;
import com.nxlh.manager.model.dbo.TbMqFail;
import com.nxlh.manager.model.dto.MqFailDTO;
import com.nxlh.manager.service.MqFailService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;

@Service(interfaceClass = MqFailService.class)
public class MqFailServiceImp extends BaseDbCURDSServiceImpl<TbMqFailMapper, TbMqFail, MqFailDTO> implements MqFailService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public void send(MqFailDTO model) {
        this.rabbitTemplate.convertAndSend(LogQueue.Log_MASTER_EX, LogQueue.LogQueue, model, new CorrelationData(IDUtils.genUUID()));
    }

    @Override
    public void send(String error) {
        this.rabbitTemplate.convertAndSend(LogQueue.Log_MASTER_EX, LogQueue.LogQueue, error, new CorrelationData(IDUtils.genUUID()));
    }
}
