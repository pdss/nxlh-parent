package com.nxlh.manager.amqp.bussiness;

import com.nxlh.common.utils.RabbitMQUtils;
import com.nxlh.common.utils.redis.RedisCommand;
import com.nxlh.manager.amqp.RoutingKey;
import com.nxlh.manager.amqp.queue.ShoppingCarQueue;
import com.nxlh.manager.service.MqFailService;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class BaseAmqpService {

    @Autowired
    @Lazy
    protected RedisCommand redisService;

    @Autowired
    @Lazy
    protected RabbitTemplate rabbitTemplate;

    @Autowired
    @Lazy
    protected MqFailService mqFailService;


    protected void ackOk(Message message, Channel channel) throws IOException {
        //则仅表示deliveryTag的消息已经成功处理。
        channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
    }


    protected void ackFail(Message message, Channel channel) throws IOException {
        //表示deliveryTag的消息处理失败且将该消息重新放回队列。
        channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
    }

    protected void ackReject(Message message, Channel channel) throws IOException {
        //告诉服务器该消息处理失败并且丢弃该消息
        channel.basicReject(message.getMessageProperties().getDeliveryTag(), false);
    }


    protected MessagePostProcessor setHeaders(Map<String, Object> headers) {
        return new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                headers.forEach((k, v) -> {
                    message.getMessageProperties().setHeader(k, v);
                });

                return message;
            }
        };
    }


    protected AMQP.BasicProperties createOverrideProperties(AMQP.BasicProperties properties, Map<String, Object> headers) {
        return new AMQP.BasicProperties(
                properties.getContentType(),
                properties.getContentEncoding(),
                headers,
                properties.getDeliveryMode(),
                properties.getPriority(),
                properties.getCorrelationId(),
                properties.getReplyTo(),
                properties.getExpiration(),
                properties.getMessageId(),
                properties.getTimestamp(),
                properties.getType(),
                properties.getUserId(),
                properties.getAppId(),
                properties.getClusterId()
        );
    }


    /**
     * 获取原始的routingKey
     *
     * @param properties   AMQP消息属性
     * @param defaultValue 默认值
     * @return 原始的routing-key
     */
    protected String getOrigRoutingKey(MessageProperties properties, String defaultValue) {
        String routingKey = defaultValue;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-orig-routing-key")) {
                    routingKey = headers.get("x-orig-routing-key").toString();
                }
            }
        } catch (Exception ignored) {
        }

        return routingKey;
    }

    protected String getOrigExchange(MessageProperties properties, String defaultValue) {
        String routingKey = defaultValue;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-orig-exchange")) {
                    routingKey = headers.get("x-orig-exchange").toString();
                }
            }
        } catch (Exception ignored) {
        }

        return routingKey;
    }

    protected String getOrigQueue(MessageProperties properties, String defaultValue) {
        String routingKey = defaultValue;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-orig-queue")) {
                    routingKey = headers.get("x-orig-queue").toString();
                }
            }
        } catch (Exception ignored) {
        }

        return routingKey;
    }


    protected void retry(Channel channel, Message message, Object data, String retryEx, String retryQueue, String failEx, String failQueue) throws IOException {
        var retryCount = RabbitMQUtils.getRetryCount(message.getMessageProperties());
        var routingKey = this.getOrigRoutingKey(message.getMessageProperties(), message.getMessageProperties().getReceivedRoutingKey());

        if (retryCount > 3) {
            // 重试次数大于3次，则自动加入到失败队列
            this.log.error("failed. send message to failed exchange");

            Map<String, Object> headers = new HashMap<>();
            headers.put("x-orig-routing-key", routingKey);
            headers.put("x-orig-exchange", message.getMessageProperties().getReceivedExchange());
            headers.put("x-orig-queue", message.getMessageProperties().getConsumerQueue());
            this.rabbitTemplate.convertAndSend(failEx, failQueue, data, this.setHeaders(headers), new CorrelationData(message.getMessageProperties().getCorrelationId()));

        } else {
            // 重试次数小于3，则加入到重试队列，30s后再重试
            this.log.error("exception. send message to retry exchange");

            Map<String, Object> headers = message.getMessageProperties().getHeaders();
            if (headers == null) {
                headers = new HashMap<>();
            }

            headers.put("x-orig-routing-key", routingKey);
            headers.put("x-orig-exchange", message.getMessageProperties().getReceivedExchange());
            headers.put("x-orig-queue", message.getMessageProperties().getConsumerQueue());
            this.rabbitTemplate.convertAndSend(retryEx, retryQueue, data, this.setHeaders(headers), new CorrelationData(message.getMessageProperties().getCorrelationId()));
        }
        this.ackReject(message, channel);
    }

}
