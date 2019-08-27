package com.nxlh.manager.amqp.bussiness;


import com.nxlh.manager.model.dto.NotificationDTO;
import com.nxlh.manager.service.NotificationService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 站内消息监听队列
 */
@Slf4j
@Component
public class NotificationAmqpService extends BaseAmqpService  {


    @Autowired
    private NotificationService notificationService;


    /**
     * 站内用户消息通知
     */
    @RabbitListener(queues = "notification.add")
    @RabbitHandler()
    public void send(NotificationDTO notifi, Channel channel, Message message){





    }


}
