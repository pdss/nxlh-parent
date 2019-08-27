package com.nxlh.manager.amqp.bussiness;


import com.alibaba.dubbo.config.annotation.Reference;
import com.nxlh.common.utils.DateUtils;
import com.nxlh.common.utils.IDUtils;
import com.nxlh.manager.amqp.queue.LogQueue;
import com.nxlh.manager.model.dto.LogDTO;
import com.nxlh.manager.model.enums.LogLevelEnums;
import com.nxlh.manager.service.LogService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 系统日志
 */
@Slf4j
@Component
public class LogAmqpService extends BaseAmqpService {


    @Autowired
    private LogService logService;

    @RabbitHandler
    @RabbitListener(queues = LogQueue.LogQueue)
    private void log(Message message, Channel channel) throws IOException {

        try {
            var dto = new LogDTO();
            dto.setId(IDUtils.genUUID());
            dto.setAddtime(DateUtils.now());
            dto.setLevel(LogLevelEnums.Level.fata.getValue());
            dto.setLog(new String(message.getBody()));
            this.logService.add(dto);
            this.ackOk(message, channel);
        } catch (Exception ex) {
            this.ackReject(message, channel);
        }

    }
}
