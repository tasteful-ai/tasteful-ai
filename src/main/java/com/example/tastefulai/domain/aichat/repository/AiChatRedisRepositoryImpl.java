package com.example.tastefulai.domain.aichat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AiChatRedisRepositoryImpl implements aiChatRedisRepository {

    private final RedisTemplate<String, Object> aiChatRedisTemplate;
    private static final String SESSION_KEY_PREFIX = "ai:chat:session:";
    private static final String RECOMMENDATION_LIST_KEY_PREFIX = "ai:chat:recommendations:";

    @Override
    public String getSessionId(Long memberId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();

        return (String) sessionOps.get(sessionKey);
    }

    @Override
    public void saveSessionId(Long memberId, String sessionId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();
        sessionOps.set(sessionKey, sessionId);
    }

    @Override
    public void saveRecommendation(String sessionId, String recommendation) {
        String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation);
    }

    @Override
    public void deleteChatHistory(Long memberId) {
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
