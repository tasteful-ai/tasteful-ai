package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final RedisTemplate<String, Integer> aiRedisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REQUEST_COUNT_KEY_PREFIX = "ai_chat_request_count:";

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto) {
//        String userId = getCurrentUserId(); // 현재 사용자 Token 가져오는 로직 필요..

        // 요청 횟수 제한 확인
        String redisKey = REQUEST_COUNT_KEY_PREFIX; // +userId;
        ValueOperations<String, Integer> ops = aiRedisTemplate.opsForValue();
        Integer count = ops.get(redisKey);

        if (count == null) {
            ops.set(redisKey, 1, Duration.ofDays(1));

        } else if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);

        } else {
            ops.increment(redisKey);
        }

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

//    private String getCurrentUserId() {
        // 현재 로그인한 사용자의 ID를 가져오는 로직 구현
        // 예: SecurityContextHolder.getContext().getAuthentication().getName();
//        return "user123"; // 임시 값
//    }
}
