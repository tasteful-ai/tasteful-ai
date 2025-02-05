package com.example.tastefulai.domain.aichat.service;

import java.util.List;

public interface AiChatHistoryService {

    // 세션 ID 가져오기 (없으면 생성)
    String getSessionId(Long memberId);

    // AI 추천 내역 MySQL 저장 & Redis 캐싱
    void saveChatHistory(Long memberId, String sessionId, String recommendation);

    // AI 추천 히스토리 조회 (Redis 캐싱 후 MySQL 조회)
    List<String> getChatHistory(Long memberId);

    // AI 추천 내역 삭제 (Redis & MySQL 동시 삭제)
    void clearChatHistory(Long memberId);
}
