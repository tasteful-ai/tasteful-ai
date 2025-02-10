package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMessageServiceImpl implements RedisMessageService {

    @Qualifier("messageCacheRedisTemplate")
    private final RedisTemplate<String, Object> messageCacheRedisTemplate;
    private final ObjectMapper objectMapper;
    private static final int MAX_MESSAGE_COUNT = 50;

    /**
     * Redis 에 메시지 저장 -> 채팅 메시지를 직렬화 후 Redis 리스트로 추가, 메시지가 50개를 초과하면 가장 오래된 메시지 삭제
     */
    @Override
    public void saveMessage(Long chattingroomId, ChattingMessageResponseDto chattingMessageResponseDto) {
        try {
            String key = getRedisKey(chattingroomId);
            String serializedMessage = objectMapper.writeValueAsString(chattingMessageResponseDto);

            messageCacheRedisTemplate.opsForList().rightPush(key, serializedMessage);
            messageCacheRedisTemplate.opsForList().trim(key, -MAX_MESSAGE_COUNT, -1);

        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Redis 메시지 저장 오류: {}", jsonProcessingException.getMessage());
        }
    }

    @Override
    public List<ChattingMessageResponseDto> getRecentMessages(Long chattingroomId) {
        String key = getRedisKey(chattingroomId);
        List<Object> messages = messageCacheRedisTemplate.opsForList().range(key, 0, -1);

        //JSON 문자열을 객체로 역직렬화
        return messages.stream()
                .map(msg -> {
                    try {
                        return objectMapper.readValue(msg.toString(), ChattingMessageResponseDto.class);
                    } catch (JsonProcessingException jsonProcessingException) {
                        log.error("Redis 메시지 조회 오류: {}", jsonProcessingException.getMessage());
                        return null;
                    }
                })
                .filter(msg -> msg != null) //null 값 필터링
                .collect(Collectors.toList());
    }

    private String getRedisKey(Long chattingroomId) {
        return "chat:messages:" + chattingroomId;
    }
}
