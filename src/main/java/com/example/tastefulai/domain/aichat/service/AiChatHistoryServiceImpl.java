package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.domain.aichat.entity.AiChatHistory;
import com.example.tastefulai.domain.aichat.repository.AiChatHistoryRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteGetService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.RedisKeyUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * AI 채팅 히스토리를 관리하는 서비스 구현체
 *
 * <p>회원의 AI 추천 내역을 MySQL과 Redis에 저장 및 조회하며, 세션 ID를 관리
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class AiChatHistoryServiceImpl implements AiChatHistoryService {

    private final AiChatHistoryRepository aiChatHistoryRepository;
    private final MemberService memberService;
    private final TasteGetService tasteGetService;
    private final ObjectMapper objectMapper;

    @Qualifier("aiChatRedisTemplate")
    private final RedisTemplate<String, String> aiChatRedisTemplate;

    @Override
    public String getSessionId(Long memberId) {

        String sessionKey = RedisKeyUtil.getSessionKey(memberId);
        ValueOperations<String, String> sessionOps = aiChatRedisTemplate.opsForValue();
        String sessionId = (String) sessionOps.get(sessionKey);

        if (sessionId == null) {
            sessionId = UUID.randomUUID().toString();
            sessionOps.set(sessionKey, sessionId, 1, TimeUnit.DAYS);
            log.info("새로운 AI 채팅 세션 ID 생성 - 회원 ID: {}, 세션 ID: {}", memberId, sessionId);
        }

        return sessionId;
    }

    @Override
    @Transactional
    public void saveChatHistory(Long memberId, String sessionId, String recommendation, String description) {

        validateMemberId(memberId);
        validateSessionId(sessionId);

        Member member = memberService.findById(memberId);
        String tasteData = serializeTasteData(memberId);

        AiChatHistory aiChatHistory = new AiChatHistory(sessionId, recommendation, description, tasteData, member);
        aiChatHistoryRepository.save(aiChatHistory);
        log.debug("MySQL AI 채팅 히스토리 저장 완료 - 회원 ID: {}, 세션 ID: {}", memberId, sessionId);

        cacheChatHistory(sessionId, recommendation, description);
    }

    // Redis에 AI 추천 내역 저장
    private void cacheChatHistory(String sessionId, String recommendation, String description) {

        String historyKey = RedisKeyUtil.getHistoryKey(sessionId);
        ListOperations<String, String> listOps = aiChatRedisTemplate.opsForList();
        listOps.rightPush(historyKey, recommendation + " - " + description);

        aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
        log.debug("Redis AI 추천 결과 캐싱 완료 - 세션 ID: {}, 추천 메뉴: {}, 설명: {}", sessionId, recommendation, description);
    }

    /**
     * 회원의 AI 채팅 히스토리를 조회
     * <p>Redis에 데이터가 있는 경우 즉시 반환하며, 없을 경우 MySQL에서 조회 후 Redis에 저장
     *
     * @param memberId 회원의 ID
     * @return AI 추천 내역 목록
     */
    @Override
    public List<String> getChatHistory(Long memberId) {

        String sessionId = getSessionId(memberId);
        String historyKey = RedisKeyUtil.getHistoryKey(sessionId);
        ListOperations<String, String> listOps = aiChatRedisTemplate.opsForList();

        List<String> cachedHistory = listOps.range(historyKey, 0, -1);
        if (cachedHistory != null && !cachedHistory.isEmpty()) {
            log.debug("Redis에서 AI 채팅 히스토리 반환 - 회원 ID: {}", memberId);

            return cachedHistory.stream().map(Object::toString).collect(Collectors.toList());
        }

        return fetchAndCacheHistoryFromDB(memberId, sessionId, historyKey);
    }

    /**
     * MySQL에서 AI 추천 내역을 가져온 후 Redis에 저장
     *
     * @param memberId 회원의 ID
     * @param sessionId AI 채팅 세션 ID
     * @param historyKey Redis 키
     * @return AI 추천 내역 목록
     */
    private List<String> fetchAndCacheHistoryFromDB(Long memberId, String sessionId, String historyKey) {

        List<AiChatHistory> dbHistory = aiChatHistoryRepository.findByMemberIdOrderByCreatedAtDesc(memberId);
        List<String> recommendations = dbHistory.stream()
                .map(h -> h.getRecommendation() + " - " + h.getDescription())
                .collect(Collectors.toList());

        if (!recommendations.isEmpty()) {
            ListOperations<String, String> listOps = aiChatRedisTemplate.opsForList();
            listOps.rightPushAll(historyKey, recommendations);

            aiChatRedisTemplate.expire(historyKey, 1, TimeUnit.DAYS);
            log.debug("MySQL에서 가져와 Redis에 AI 채팅 히스토리 캐싱 완료 - 회원 ID: {}", memberId);
        }

        return recommendations;
    }

    @Override
    public void clearChatHistory(Long memberId) {
        Member member = memberService.findById(memberId);

        aiChatHistoryRepository.deleteByMember(member);
        log.info("MySQL에서 AI 채팅 히스토리 삭제 완료 - 회원 ID: {}", memberId);

        String sessionId = getSessionId(memberId);
        String historyKey = RedisKeyUtil.getHistoryKey(sessionId);
        aiChatRedisTemplate.delete(List.of(historyKey, RedisKeyUtil.getSessionKey(memberId)));
        log.info("Redis에서 AI 채팅 히스토리 삭제 완료 - 회원 ID: {}", memberId);
    }

    private void validateMemberId(Long memberId) {
        if (memberId == null || memberId <= 0) {
            log.warn("잘못된 memberId 값 감지 - 입력 값: {}", memberId);
            throw new CustomException(ErrorCode.INVALID_REQUEST, "잘못된 memberId 값: " + memberId);
        }
        log.debug("유효한 memberId 검증 완료 - memberId: {}", memberId);
    }

    private void validateSessionId(String sessionId) {
        if (sessionId == null || sessionId.trim().isEmpty()) {
            log.warn("잘못된 sessionId 값 감지 - 입력 값: {}", sessionId);
            throw new CustomException(ErrorCode.INVALID_REQUEST, "잘못된 sessionId 값: " + sessionId);
        }
        log.debug("유효한 sessionId 검증 완료 - sessionId: {}", sessionId);
    }

    private String serializeTasteData(Long memberId) {
        try {
            TasteResponseDto tasteResponseDto = tasteGetService.getCompleteTaste(memberId);
            return objectMapper.writeValueAsString(tasteResponseDto);
        } catch (JsonProcessingException exception) {
            log.error("취향 정보 직렬화 실패 - 회원 ID: {}, 오류 메시지: {}", memberId, exception.getMessage());
            return "{}";
        }
    }
}