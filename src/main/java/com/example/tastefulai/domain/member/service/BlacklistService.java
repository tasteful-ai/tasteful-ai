package com.example.tastefulai.domain.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class BlacklistService {

    private final RedisTemplate<String, String> blacklistTemplate;

    // Access Token 블랙리스트 등록
    public void addToBlacklist(String token, long expiryMillis) {
        // Redis에 토큰을 블랙리스트로 저장
        blacklistTemplate.opsForValue().set(
                "blacklist:" + token, // Key: 토큰
                "logout", // Value: 로그아웃 표시
                expiryMillis, // TTL: 만료 시간
                TimeUnit.MILLISECONDS // 시간 단위
        );
    }

    // 블랙리스트 확인
    public boolean isBlacklisted(String token) {
        // Redis에서 토큰 검증
        return blacklistTemplate.hasKey("blacklist:" + token);
    }
}