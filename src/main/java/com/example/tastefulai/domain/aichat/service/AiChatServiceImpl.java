package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteDto;
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
    private final MemberService memberService;
    private final AiChatCountService aiChatCountService;
    private final AiChatHistoryService aiChatHistoryService;

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId) {

        // 요청 횟수 증가 (제한 초과 시 예외)
        aiChatCountService.incrementRequestCount(memberId);

        // 세션 Id 가져오기
        String sessionId = aiChatHistoryService.getSessionId(memberId);

        // taste 정보 가져오기
        TasteDto tasteDto = memberService.getMemberTaste(memberId);

        // AI 프롬프트 생성 (취향 정보를 포함하여 AI에게 전달)
        String prompt = String.format(
                "나는 다음과 같은 음식 취향을 가지고 있어. 참고해줘." +
                        "선호하는 장르: %s, 좋아하는 음식: %s, 선호하지 않는 음식: %s, 식단 성향: %s, 매운 음식 가능정도: %s." +
                        "하지만 항상 같은 취향의 메뉴만 추천하지 말고, 가끔은 새로운 메뉴도 추천해줘." +
                        "내가 한 번도 먹어보지 못했을 법한 흥미로운 메뉴도 제안해줘." +
                        "응답 형식은 반드시 JSON 형식으로 제공해야 해." +
                        "예시: {\"recommendation\": \"메뉴 이름\", \"description\": \"간단한 메뉴 설명\"}.",
                tasteDto.getGenres(),
                tasteDto.getLikeFoods(),
                tasteDto.getDislikeFoods(),
                tasteDto.getDietaryPreferences(),
                tasteDto.getSpicyLevel()
        );

        // ChatClient를 사용해 AI에게 요청
        String response = chatClient.prompt().user(prompt).call().content();

        // AI 요청 (임시 Mock 데이터 사용)
//        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: chatClient 로직으로 대체

        // JSON 응답 파싱
        String recommendation;
        String description;
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            recommendation = Optional.ofNullable(responseMap.get("recommendation"))
                    .orElse("추천할 메뉴가 없습니다.").trim();
            description = Optional.ofNullable(responseMap.get("description"))
                    .orElse("설명이 제공되지 않았습니다.").trim();
        } catch (Exception exception) {
            recommendation = "추천할 메뉴를 파싱하는 데 실패했습니다.";
            description = "설명을 파싱하는데 실패했습니다.";
        }

        // AI 추천 히스토리 MySQL + Redis에 저장
        aiChatHistoryService.saveChatHistory(memberId, sessionId, recommendation, description);

        return new AiChatResponseDto(recommendation, description);
    }
}