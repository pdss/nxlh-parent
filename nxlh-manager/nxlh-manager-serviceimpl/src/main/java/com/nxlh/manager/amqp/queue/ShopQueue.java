package com.nxlh.manager.amqp.queue;


import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ShopQueue {

    @Bean(SHOP_SOLR_SAVE_QUEUE)
    public Queue addQueue() {
        return new Queue(SHOP_SOLR_SAVE_QUEUE);
    }

    @Bean(SHOP_SOLR_QUERY_QUEUE)
    public Queue searchQueue() {
        return new Queue(SHOP_SOLR_QUERY_QUEUE);
    }

    @Bean(SHOP_SOLR_DELETE_QUEUE)
    public Queue deleteQueue() {
        return new Queue(SHOP_SOLR_DELETE_QUEUE);
    }


    @Bean(SHOP_SOLR_MASTER_EX)
    public DirectExchange logEx() {
        return new DirectExchange(SHOP_SOLR_MASTER_EX, true, false);
    }

    //保存
    public static final String SHOP_SOLR_SAVE_QUEUE = "shop.solr.save.queue";

    //搜索
    public static final String SHOP_SOLR_QUERY_QUEUE = "shop.solr.query.queue";

    //删除
    public static  final String SHOP_SOLR_DELETE_QUEUE ="shop.solr.delete.queue";


    //交换机名称
    public static final String SHOP_SOLR_MASTER_EX = "shop_solrEx@master";


    @Bean
    public Binding bindEx() {
        return BindingBuilder.bind(addQueue()).to(logEx()).with(SHOP_SOLR_SAVE_QUEUE);
    }

    @Bean
    public Binding bindSearchEx(){ return BindingBuilder.bind(searchQueue()).to(logEx()).with(SHOP_SOLR_QUERY_QUEUE); };

    @Bean
    public Binding bindDeleteEx(){ return BindingBuilder.bind(deleteQueue()).to(logEx()).with(SHOP_SOLR_DELETE_QUEUE); };


}
