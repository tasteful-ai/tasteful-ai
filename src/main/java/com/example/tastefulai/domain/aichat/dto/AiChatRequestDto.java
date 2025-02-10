package com.example.tastefulai.domain.aichat.dto;

/**
 * AI 채팅 요청을 위한 DTO
 *
 * <p>이 클래스는 Java의 {@code record}를 사용하여 불변 데이터를 간결하게 표현
 * <ul>
 *     <li>멤버 변수는 {@code private final}로 선언되며, 값의 무결성을 유지</li>
 *     <li>각 필드에 대한 getter가 자동 생성</li>
 *     <li>모든 멤버 변수를 포함하는 생성자가 자동으로 제공</li>
 *     <li>{@code equals}, {@code hashCode}, {@code toString} 메서드 자동 생성</li>
 *     <li>{@code record}는 불변 데이터를 다루므로, 생성자 실행 시 인스턴스 필드를 수정할 수 없음</li>
 *     <li>기본 생성자는 제공되지 않으며, 직접 생성해야 함</li>
 * </ul>
 */
public record AiChatRequestDto(String message) {}
