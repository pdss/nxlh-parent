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
public class OrderQueue {



    @Bean("order_success_queue")
    public Queue addQueue() {
        return new Queue(ORDER_SUCCESS);
    }

    @Bean("order_seckill_success_queue")
    public Queue seckillQueue() {
        return new Queue(ORDER_SECKILL_SUCCESS);
    }


    @Bean("order_payretry_queue")
    public Queue reryQueue() {
        return new Queue(ORDER_RETRY);
    }


    @Bean("order_payfail_queue")
    public Queue failQueue() {
        return new Queue(ORDER_FAIL);
    }



    @Bean("orderEx")
    public DirectExchange orderEx() {
        return new DirectExchange(MASTER_EX, true, false);
    }

    @Bean("orderExRetry")
    public DirectExchange orderExRetry() {
        return new DirectExchange(RETRY_EX, true, false);
    }

    @Bean("orderExFail")
    public DirectExchange orderExFail() {
        return new DirectExchange(FAIL_EX, true, false);
    }


    //支付成功--普通支付
    public static final String ORDER_SUCCESS = "order.paysuccess";
    //支付成功--秒杀订单
    public static  final String ORDER_SECKILL_SUCCESS="order.seckill.paysuccess";
    public static final String ORDER_RETRY = "order.payretry";
    public static final String ORDER_FAIL = "order.payfail";



    //交换机名称
    public static final String MASTER_EX = "orderEx@master";
    public static final String RETRY_EX = "orderEx@retry";
    public static final String FAIL_EX = "orderEx@fail";


    /**
     * 订单支付成功队列绑定
     * @return
     */
    @Bean
    public Binding bindOrderPay() {
        return BindingBuilder.bind(addQueue()).to(orderEx()).with(ORDER_SUCCESS);
    }

    @Bean
    public Binding bindOrderSeckillPay() {
        return BindingBuilder.bind(addQueue()).to(orderEx()).with(ORDER_SECKILL_SUCCESS);
    }

    @Bean
    public Binding bindOrderRetry() {
        return BindingBuilder.bind(reryQueue()).to(orderExRetry()).with(ORDER_RETRY);
    }

    @Bean
    public Binding bindOrderFail() {
        return BindingBuilder.bind(failQueue()).to(orderExFail()).with(ORDER_FAIL);
    }




}
