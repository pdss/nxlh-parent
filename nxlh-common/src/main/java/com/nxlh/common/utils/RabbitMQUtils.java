package com.nxlh.common.utils;

import com.rabbitmq.client.AMQP;
import org.springframework.amqp.core.MessageProperties;

import java.util.List;
import java.util.Map;

public class RabbitMQUtils {

    /**
     * 获取消息重试次数
     *
     * @param properties AMQP消息属性
     * @return 消息重试次数
     */
    public static Long getRetryCount(MessageProperties properties) {
        Long retryCount = 0L;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-death")) {
                    List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
                    if (deaths.size() > 0) {
                        Map<String, Object> death = deaths.get(0);
                        retryCount = (Long) death.get("count");
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return retryCount;
    }

    public static Long getRetryCount(AMQP.BasicProperties properties) {
        Long retryCount = 0L;
        try {
            Map<String, Object> headers = properties.getHeaders();
            if (headers != null) {
                if (headers.containsKey("x-death")) {
                    List<Map<String, Object>> deaths = (List<Map<String, Object>>) headers.get("x-death");
                    if (deaths.size() > 0) {
                        Map<String, Object> death = deaths.get(0);
                        retryCount = (Long) death.get("count");
                    }
                }
            }
        } catch (Exception ignored) {
        }

        return retryCount;
    }
}
