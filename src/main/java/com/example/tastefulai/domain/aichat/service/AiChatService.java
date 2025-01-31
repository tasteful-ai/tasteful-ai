package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;

public interface AiChatService {

    // AI 추천 메뉴 생성
    AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId);

    // AI 채팅 히스토리 삭제 (세션 종료 시)
    void clearChatHistory(Long memberId);
}
