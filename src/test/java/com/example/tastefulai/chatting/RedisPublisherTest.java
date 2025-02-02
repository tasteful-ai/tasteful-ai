package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RedisPublisherTest {

    @InjectMocks
    private RedisPublisher redisPublisher;

    @Mock
    private RedisTemplate<String, String> pubSubRedisTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Redis 채널에 메시지 발송 성공")
    void publishMessage_Success() throws JsonProcessingException {
        Long chattingroomId = 1L;
        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(1L, "User", "Hello", chattingroomId);

        String serializedMessage = "{\"senderId\":1,\"senderNickname\":\"User\",\"message\":\"Hello\",\"chattingroomId\":1}";

        when(objectMapper.writeValueAsString(chattingMessageResponseDto)).thenReturn(serializedMessage);

        redisPublisher.publishMessage(chattingroomId, chattingMessageResponseDto);

        verify(pubSubRedisTemplate, times(1)).convertAndSend(anyString(), eq(serializedMessage));
    }

    @Test
    @DisplayName("Redis 메시지 직렬화 실패 & fallback 메시지 전송")
    void publishMessage_SerializationError() throws JsonProcessingException {
        Long chattingroomId = 1L;
        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(1L, "User", "Hello", chattingroomId);

        when(objectMapper.writeValueAsString(chattingMessageResponseDto))
                .thenThrow(new JsonProcessingException("Serialization error") {
                });

        CustomException customException = assertThrows(CustomException.class,
                () -> redisPublisher.publishMessage(chattingroomId, chattingMessageResponseDto));

        assertEquals(ErrorCode.REDIS_SERIALIZATION_ERROR, customException.getErrorCode());
        verify(pubSubRedisTemplate, times(1)).convertAndSend(anyString(), contains("메시지 전송에 실패하였습니다."));
    }
}
