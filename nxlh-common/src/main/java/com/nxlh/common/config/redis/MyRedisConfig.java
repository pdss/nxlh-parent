package com.nxlh.common.config.redis;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nxlh.common.utils.protobuf.ProtostuffSerializer;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.Resource;
import java.lang.reflect.Method;
import java.time.Duration;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableCaching
public class MyRedisConfig {


//    @Bean
//    public LettuceConnectionFactory lettuceConnectionFactory() {
//
//        Map<String, Object> source = new HashMap<String, Object>();
//
//        source.put("spring.redis.cluster.nodes", environment.getProperty("spring.redis.cluster.nodes"));
//        source.put("spring.redis.cluster.timeout", environment.getProperty("spring.redis.timeout"));
////        source.put("spring.redis.cluster.max-redirects", environment.getProperty("spring.redis.cluster.max-redirects"));
//        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(new MapPropertySource("RedisClusterConfiguration", source));
//        return new LettuceConnectionFactory(redisClusterConfiguration);
//    }


    /**
     * 通用的 RedisTemplate,Jackson
     *
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(LettuceConnectionFactory lettuceConnectionFactory) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(lettuceConnectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        ProtostuffSerializer valueSerializer = new ProtostuffSerializer();
        template.setValueSerializer(valueSerializer);
        template.setHashKeySerializer(template.getKeySerializer());
        template.setHashValueSerializer(template.getValueSerializer());
        return template;
    }






    @Bean
    public KeyGenerator keyGenerator() {
        return new KeyGenerator() {
            @Override
            public Object generate(Object target, Method method, Object... params) {
                //全类名
                StringBuilder builder = new StringBuilder(target.getClass().getName());
                //方法名
                builder.append("." + method.getName());
                //参数列表
                Class<?>[] types = method.getParameterTypes();
                if (ArrayUtils.isNotEmpty(types)) {
                    builder.append("(");
                    for (Class<?> type : types) {
                        String name = type.getName();
                        builder.append(name + ",");
                    }
                    builder.append(")");
                }
                //参数列表对应的参数值
                if (ArrayUtils.isNotEmpty(params)) {
                    builder.append("[");
                    for (Object p : params) {
                        builder.append(p + ",");
                    }
                    builder.append("]");
                }
//                System.out.println("--------------------keyGenerator:"+builder.toString());
                return builder.toString();
            }
        };
    }


    @Bean
    @Primary()
    public CacheManager cacheManager(RedisTemplate redisTemplate) {
        RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(redisTemplate.getConnectionFactory());
        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisTemplate.getValueSerializer()))
                .entryTtl(Duration.ofDays(1)); //默认1天

       return new RedisCacheManager(redisCacheWriter, redisCacheConfiguration);

    }



    //==================消息订阅=======================

//    @Bean
//    public RedisMessageListenerContainer listenerContainer(RedisConnectionFactory redisConnection, Executor executor){
//        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
//        // 设置Redis的连接工厂
//        container.setConnectionFactory(redisConnection);
//        // 设置监听使用的线程池
////        container.setTaskExecutor(executor);
//        // 设置监听的Topic: PatternTopic/ChannelTopic
//        Topic topic = new PatternTopic(RedisExpiredListener.LISTENER_PATTERN);
//        // 设置监听器
//        container.addMessageListener(new RedisExpiredListener(), topic);
//        return container;
//    }
//
//    @Bean
//    public Executor executor(){
//        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
//        executor.setCorePoolSize(10);
//        executor.setMaxPoolSize(20);
//        executor.setQueueCapacity(100);
//        executor.setKeepAliveSeconds(60);
//        executor.setThreadNamePrefix("V-Thread");
//
//        // rejection-policy：当pool已经达到max size的时候，如何处理新任务
//        // CALLER_RUNS：不在新线程中执行任务，而是由调用者所在的线程来执行
//        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
//        executor.initialize();
//        return executor;
//    }


}


