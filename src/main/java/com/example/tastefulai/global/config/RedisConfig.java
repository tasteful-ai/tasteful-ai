package com.example.tastefulai.global.config;

import com.example.tastefulai.domain.chatting.redis.RedisSubscriber;
import com.example.tastefulai.domain.chatting.websocket.enums.RedisChannel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Slf4j
@Configuration
public class RedisConfig {

    // Redis 서버와 연결
    @Bean(name = "chatRedisConnectionFactory")
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory("localhost", 6379);
        log.info("Redis 연결 생성: {}:{}", lettuceConnectionFactory.getHostName(), lettuceConnectionFactory.getPort());
        return lettuceConnectionFactory;
    }

    @Bean(name = "chatRedisTemplate")
    public RedisTemplate<String, Object> chatRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean(name = "aiRedisTemplate")
    public RedisTemplate<String, Integer> aiRedisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Integer> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        return redisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        return new StringRedisTemplate(connectionFactory);
    }

    public ZSetOperations<String, String> zSetOperations(RedisTemplate<String, String> redisTemplate) {
        return redisTemplate.opsForZSet();
    }

    // Redis Pub/Sub 메시지 리스너 : 수신한 메시지를 처리
    @Bean
    public MessageListenerAdapter messageListener(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber, "handleMessage");
    }

    // Redis 채널 구독 및 메시지 수신 처리 : 메시지 리스너를 등록하여 Redis 채널 메시지를 수신
    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            RedisConnectionFactory chatRedisConnectionFactory,
            MessageListenerAdapter messageListener
    ) {
        RedisMessageListenerContainer redisMessageListenerContainer = new RedisMessageListenerContainer();

        // 특정 채널 "chatroom"을 구독
        redisMessageListenerContainer.setConnectionFactory(chatRedisConnectionFactory);
        redisMessageListenerContainer.addMessageListener(messageListener, new PatternTopic("chatroom:*"));

        return redisMessageListenerContainer;
    }
}
