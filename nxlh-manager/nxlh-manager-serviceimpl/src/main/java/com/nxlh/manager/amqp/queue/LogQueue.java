package com.nxlh.manager.amqp.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LogQueue {


    @Bean("log_queue")
    public Queue addQueue() {
        return new Queue(LogQueue);
    }


    @Bean("logEx")
    public DirectExchange logEx() {
        return new DirectExchange(Log_MASTER_EX, true, false);
    }


    public static final String LogQueue = "nxlh.log";


    //交换机名称
    public static final String Log_MASTER_EX = "logEx@master";


    @Bean
    public Binding bindLogEx() {
        return BindingBuilder.bind(addQueue()).to(logEx()).with(LogQueue);
    }


}
