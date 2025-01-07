package com.example.tastefulai.domain.chatting.dto;

import com.example.tastefulai.global.common.dto.BaseDtoType;
import lombok.Getter;

@Getter
public class ChattingMessageResponseDto implements BaseDtoType {

    private final String senderNickname;
    private final String message;

    public ChattingMessageResponseDto(String senderNickname, String message) {
        this.senderNickname = senderNickname;
        this.message = message;
    }
}
