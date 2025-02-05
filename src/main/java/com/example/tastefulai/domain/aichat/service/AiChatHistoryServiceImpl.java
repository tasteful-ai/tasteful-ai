package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.entity.AiChatHistory;
import com.example.tastefulai.domain.aichat.repository.AiChatHistoryRepository;
import com.example.tastefulai.domain.aichat.repository.AiChatRedisRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AiChatHistoryServiceImpl implements AiChatHistoryService {

    private final AiChatHistoryRepository aiChatHistoryRepository;
    private final AiChatRedisRepository aiChatRedisRepository;
    private final MemberService memberService;
    private final TasteGetService tasteGetService;
    private final ObjectMapper objectMapper;

    @Qualifier("aiChatRedisTemplate")
    private final RedisTemplate<String, Object> aiChatRedisTemplate;

    private static final String HISTORY_KEY_PREFIX = "ai:chat:history:";

    // 세션 Id 가져오기 (없으면 생성)
    @Override
    public String getSessionId(Long memberId) {

        String sessionId = aiChatRedisRepository.getSessionId(memberId);

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            aiChatRedisRepository.saveSessionId(memberId, sessionId);
        }

        return sessionId;
    }

    // AI 추천 내역 MySQL에 저장 & Redis 캐싱
    @Override
    public void saveChatHistory(Long memberId, String sessionId, String recommendation) {

        Member member = memberService.findById(memberId);

        // Taste 정보 가져오기 & JSON 변환
        TasteResponseDto tasteResponseDto = tasteGetService.getCompleteTaste(memberId);
        String tasteData;

        try {
            tasteData = objectMapper.writeValueAsString(tasteResponseDto);
        } catch (JsonProcessingException exception) {
            tasteData = "{}";  // 변환 실패 시 빈 JSON 저장
        }

        // MySQL에 저장
        AiChatHistory aiChatHistory = new AiChatHistory(sessionId, recommendation, tasteData, member);
        aiChatHistoryRepository.save(aiChatHistory);

        // Redis에 캐싱 (세션 기준으로)
        String historyKey = HISTORY_KEY_PREFIX + memberId;
        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation);
        aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
    }

    // AI 추천 히스토리 조회 (Redis 캐싱 후 MySQL 조회)
    @Override
    public List<String> getChatHistory(Long memberId) {

        Member member = memberService.findById(memberId);
        String historyKey = HISTORY_KEY_PREFIX + memberId;

        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        List<Object> cachedHistory = listOps.range(historyKey, 0, -1);

        if (cachedHistory != null && !cachedHistory.isEmpty()) {
            return cachedHistory.stream().map(Object::toString).collect(Collectors.toList());
        }

        // Redis에 없으면 MySQL에서 가져와 Redis에 저장
        List<AiChatHistory> dbHistory = aiChatHistoryRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
        List<String> recommendations = dbHistory.stream()
                .map(AiChatHistory::getRecommendation)
                .collect(Collectors.toList());

        if (!recommendations.isEmpty()) {
            listOps.rightPushAll(historyKey, recommendations);
            aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
        }

        return recommendations;
    }

    // AI 추천 내역 삭제 (Redis & MySQL 동시 삭제)
    @Override
    public void clearChatHistory(Long memberId) {
        Member member = memberService.findById(memberId);

        // MySQL에서 AI 채팅 기록 삭제
        aiChatHistoryRepository.deleteByMember(memberId);

        // Redis에서도 해당 회원의 캐시 삭제
        String historyKey = HISTORY_KEY_PREFIX + memberId;
        aiChatRedisTemplate.delete(historyKey);
        aiChatRedisRepository.deleteChatHistory(memberId);
    }
}
