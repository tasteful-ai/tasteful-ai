package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final RedisTemplate<String, Integer> aiCountRedisTemplate;
    private final RedisTemplate<String, Object> aiChatRedisTemplate;
    private final ObjectMapper objectMapper;
    private final TasteGetService tasteGetService;

    private static final String REQUEST_COUNT_KEY_PREFIX = "ai:chat:request:count:";
    private static final String RECOMMENDATION_LIST_KEY_PREFIX = "ai:chat:recommendations:";
    private static final String SESSION_KEY_PREFIX = "ai:chat:session:";

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId) {

        // 세션 Id 생성
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();
        String sessionId = Optional.ofNullable((String) sessionOps.get(sessionKey))
                .orElseGet(() -> {
                    String newSessionId = UUID.randomUUID().toString();
                    sessionOps.set(sessionKey, newSessionId);
                    return newSessionId;
                });

        // 요청 횟수 제한 확인 (매일 자정 기준 초기화)
        String countKey = REQUEST_COUNT_KEY_PREFIX + memberId;
        ValueOperations<String, Integer> countOps = aiCountRedisTemplate.opsForValue();
        Integer count = Optional.ofNullable(countOps.get(countKey)).orElse(0);

        if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        // 자정까지 남은 시간 계산하여 TTL 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();

        countOps.increment(countKey); // 꼭 이거를 else 처리 해야하나?
        if (count == 0) {
            countOps.set(countKey, 1, secondsUntilMidnight, TimeUnit.SECONDS); // 자정 기준으로 초기화
        }

        // taste 정보 prompt로 넘겨주기
        TasteResponseDto tasteResponseDto = tasteGetService.getCompleteTaste(memberId);

        // AI 프롬프트 (취향 정보를 포함하여 AI에게 전달)
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

        // ChatClient를 사용해 AI에게 메시지 전달 및 응답 받음
         String response = chatClient.prompt().user(prompt).call().content();

        // AI 요청 (임시 Mock 데이터 사용)
//        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: chatClient 로직으로 대체

        // JSON 응답 파싱
        String recommendation;
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            recommendation = Optional.ofNullable(responseMap.get("recommendation"))
                    .orElse("추천할 메뉴가 없습니다.").trim();
        } catch (Exception exception) {
            recommendation = "추천할 메뉴를 파싱하는 데 실패했습니다.";
        }

        // AI 채팅 히스토리를 Redis에 저장 (세션 ID 기반)
        String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation);

        return new AiChatResponseDto(recommendation);
    }

    @Override
    public void clearChatHistory(Long memberId) {
        String sessionKey = SESSION_KEY_PREFIX + memberId;
        ValueOperations<String, Object> sessionOps = aiChatRedisTemplate.opsForValue();
        String sessionId = (String) sessionOps.get(sessionKey);

        if (sessionId != null) {
            String historyKey = RECOMMENDATION_LIST_KEY_PREFIX + sessionId;
            aiChatRedisTemplate.delete(historyKey);
            aiChatRedisTemplate.delete(sessionKey);
        }
    }
}