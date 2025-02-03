package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AiChatCountService {

    @Qualifier("aiCountRedisTemplate")
    private final RedisTemplate<String, Integer> aiCountRedisTemplate;

    private static final String REQUEST_COUNT_KEY_PREFIX = "ai:chat:request:count:";

    // 요청 횟수 증가 및 자정까지 TTL 설정
    public void incrementRequestCount(Long memberId) {
        String countKey = REQUEST_COUNT_KEY_PREFIX + memberId;
        ValueOperations<String, Integer> countOps = aiCountRedisTemplate.opsForValue();
        Integer count = Optional.ofNullable(countOps.get(countKey)).orElse(0);

        if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        countOps.increment(countKey);

        // 첫 번째 요청인 경우 자정까지 TTL 설정
        if (count == 0) {
            long secondsUntilMidnight = getSecondsUntilMidnight();
            countOps.set(countKey, 1, secondsUntilMidnight, TimeUnit.SECONDS);
        }
    }

    // 현재 시간 기준으로 자정까지 남은 초 계산
    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, midnight).getSeconds();
    }
}
