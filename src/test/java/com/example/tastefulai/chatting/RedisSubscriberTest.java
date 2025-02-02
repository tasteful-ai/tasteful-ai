package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisSubscriber;
import com.example.tastefulai.domain.chatting.service.ChattingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisSubscriberTest {

    @InjectMocks
    private RedisSubscriber redisSubscriber;

    @Mock
    private ChattingService chattingService;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Redis 메시지 수신 및 처리")
    void handleMessage_Success() throws Exception {
        String message = "{\"senderId\":1,\"senderNickname\":\"User\",\"message\":\"Hello\",\"chattingroomId\":1}";

        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(1L, "User", "Hello", 1L);

        when(objectMapper.readValue(message, ChattingMessageResponseDto.class)).thenReturn(chattingMessageResponseDto);

        redisSubscriber.handleMessage(message);

        verify(chattingService, times(1)).processReceivedMessage(chattingMessageResponseDto);
    }
}
