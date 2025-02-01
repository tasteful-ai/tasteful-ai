package com.example.tastefulai.domain.aichat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiChatSessionService {

    private final RedisTemplate<String, Object> aiChatRedisTemplate;
    private static final String SESSION_KEY_PREFIX = "ai:chat:session:";
    private static final String RECOMMENDATION_LIST_KEY_PREFIX = "ai:chat:recommendations:";

    // 세션 Id 가져오기 (없으면 생성)
    public String getSessionId(Long memberId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();

        return Optional.ofNullable((String) sessionOps.get(sessionKey))
                .orElseGet(() -> {
                    String newSessionId = UUID.randomUUID().toString();
                    sessionOps.set(sessionKey, newSessionId);
                    return newSessionId;
                });
    }

    // AI 추천 히스토리 Redis에 저장
    public void saveRecommendation(String sessionId, String recommendation) {
        String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation);
    }

    // AI 채팅 히스토리 삭제 (세션 종료 시)
    public void clearChatHistory(Long memberId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();
        String sessionId = (String) sessionOps.get(sessionKey);

        if (sessionId != null) {
            String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
            aiChatRedisTemplate.delete(historyKey);
            aiChatRedisTemplate.delete(sessionKey);
        }
    }
}
