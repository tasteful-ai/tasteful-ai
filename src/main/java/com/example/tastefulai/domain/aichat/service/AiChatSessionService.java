package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.repository.aiChatRedisRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AiChatSessionService {

    private final aiChatRedisRepository aiChatRedisRepository;

    // 세션 Id 가져오기 (없으면 생성)
    public String getSessionId(Long memberId) {

        String sessionId = aiChatRedisRepository.getSessionId(memberId);
        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            aiChatRedisRepository.saveSessionId(memberId, sessionId);
        }
        return sessionId;
    }

    // AI 추천 히스토리 Redis에 저장
    public void saveRecommendation(String sessionId, String recommendation) {
        aiChatRedisRepository.saveRecommendation(sessionId, recommendation);
    }

    // AI 채팅 히스토리 삭제 (세션 종료 시)
    public void clearChatHistory(Long memberId) {
        aiChatRedisRepository.deleteChatHistory(memberId);
    }
}
