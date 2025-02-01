package com.example.tastefulai.domain.aichat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AiChatScheduler {

    private final AiChatCountService aiChatCountService;
    private final RedisTemplate<String, Integer> aiCountRedisTemplate;

    @Scheduled(cron = "0 0 0 * * ?")
    public void resetDailyRequestCounts() {
        Set<String> keys = aiCountRedisTemplate.keys("ai:chat:request:count:*");

        if (keys != null) {
            aiCountRedisTemplate.delete(keys);
        }
    }
}
