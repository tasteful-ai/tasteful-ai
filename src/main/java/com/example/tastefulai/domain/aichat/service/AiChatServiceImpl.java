package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto) {

        // 프롬프트 사용
        String prompt = "오늘 점심메뉴를 하나만 추천해. 응답은 반드시 JSON 형식으로, 큰따옴표만 사용해서. {\"recommendation\": \"메뉴 이름\"}";

        // ChatClient를 사용해 AI에게 메시지 전달 및 응답 받음
        String response = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        // JSON 응답 파싱
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);

            String recommendation = responseMap.get("recommendation");

            if (recommendation == null || recommendation.isEmpty()) {

                return new AiChatResponseDto("추천할 메뉴가 없습니다.");
            }

            return new AiChatResponseDto(recommendation.trim());
        } catch (Exception exception) {

            // 파싱 오류 시 기본 메시지 반환
            return new AiChatResponseDto("추천할 메뉴를 파싱하는 데 실패했습니다.");
        }
    }
}
