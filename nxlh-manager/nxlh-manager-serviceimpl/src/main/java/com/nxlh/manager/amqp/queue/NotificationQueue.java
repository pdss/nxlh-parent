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
public class NotificationQueue {


    public static final  String NOTIFICATION_ROUTINGKEY="notification.*";

    @Bean("notification_add_queue")
    public Queue addQueue() {
        return new Queue("notification.add");
    }


    @Bean
    public DirectExchange directExchange(){ return new DirectExchange("directExchange");}


    /**
     * 站内消息绑定
     * @return
     */
    @Bean
    public Binding bindShoppingCarAdd(@Qualifier("notification_add_queue") Queue queue, DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(RoutingKey.NOTIFICATION_ADD);
    }



}
