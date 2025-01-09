package com.example.tastefulai.global.common.dto;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final RedisTemplate<String, String> redisTemplate;

    // Refresh-Token 저장
    public void storeRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(email, refreshToken, 7, TimeUnit.DAYS);
    }

    // Refresh-Token 조회
    public String getRefreshToken(String email) {
        return redisTemplate.opsForValue().get(email);
    }
}
