package com.nxlh.manager.amqp.bussiness;


import com.nxlh.common.utils.IDUtils;
import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.MessageUtils;
import com.nxlh.manager.model.dto.SmsDTO;
import com.nxlh.manager.model.dto.WxuserLotteryDTO;
import com.nxlh.manager.service.SmsService;
import com.nxlh.manager.service.WxuserLotteryService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static com.nxlh.manager.amqp.queue.LotteryQueue.LOTTERYQueue;


@Slf4j
@Component
public class LotteryAmqpService extends BaseAmqpService {


    @Autowired
    private WxuserLotteryService wxuserLotteryService;

    /**
     * 记录用户的抽奖记录
     *
     * @throws IOException
     */
    @RabbitListener(queues = LOTTERYQueue)
    @RabbitHandler()
    public void send(WxuserLotteryDTO record, Channel channel, Message message) throws IOException {
        var result = false;
        try {

            this.wxuserLotteryService.add(record);
            this.ackOk(message, channel);
        } catch (Exception ex) {
            this.log.error(ex.toString());
            this.ackFail(message, channel);
        }
    }
}
