package com.example.tastefulai.domain.chatting.redis;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.service.ChattingMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisSubscriber {

    private final ChattingMessageService chattingMessageService;
    private final ObjectMapper objectMapper;

    //redis 채널에서 수신한 메시지 처리
    public void handleMessage(String message) {
        try {
            ChattingMessageResponseDto chattingMessageResponseDto = objectMapper.readValue(message, ChattingMessageResponseDto.class);
            chattingMessageService.processReceivedMessage(chattingMessageResponseDto);
        } catch (JsonProcessingException jsonProcessingException) {
            log.error("Redis 메시지 역직렬화 오류: {}", jsonProcessingException.getMessage());
        }
    }
}
