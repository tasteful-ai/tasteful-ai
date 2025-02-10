package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteDto;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

/**
 * AI 기반 메뉴 추천 서비스 구현체
 *
 * <p>회원의 음식 취향 데이터를 기반으로 AI에게 요청을 보내고, AI가 제공하는 추천 메뉴를 반환
 * 요청 횟수를 관리하고, AI 추천 기록을 저장하는 기능을 포함
 */
@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper;
    private final MemberService memberService;
    private final AiChatCountService aiChatCountService;
    private final AiChatHistoryService aiChatHistoryService;

    /**
     * AI를 이용하여 회원에게 맞는 메뉴를 추천
     *
     * <ul>
     *     <li>AI 추천 요청 횟수를 증가시키고 제한을 확인</li>
     *     <li>세션 ID를 가져와 AI 요청에 포함</li>
     *     <li>회원의 음식 취향 정보를 조회하여 프롬프트에 포함</li>
     *     <li>AI에게 요청을 보내고 JSON 응답을 처리함</li>
     *     <li>AI 추천 기록을 MySQL 및 Redis에 저장</li>
     * </ul>
     *
     * @param aiChatRequestDto AI 추천 요청 정보
     * @param memberId 추천을 요청한 회원의 ID
     * @return 추천된 메뉴와 설명을 포함하는 {@link AiChatResponseDto}
     * @throws CustomException JSON 파싱 오류, 응답 데이터 누락 등의 문제가 발생할 경우 예외 발생
     */
    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId) {

        aiChatCountService.incrementRequestCount(memberId);

        String sessionId = aiChatHistoryService.getSessionId(memberId);

        TasteDto tasteDto = memberService.getMemberTaste(memberId);

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
        String response = chatClient.prompt().user(prompt).call().content();

//        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: chatClient 로직으로 대체

        String recommendation;
        String description;

        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);

            recommendation = Optional.ofNullable(responseMap.get("recommendation"))
                    .orElseThrow(() -> new CustomException(ErrorCode.RECOMMENDATION_PARSING_ERROR));

            description = Optional.ofNullable(responseMap.get("description"))
                    .orElseThrow(() -> new CustomException(ErrorCode.DESCRIPTION_PARSING_ERROR));

        } catch (JsonProcessingException jsonProcessingException) {
            throw new CustomException(ErrorCode.JSON_PROCESSING_ERROR);
        }
        aiChatHistoryService.saveChatHistory(memberId, sessionId, recommendation, description);

        return new AiChatResponseDto(recommendation, description);
    }
}