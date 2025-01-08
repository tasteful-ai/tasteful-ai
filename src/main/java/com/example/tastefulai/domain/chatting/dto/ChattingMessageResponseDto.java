package com.example.tastefulai.domain.chatting.dto;

import lombok.Getter;

@Getter
public class ChattingMessageResponseDto {

    private final String senderNickname;
    private final String message;

    public ChattingMessageResponseDto(String senderNickname, String message) {
        this.senderNickname = senderNickname;
        this.message = message;
    }
}
