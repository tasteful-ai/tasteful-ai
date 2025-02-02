package com.example.tastefulai.domain.aichat.repository;

public interface aiChatRedisRepository {

    String getSessionId(Long memberId);
    void saveSessionId(Long memberId, String sessionId);
    void saveRecommendation(String sessionId, String recommendation);
    void deleteChatHistory(Long memberId);
}
