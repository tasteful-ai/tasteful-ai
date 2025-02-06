package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;

public interface AiChatService {

    // AI 추천 메뉴 생성
    AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId);
}
