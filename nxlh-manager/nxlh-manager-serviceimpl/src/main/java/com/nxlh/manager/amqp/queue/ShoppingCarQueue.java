package com.nxlh.manager.amqp.queue;

import com.nxlh.manager.amqp.RoutingKey;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.nxlh.manager.amqp.RoutingKey.*;

@Configuration
public class ShoppingCarQueue {


    public static final String SHOPPINGCAR_ROUTINGKEY = "shoppingcar.*";

    @Bean("shoppingcar_add_queue")
    public Queue addQueue() {
        return new Queue(SHOPPINGCAR_ADD,true,false,false);
    }

    @Bean("shoppingcar_update_queue")
    public Queue updateQueue() {
        return new Queue(SHOPPINGCAR_UPDATE,true,false,false);
    }

    @Bean("shoppingcar_delete_queue")
    public Queue shoppingCarDeleteQueue() {
        return new Queue(SHOPPINGCAR_DELETE,true,false,false);
    }

     //交换机名称
    public static final String MASTER_EX = "shoppingcarEx@master";
    public static final String RETRY_EX = "shoppingcarEx@retry";
    public static final String FAIL_EX = "shoppingcarEx@fail";

    //队列名称
    public static final String SHOPPINGCAR_ADD = "shoppingcar.add";
    public static final String SHOPPINGCAR_UPDATE = "shoppingcar.update";
    public static final String SHOPPINGCAR_DELETE = "shoppingcar.delete";
    public static final String SHOPPINGCAR_FAIL ="shoppingcar.fail";
    public static final String SHOPPINGCAR_RETRY_ADD ="shoppingcar.retry.add";
    public static final String SHOPPINGCAR_RETRY_DELETE ="shoppingcar.retry.delete";
    public static final String SHOPPINGCAR_RETRY_UPDATE ="shoppingcar.retry.update";


    /**
     * 失败处理队列
     *
     * @return
     */
    @Bean("shoppingcar_fail_queue")
    public Queue shoppingCarFailQueue() {
        return new Queue(SHOPPINGCAR_FAIL,true,false,false);
    }

    /**
     * 重试队列-添加
     *
     * @return
     */
    @Bean("shoppingcar_retry_add_queue")
    public Queue shoppingCarRetryAddQueue() {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MASTER_EX);
        arguments.put("x-message-ttl", 15 * 1000);
        arguments.put("x-dead-letter-routing-key", SHOPPINGCAR_ADD);
        return new Queue(SHOPPINGCAR_RETRY_ADD, true, false, false, arguments);
    }

    /**
     * 重试队列-删除
     *
     * @return
     */
    @Bean("shoppingcar_retry_delete_queue")
    public Queue shoppingCarRetryDeleteQueue() {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MASTER_EX);
        arguments.put("x-message-ttl", 15 * 1000);
        arguments.put("x-dead-letter-routing-key", SHOPPINGCAR_DELETE);
        return new Queue(SHOPPINGCAR_RETRY_DELETE, true, false, false, arguments);
    }

    /**
     * 重试队列-修改
     *
     * @return
     */
    @Bean("shoppingcar_retry_update_queue")
    public Queue shoppingCarRetryUpdateQueue() {

        Map<String, Object> arguments = new HashMap<>();
        arguments.put("x-dead-letter-exchange", MASTER_EX);
        arguments.put("x-message-ttl", 15 * 1000);
        arguments.put("x-dead-letter-routing-key", SHOPPINGCAR_UPDATE);
        return new Queue(SHOPPINGCAR_RETRY_UPDATE, true, false, false, arguments);
    }


    /**
     * 交换机
     * @return
     */
    @Bean("shoppingcarEx")
    public DirectExchange shoppingcarEx() {
        return new DirectExchange(MASTER_EX, true, false);
    }

    @Bean("shoppingcarExRetry")
    public DirectExchange shoppingcarExRetry() {
        return new DirectExchange(RETRY_EX, true, false);
    }

    @Bean("shoppingcarExFail")
    public DirectExchange shoppingcarExFail() {
        return new DirectExchange(FAIL_EX, true, false);
    }


    /**
     * 购物车队列绑定
     *
     * @return
     */
    @Bean
    public Binding bindShoppingCarAdd1( ) {
        return BindingBuilder.bind(addQueue()).to(shoppingcarEx()).with(SHOPPINGCAR_ADD);
    }

    @Bean
    public Binding bindShoppingCarUpdate(@Qualifier("shoppingcar_update_queue") Queue queue, @Qualifier("shoppingcarEx") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_UPDATE);
    }

    @Bean
    public Binding bindShoppingCarDelete(@Qualifier("shoppingcar_delete_queue") Queue queue, @Qualifier("shoppingcarEx") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_DELETE);
    }

    @Bean
    public Binding bindShoppingCarRetryAdd(@Qualifier("shoppingcar_retry_add_queue") Queue queue, @Qualifier("shoppingcarExRetry") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_RETRY_ADD);
    }

    @Bean
    public Binding bindShoppingCarRetryDelete(@Qualifier("shoppingcar_retry_delete_queue") Queue queue, @Qualifier("shoppingcarExRetry") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_RETRY_DELETE);
    }

    @Bean
    public Binding bindShoppingCarRetryUpdate(@Qualifier("shoppingcar_retry_update_queue") Queue queue, @Qualifier("shoppingcarExRetry") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_RETRY_UPDATE);
    }

    @Bean
    public Binding bindShoppingCarFail(@Qualifier("shoppingcar_fail_queue") Queue queue, @Qualifier("shoppingcarExFail") DirectExchange directExchange) {
        return BindingBuilder.bind(queue).to(directExchange).with(SHOPPINGCAR_FAIL);
    }

}
