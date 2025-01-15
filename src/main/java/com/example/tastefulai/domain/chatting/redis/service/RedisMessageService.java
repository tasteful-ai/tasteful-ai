package com.example.tastefulai.domain.chatting.redis.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisMessageService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    //Redis 키와 최대 메시지 수 설정
    private static final String CHAT_MESSAGES_KEY = "chat:messages";
    private static final int MAX_MESSAGE_COUNT = 50;

    //Redis 에 메시지 저장 -> 채팅 메시지를 직렬화 후 Redis 리스트로 추가, 메시지가 50개를 초과하면 가장 오래된 메시지 삭제
    public void saveMessage(ChattingMessageResponseDto chattingMessageResponseDto) {
        try {
            //메시지 객체를 JSON 문자열로 직렬화 후 Redis 리스트의 끝에 추가
            String serializedMessage = objectMapper.writeValueAsString(chattingMessageResponseDto);
            redisTemplate.opsForList().rightPush(CHAT_MESSAGES_KEY, serializedMessage);

            //메시지가 50개를 초과하면 가장 오래된 메시지부터 삭제
            if (redisTemplate.opsForList().size(CHAT_MESSAGES_KEY) > MAX_MESSAGE_COUNT) {
                redisTemplate.opsForList().leftPop(CHAT_MESSAGES_KEY);
            }
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Redis 메시지 저장 오류: {}", jsonProcessingException.getMessage());
        }
    }

    public List<ChattingMessageResponseDto> getRecentMessages() {
        List<Object> messages = redisTemplate.opsForList().range(CHAT_MESSAGES_KEY, 0, -1);

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
}
