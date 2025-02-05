package com.example.tastefulai.global.util;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedisKeyUtil {

    private static final String SESSION_KEY_PREFIX = "ai:chat:session:";
    private static final String HISTORY_KEY_PREFIX = "ai:chat:history:";
    private static final String REQUEST_COUNT_KEY_PREFIX = "ai:chat:request:count:";

    public static String getSessionKey(Long memberId) {

        validateMemberId(memberId);
        String key = SESSION_KEY_PREFIX + memberId;
        log.info("Redis 세션 키 생성: {}", key);

        return key;
    }

    public static String getHistoryKey(String sessionId) {

        if (sessionId == null || sessionId.trim().isEmpty()) {
            throw new IllegalArgumentException("잘못된 sessionId 값: " + sessionId);
        }
        String key = HISTORY_KEY_PREFIX + sessionId;
        log.info("Redis AI 채팅 히스토리 키 생성: {}", key);

        return key;
    }


    public static String getRequestCountKey(Long memberId) {

        validateMemberId(memberId);
        String key = REQUEST_COUNT_KEY_PREFIX + memberId;
        log.info("Redis AI 채팅 요청 횟수 키 생성: {}", key);

        return key;
    }

    private static void validateMemberId(Long memberId) {

        if (memberId == null || memberId <= 0) {
            throw new IllegalArgumentException("잘못된 MemberId 값: " + memberId);
        }
    }
}