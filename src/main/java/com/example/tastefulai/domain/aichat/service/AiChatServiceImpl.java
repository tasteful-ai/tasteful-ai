package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.global.config.AiConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final AiConfig aiConfig;

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto) {

        // ChatClient를 사용해 AI 메시지 전달 및 응답 받기
        String response = chatClient.prompt()
                .user(aiChatRequestDto.message())
                .call()
                .content();

        // 응답에서 메뉴 추천 하나만 추출
        String singleRecommendation = parseSingleRecommendation(response);

        return new AiChatResponseDto(singleRecommendation.trim());
    }

    private String parseSingleRecommendation(String recommendation) {

        // 간단한 줄바꿈이나 마침표를 기준으로 첫 번째 메뉴만 반환
        if (recommendation == null || recommendation.isEmpty()) {

            return "추천할 메뉴가 없습니다.";
        }
        String[] parts = recommendation.split("[\n.,]]");

        return parts.length > 0 ? parts[0] : recommendation;
    }
}
