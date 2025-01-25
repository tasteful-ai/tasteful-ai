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

import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AiChatServiceImpl implements AiChatService {

    private final ChatClient chatClient;
    private final RedisTemplate<String, Integer> aiRedisTemplate;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String REQUEST_COUNT_KEY_PREFIX = "ai:chat:request:count:";
    private static final String RECOMMENDATION_LIST_KEY_PREFIX = "ai:chat:recommendations:";

    @Override
    public AiChatResponseDto createMenuRecommendation(AiChatRequestDto aiChatRequestDto, Long memberId) {

        //  tasteRepository.findByMemberId(memberId);
        //  이 taste 정보 prompt로 넘겨주기

        // 요청 횟수 제한 확인
        String redisKey = REQUEST_COUNT_KEY_PREFIX + memberId;
        ValueOperations<String, Integer> ops = aiRedisTemplate.opsForValue();
        Integer count = ops.get(redisKey);

        if (count == null) {
            ops.set(redisKey, 1, 1, TimeUnit.DAYS);
        } else if (count >= 10) {
            throw new CustomException(ErrorCode.TOO_MANY_REQUESTS);

        } else {
            ops.increment(redisKey);
        }

        // 프롬프트 사용
        String prompt = "오늘 점심메뉴를 하나만 추천해. 응답은 반드시 JSON 형식으로, {\"recommendation\": \"메뉴 이름\"}";

        // ChatClient를 사용해 AI에게 메시지 전달 및 응답 받음
//        String response = chatClient.prompt()
//                .user(prompt)
//                .call()
//                .content();

        String response = "{\"recommendation\": \"김치찌개\"}";   // TODO: 나중에 ai 요청 로직으로 변경

        // JSON 응답 파싱
        String recommendation;
        try {
            Map<String, String> responseMap = objectMapper.readValue(response, Map.class);
            recommendation = responseMap.get("recommendation");

            if (recommendation == null || recommendation.isEmpty()) {
                recommendation = "추천할 메뉴가 없습니다.";

            } else {
                recommendation = recommendation.trim();
            }
        } catch (Exception exception) {
            recommendation = "추천할 메뉴를 파싱하는 데 실패했습니다.";
        }

        // 추천 메뉴 저장
        String recommendationKey = RECOMMENDATION_LIST_KEY_PREFIX + memberId;
        ListOperations<String, Object> listOps = redisTemplate.opsForList();
        listOps.rightPush(recommendationKey, recommendation);

        return new AiChatResponseDto(recommendation);
    }
}