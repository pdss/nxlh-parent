package com.nxlh.manager.amqp.queue;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LotteryQueue {


    @Bean("lottery_queue")
    public Queue addQueue() {
        return new Queue(LOTTERYQueue);
    }


    @Bean("lotteryEx")
    public DirectExchange logEx() {
        return new DirectExchange(LOTTERY_MASTER_EX, true, false);
    }


    public static final String LOTTERYQueue = "lottery.userrecord";


    //交换机名称
    public static final String LOTTERY_MASTER_EX = "lotteryEx@master";


    @Bean
    public Binding bindLotteryEx() {
        return BindingBuilder.bind(addQueue()).to(logEx()).with(LOTTERYQueue);
    }


}
