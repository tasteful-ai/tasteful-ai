package com.example.tastefulai.domain.aichat.dto;

/*
역직렬화 오류로 인해 AI 요청이 실패하는 오류 발생
Record 클래스는 불변 데이터를 객체 간에 전달하는 작업을 간단하게 만듦.
멤버 변수는 private final로 선언(dto 객체 특성 상 값 무결성 유지로 final 선언)
필드 별 getter 자동 생성
모든 멤버 변수를 인자로 하는 public 생성자 자동 생성
record는 불변 데이터를 다루므로 생성자 실행 시 인스턴트 필드 수정 불가
equals, hashcode, toString 자동 생성
기본 생성자는 제공 불가, 직접 생성해야 함.
*/
public record AiChatRequestDto(String message) {}
