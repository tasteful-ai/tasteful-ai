package com.example.tastefulai.domain.chatting.redis;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
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

    private final RedisTemplate<String, String> pubSubRedisTemplate;
    private final ObjectMapper objectMapper;

    public void publishMessage(Long chattingroomId, ChattingMessageResponseDto chattingMessageResponseDto) {
        try {
            String channel = getChannelName(chattingroomId);
            String serializedMessage = objectMapper.writeValueAsString(chattingMessageResponseDto);

            pubSubRedisTemplate.convertAndSend(channel, serializedMessage);
            log.info("Redis 채널 '{}'로 메시지 발행: {}", channel, serializedMessage);

        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Redis 메시지 직렬화 오류: {}", jsonProcessingException.getMessage());

            String fallbackMessage = "{\"message\": \"메시지 전송에 실패하였습니다. 잠시 후 다시 시도해주세요.\", \"senderNickname\": \"System\"}";
            pubSubRedisTemplate.convertAndSend(getChannelName(chattingroomId), fallbackMessage);
            log.warn("Redis Fallback 메시지를 전송했습니다.");

            throw new CustomException(ErrorCode.REDIS_SERIALIZATION_ERROR);
        }
    }

    private String getChannelName(Long chattingroomId) {
        return "chatroom:" + chattingroomId;
    }
}
