package com.nxlh.manager.amqp;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
@Slf4j
public class RabbitConfig {

    @Autowired
    private ConnectionFactory connectionFactory;


    @Bean
    @Primary
    public RabbitTemplate rabbitTemplate() {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(new Jackson2JsonMessageConverter());
        template.setEncoding("UTF-8");
        template.setMandatory(true);
        //消息发送失败回调
        template.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            String correlationId = message.getMessageProperties().getCorrelationIdString();
            log.debug("消息：{} 发送失败, 应答码：{} 原因：{} 交换机: {}  路由键: {}", correlationId, replyCode, replyText, exchange, routingKey);
        });
        //发送确认回调
        template.setConfirmCallback(((correlationData, ack, cause) -> {
            if (ack) {
                log.debug("消息发送到exchange成功,id: {}", correlationData.getId());
            } else {
                log.debug("消息发送到exchange失败,原因: {}", cause);
            }
        }));

        return template;
    }

    @Bean
    public Jackson2JsonMessageConverter converter() {
        return new Jackson2JsonMessageConverter();
    }


}
