package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.RedisKeyUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiChatCountServiceImpl implements AiChatCountService {

    @Qualifier("aiCountRedisTemplate")
    private final RedisTemplate<String, Integer> aiCountRedisTemplate;

    // 요청 횟수 증가 및 자정까지 TTL 설정
    @Override
    public void incrementRequestCount(Long memberId) {
        String countKey = RedisKeyUtil.getRequestCountKey(memberId);
        ValueOperations<String, Integer> countOps = aiCountRedisTemplate.opsForValue();
        Integer count = Optional.ofNullable(countOps.get(countKey)).orElse(0);

        if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        countOps.increment(countKey);
        log.info("AI 채팅 요청 횟수 증가 - 회원 ID: {}, 현재 횟수: {}", memberId, count + 1);


        // 첫 번째 요청인 경우 자정까지 TTL 설정
        if (count == 0) {
            long secondsUntilMidnight = getSecondsUntilMidnight();
            countOps.set(countKey, 1, secondsUntilMidnight, TimeUnit.SECONDS);
            log.info("AI 채팅 요청 횟수 TTL 설정 완료 - 회원 ID: {}, 남은 시간(초): {}", memberId, secondsUntilMidnight);
        }
    }

    // 현재 시간 기준으로 자정까지 남은 초 계산
    private long getSecondsUntilMidnight() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        return Duration.between(now, midnight).getSeconds();
    }
}
