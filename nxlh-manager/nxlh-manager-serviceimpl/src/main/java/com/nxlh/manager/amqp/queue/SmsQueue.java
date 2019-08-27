package com.nxlh.manager.amqp.queue;

import com.nxlh.manager.amqp.RoutingKey;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SmsQueue {


    public static final  String SMS_ROUTINGKEY="sms.*";

    @Bean("sms_send_queue")
    public Queue addQueue() {
        return new Queue("sms.send");
    }


    @Bean
    public DirectExchange directExchange(){ return new DirectExchange("directExchange");}


    /**
     * 发送短信绑定
     * @return
     */
    @Bean
    public Binding bindShoppingCarAdd(@Qualifier("sms_send_queue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(RoutingKey.SMS_SEND);
    }



}
