package com.example.tastefulai.member;

import com.example.tastefulai.domain.member.service.BlacklistService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.*;

class BlacklistServiceTest {

    private BlacklistService blacklistService;

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        blacklistService = new BlacklistService(redisTemplate);
    }

    @Test
    @DisplayName("토큰이 Redis에 블랙리스트로 저장되는지 확인")
    void addToBlacklist_ShouldStoreTokenInRedis() {
        // Arrange
        String token = "test-token";
        long ttlMillis = 60000; // 1 minute

        when(redisTemplate.opsForValue()).thenReturn(valueOperations);

        // Act
        blacklistService.addToBlacklist(token, ttlMillis);

        // Assert
        verify(redisTemplate.opsForValue(), times(1))
                .set("blacklist:" + token, "invalid", ttlMillis, TimeUnit.MILLISECONDS);
    }
}