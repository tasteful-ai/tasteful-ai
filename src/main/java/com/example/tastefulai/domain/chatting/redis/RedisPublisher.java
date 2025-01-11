package com.example.tastefulai.domain.chatting.redis;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String CHANNEL = "chatroom";

    public void publishMessage(ChattingMessageResponseDto chattingMessageResponseDto) {
        try {
            String serializedMessage = objectMapper.writeValueAsString(chattingMessageResponseDto);
            redisTemplate.convertAndSend(CHANNEL, serializedMessage);
            log.info("Redis 채널 '{}'로 메시지 발행: {}", CHANNEL, serializedMessage);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Redis 메시지 직렬화 오류: {}", jsonProcessingException.getMessage());
        }
    }
}
