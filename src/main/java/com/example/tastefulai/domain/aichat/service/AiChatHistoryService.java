package com.example.tastefulai.domain.aichat.service;

import java.util.List;

/**
 * AI 채팅 히스토리를 관리하는 서비스 인터페이스
 *
 * <p>세션 ID 생성, AI 추천 내역 저장 및 조회, 데이터 삭제 등의 기능을 제공함
 */
public interface AiChatHistoryService {

    /**
     * 회원의 세션 ID를 가져오고, 세션이 존재하지 않는 경우 새로 생성
     *
     * @param memberId 회원의 ID
     * @return 세션 ID
     */
    String getSessionId(Long memberId);


    /**
     * AI 추천 내역을 MySQL에 저장하고 Redis에 캐싱
     *
     * @param memberId 회원의 ID
     * @param sessionId AI 채팅 세션 ID
     * @param recommendation AI가 추천한 메뉴
     * @param description 추천 메뉴의 설명
     */
    void saveChatHistory(Long memberId, String sessionId, String recommendation, String description);

    /**
     * 회원의 AI 채팅 히스토리를 조회
     * <p>먼저 Redis에서 조회하며, 데이터가 없을 경우 MySQL에서 가져와 Redis에 저장한 후 반환
     *
     * @param memberId 회원의 ID
     * @return AI 추천 내역 목록
     */
    List<String> getChatHistory(Long memberId);

    /**
     * 회원의 AI 채팅 히스토리를 삭제
     * <p>MySQL과 Redis에서 동시에 삭제
     *
     * @param memberId 회원의 ID
     */
    void clearChatHistory(Long memberId);
}
