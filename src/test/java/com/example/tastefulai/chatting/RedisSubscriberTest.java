package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisSubscriber;
import com.example.tastefulai.domain.chatting.service.ChattingMessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class RedisSubscriberTest {

    @Test
    void testHandleMessage() throws Exception {
        ChattingMessageService messageService = mock(ChattingMessageService.class);
        ObjectMapper objectMapper = new ObjectMapper();
        RedisSubscriber subscriber = new RedisSubscriber(messageService, objectMapper);

        String messageJson = "{\"senderNickname\":\"testUser\", \"message\":\"hello\"}";
        ChattingMessageResponseDto messageResponseDto = objectMapper.readValue(messageJson, ChattingMessageResponseDto.class);

        subscriber.handleMessage(messageJson);

        verify(messageService, times(1)).processReceivedMessage(messageResponseDto);
    }
}
