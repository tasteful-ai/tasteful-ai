package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final TasteGetService tasteGetService;
    private final AiChatCountService aiChatCountService;
    private final AiChatSessionService aiChatSessionService;

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId) {

        // 요청 횟수 증가 (제한 초과 시 예외)
        aiChatCountService.incrementRequestCount(memberId);

        // 세션 Id 가져오기
        String sessionId = aiChatSessionService.getSessionId(memberId);

        // taste 정보 가져오기
        TasteResponseDto tasteResponseDto = tasteGetService.getCompleteTaste(memberId);

        // AI 프롬프트 생성 (취향 정보를 포함하여 AI에게 전달)
        String prompt = String.format(
                "내 취향은 다음과 같다." +
                        "장르: %s, 좋아하는 음식: %s, 식단 성향: %s, 매운 정도: %s" +
                        "이 정보를 고려해서 오늘 점심 메뉴 추천해줘." +
                        "응답은 반드시 JSON 형식으로, {\"recommendation\": \"메뉴 이름\"} 으로 해줘.",
                tasteResponseDto.getGenres(),
                tasteResponseDto.getLikeFoods(),
                tasteResponseDto.getDislikeFoods(),
                tasteResponseDto.getDietaryPreferences(),
                tasteResponseDto.getSpicyLevel()
        );

        // ChatClient를 사용해 AI에게 요청
//        String response = chatClient.prompt().user(prompt).call().content();

        // AI 요청 (임시 Mock 데이터 사용)
        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: chatClient 로직으로 대체

        // JSON 응답 파싱
        String recommendation;
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            recommendation = Optional.ofNullable(responseMap.get("recommendation"))
                    .orElse("추천할 메뉴가 없습니다.").trim();
        } catch (Exception exception) {
            recommendation = "추천할 메뉴를 파싱하는 데 실패했습니다.";
        }

        // AI 추천 히스토리 저장
        aiChatSessionService.saveRecommendation(sessionId, recommendation);

        return new AiChatResponseDto(recommendation);
    }

    @Override
    public void clearChatHistory(Long memberId) {
        aiChatSessionService.clearChatHistory(memberId);
    }
}