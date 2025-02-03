package com.example.tastefulai.domain.aichat.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class AiChatRedisRepositoryImpl implements AiChatRedisRepository {

    private final RedisTemplate<String, Object> aiChatRedisTemplate;
    private static final String SESSION_KEY_PREFIX = "ai:chat:session:";
    private static final String RECOMMENDATION_LIST_KEY_PREFIX = "ai:chat:recommendations:";

    @Override
    public String getSessionId(Long memberId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();

        // 기존 세션 아이디 조회
        String existingSessionId = (String) sessionOps.get(sessionKey);
        if (existingSessionId != null) {
            return existingSessionId;
        }

        // 없으면 새 세션 아이디 생성 후 저장
        String newSessionId = UUID.randomUUID().toString();
        sessionOps.set(sessionKey, newSessionId);

        return newSessionId;
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
        String sessionId = (String) sessionOps.get(sessionKey);

        if (sessionId != null) {
            String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
            aiChatRedisTemplate.delete(List.of(sessionKey, historyKey));
        }
    }
}
