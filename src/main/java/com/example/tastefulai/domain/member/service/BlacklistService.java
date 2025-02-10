package com.example.tastefulai.domain.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlacklistService {

    @Qualifier("blacklistTemplate")
    private final RedisTemplate<String, String> blacklistTemplate;

    /**
     * Access Token 블랙리스트 등록
     */
    public void addToBlacklist(String token, long expiryMillis) {

        log.info("블랙리스트 등록: {} (만료시간: {} ms)", token, expiryMillis);
        blacklistTemplate.opsForValue().set(
                "blacklist:" + token, // Key: 토큰
                "logout", // Value: 로그아웃 표시
                expiryMillis, // TTL: 만료 시간
                TimeUnit.MILLISECONDS // 시간 단위
        );
    }

    /**
     * 블랙리스트 확인
     */
    public boolean isBlacklisted(String token) {
        String key = "blacklist:" + token;
        boolean exists = Boolean.TRUE.equals(blacklistTemplate.hasKey(key));
        log.info("블랙리스트 조회: {} → 존재 여부: {}", token, exists);

        return exists;
    }
}