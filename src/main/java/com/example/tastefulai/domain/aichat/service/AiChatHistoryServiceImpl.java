package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.entity.AiChatHistory;
import com.example.tastefulai.domain.aichat.repository.AiChatHistoryRepository;
import com.example.tastefulai.domain.aichat.repository.AiChatRedisRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.example.tastefulai.global.util.RedisKeyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiChatHistoryServiceImpl implements AiChatHistoryService {

    private final AiChatHistoryRepository aiChatHistoryRepository;
    private final AiChatRedisRepository aiChatRedisRepository;
    private final MemberService memberService;
    private final TasteGetService tasteGetService;
    private final ObjectMapper objectMapper;

    @Qualifier("aiChatRedisTemplate")
    private final RedisTemplate<String, Object> aiChatRedisTemplate;

    // 세션 Id 가져오기 (없으면 생성)
    @Override
    public String getSessionId(Long memberId) {

        String sessionId = aiChatRedisRepository.getSessionId(memberId);

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            aiChatRedisRepository.saveSessionId(memberId, sessionId);
            log.info("새로운 AI 채팅 세션 ID 생성 - 회원 ID: {}, 세션 ID: {}", memberId, sessionId);
        }

        return sessionId;
    }

    // AI 추천 내역 MySQL에 저장 & Redis 캐싱
    @Override
    public void saveChatHistory(Long memberId, String sessionId, String recommendation) {

        validateMemberId(memberId);
        validateSessionId(sessionId);

        Member member = memberService.findById(memberId);

        // Taste 정보 가져오기 & JSON 변환
        TasteResponseDto tasteResponseDto = tasteGetService.getCompleteTaste(memberId);
        String tasteData;

        try {
            tasteData = objectMapper.writeValueAsString(tasteResponseDto);
        } catch (JsonProcessingException exception) {
            log.error("취향 정보 직렬화 실패 - 회원 ID: {}, 오류 메시지: {}", memberId, exception.getMessage());
            tasteData = "{}";
        }

        // MySQL에 저장
        AiChatHistory aiChatHistory = new AiChatHistory(sessionId, recommendation, tasteData, member);
        aiChatHistoryRepository.save(aiChatHistory);
        log.info("AI 채팅 히스토리 저장 완료 - 회원 ID: {}, 세션 ID: {}", memberId, sessionId);

        // Redis에 캐싱 (세션 기준으로)
        String historyKey = RedisKeyUtil.getHistoryKey(memberId);
        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation);
        aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
        log.info("AI 추천 결과 Redis 캐싱 완료 - 회원 ID: {}, 추천 메뉴: {}", memberId, recommendation);
    }

    // AI 추천 히스토리 조회 (Redis 캐싱 후 MySQL 조회)
    @Override
    public List<String> getChatHistory(Long memberId) {

        Member member = memberService.findById(memberId);
        String historyKey = RedisKeyUtil.getHistoryKey(memberId);

        ListOperations<String, Object> listOps = aiChatRedisTemplate.opsForList();
        List<Object> cachedHistory = listOps.range(historyKey, 0, -1);

        if (cachedHistory != null && !cachedHistory.isEmpty()) {
            aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
            log.info("Redis에서 AI 채팅 히스토리 반환 - 회원 ID: {}", memberId);
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
            log.info("MySQL에서 가져와 Redis에 AI 채팅 히스토리 캐싱 완료 - 회원 ID: {}", memberId);
        }

        return recommendations;
    }

    // AI 추천 내역 삭제 (Redis & MySQL 동시 삭제)
    @Override
    public void clearChatHistory(Long memberId) {
        Member member = memberService.findById(memberId);

        // MySQL에서 AI 채팅 기록 삭제
        aiChatHistoryRepository.deleteByMember(member);
        log.info("MySQL에서 AI 채팅 히스토리 삭제 완료 - 회원 ID: {}", memberId);

        // Redis에서도 해당 회원의 캐시 삭제
        String historyKey = RedisKeyUtil.getHistoryKey(memberId);
        aiChatRedisTemplate.delete(historyKey);
        aiChatRedisRepository.deleteChatHistory(memberId);
        log.info("Redis에서 AI 채팅 히스토리 삭제 완료 - 회원 ID: {}", memberId);
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("잘못된 memberId 값: " + memberId);
        }
    }

    private void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("잘못된 sessionId 값: " + sessionId);
        }
    }
}
