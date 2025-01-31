package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
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

        //  tasteRepository.findByMemberId(memberId);
        //  이 taste 정보 prompt로 넘겨주기

        // 요청 횟수 제한 확인 (매일 자정 기준 초기화)
        String redisKey = REQUEST_COUNT_KEY_PREFIX + memberId;
        ValueOperations<String, Integer> ops = aiCountRedisTemplate.opsForValue();
        Integer count = Optional.ofNullable(ops.get(redisKey)).orElse(0);

        if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);
        }

        // 자정까지 남은 시간 계산하여 TTL 설정
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime midnight = now.toLocalDate().plusDays(1).atStartOfDay();
        long secondsUntilMidnight = Duration.between(now, midnight).getSeconds();

        ops.increment(redisKey);
        if (count == 0) {
            ops.set(redisKey, 1, secondsUntilMidnight, TimeUnit.SECONDS); // 자정 기준으로 초기화
        }

        // AI 프롬프트 사용
        String prompt = "오늘 점심메뉴를 하나만 추천해. 응답은 반드시 JSON 형식으로, {\"recommendation\": \"메뉴 이름\"}";

        // ChatClient를 사용해 AI에게 메시지 전달 및 응답 받음
//        String response = chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();

        // AI 요청 (임시 Mock 데이터 사용)
        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: 나중에 ai 요청 로직으로 변경

        // JSON 응답 파싱
        String recommendation;
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            recommendation = responseMap.getOrDefault("recommendation", "추천할 메뉴가 없습니다.").trim();
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