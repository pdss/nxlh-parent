package com.nxlh.manager.amqp.bussiness;


import com.nxlh.common.utils.JsonUtils;
import com.nxlh.common.utils.MessageUtils;
import com.nxlh.manager.model.dto.NotificationDTO;
import com.nxlh.manager.model.dto.SmsDTO;
import com.nxlh.manager.service.NotificationService;
import com.nxlh.manager.service.SmsService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 短信发送监听队列
 */
@Slf4j
@Component
public class SmsAmqpService extends BaseAmqpService {


    @Autowired
    private SmsService smsService;

    @Autowired
    private MessageUtils messageUtils;

    /**
     * 短信通知
     */
    @RabbitListener(queues = "sms.send")
    @RabbitHandler()
    public void send(SmsDTO sms, Channel channel, Message message) throws IOException {
        var result = false;
        try {
            var resp = this.messageUtils.sendSms(sms.getPhone(), sms.getSignName(), sms.getTempCode(), JsonUtils.objectToJson(sms.getTempParams()));
            result = resp.getCode().equalsIgnoreCase("Ok");
            //短信发送成功，写库
            if (result) {
                this.smsService.add(sms);
                this.ackOk(message, channel);
                return ;
            }
            this.ackFail(message,channel);
        } catch (Exception ex) {
            ex.printStackTrace();
            this.log.error(ex.toString());
            this.ackFail(message, channel);
        }
    }
}
