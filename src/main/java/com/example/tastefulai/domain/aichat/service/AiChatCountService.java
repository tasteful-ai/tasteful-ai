package com.example.tastefulai.domain.aichat.service;

import com.example.tastefulai.global.error.exception.CustomException;

/**
 * AI 채팅 요청 횟수를 관리하는 서비스 인터페이스
 *
 * <p>회원별 요청 횟수를 증가시키고, 제한을 초과하는 경우 예외 발생
 */
public interface AiChatCountService {

    /**
     * 회원의 AI 채팅 요청 횟수를 증가시킴
     * <p>최대 10회까지 요청할 수 있으며, 초과 시 예외 발생
     * <p>첫 요청 시에는 자정까지 TTL(Time-To-Live) 설정
     *
     * @param memberId 요청을 보낸 회원의 ID
     * @throws CustomException 요청 횟수가 초과된 경우 예외 발생
     */
    void incrementRequestCount(Long memberId);
}
