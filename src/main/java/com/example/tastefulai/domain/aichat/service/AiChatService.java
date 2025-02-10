package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;

/**
 * AI 기반 메뉴 추천 서비스 인터페이스.
 *
 * <p>회원의 음식 취향 정보를 기반으로 AI가 메뉴를 추천하는 기능을 제공합니다.
 */
public interface AiChatService {

    /**
     * AI를 이용하여 메뉴를 추천합니다.
     *
     * @param aiChatRequestDto AI 추천 요청 정보
     * @param memberId 추천을 요청한 회원의 ID
     * @return 추천된 메뉴 및 설명을 포함하는 {@link AiChatResponseDto}
     */
    AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId);
}
