package com.example.tastefulai.domain.aichat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.UUID;

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

        // 새로운 세션 Id 생성
        String sessionId = UUID.randomUUID().toString();

        // sessionKey가 없을 경우 새로운 sessionId를 저장 (한 번의 Redis 요청)
        String existingSessionId = (String) sessionOps.getAndSet(sessionKey, sessionId);

        return existingSessionId != null ? existingSessionId : sessionId;
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

        // 기존 sessionId 조회 및 삭제를 한 번의 Redis 호출로 처리
        String sessionId = (String) sessionOps.getAndDelete(sessionKey);

        if (sessionId != null) {
            String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
            aiChatRedisTemplate.delete(historyKey);
        }
    }
}
