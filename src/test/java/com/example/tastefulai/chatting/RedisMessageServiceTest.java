package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.service.RedisMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RedisMessageServiceTest {

    @InjectMocks
    private RedisMessageService redisMessageService;

    @Mock
    private RedisTemplate<String, Object> messageCacheRedisTemplate;

    @Mock
    private ListOperations<String, Object> listOperations;

    @Mock
    private ObjectMapper objectMapper;

    private static final String REDIS_KEY = "chat:messages:1";
    private static final ChattingMessageResponseDto TEST_MESSAGE = new ChattingMessageResponseDto(1L, "User", "Hello", 1L);

    private static final String SERIALIZED_MESSAGE = "{\"senderId\":1,\"senderNickname\":\"User\",\"message\":\"Hello\",\"chattingroomId\":1}";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(messageCacheRedisTemplate.opsForList()).thenReturn(listOperations);
    }

    @Test
    @DisplayName("메시지 저장 성공")
    void saveMessage_Success() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(TEST_MESSAGE)).thenReturn(SERIALIZED_MESSAGE);

        redisMessageService.saveMessage(1L, TEST_MESSAGE);

        verify(listOperations, times(1)).rightPush(REDIS_KEY, SERIALIZED_MESSAGE);
        verify(listOperations, times(1)).trim(REDIS_KEY, -50, -1);
    }

    @Test
    @DisplayName("메시지 저장 실패 - JSON 직렬화 오류")
    void saveMessage_JsonProcessingException() throws JsonProcessingException {
        when(objectMapper.writeValueAsString(TEST_MESSAGE)).thenThrow(new JsonProcessingException("직렬화 오류") {
        });

        redisMessageService.saveMessage(1L, TEST_MESSAGE);

        verify(listOperations, never()).rightPush(anyString(), any());
    }

    @Test
    @DisplayName("최근 메시지 조회 - Redis 캐시에 저장된 메시지가 있는 경우")
    void getRecentMessages_CacheExists() throws JsonProcessingException {
        when(listOperations.range(REDIS_KEY, 0, -1)).thenReturn(List.of(SERIALIZED_MESSAGE));
        when(objectMapper.readValue(SERIALIZED_MESSAGE, ChattingMessageResponseDto.class)).thenReturn(TEST_MESSAGE);

        List<ChattingMessageResponseDto> messages = redisMessageService.getRecentMessages(1L);

        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getMessage());
    }

    @Test
    @DisplayName("최근 메시지 조회 - Redis 캐시에 저장된 메시지가 없는 경우")
    void getRecentMessages_NoCache() {
        when(listOperations.range(REDIS_KEY, 0, -1)).thenReturn(List.of());

        List<ChattingMessageResponseDto> messages = redisMessageService.getRecentMessages(1L);

        assertTrue(messages.isEmpty());
    }

    @Test
    @DisplayName("최근 메시지 조회 실패")
    void getRecentMessages_JsonProcessingException() throws JsonProcessingException {
        when(listOperations.range(REDIS_KEY, 0, -1)).thenReturn(List.of(SERIALIZED_MESSAGE));
        when(objectMapper.readValue(SERIALIZED_MESSAGE, ChattingMessageResponseDto.class)).thenThrow(new JsonProcessingException("역직렬화 오류") {
        });

        List<ChattingMessageResponseDto> messages = redisMessageService.getRecentMessages(1L);

        assertTrue(messages.isEmpty());
    }
}
