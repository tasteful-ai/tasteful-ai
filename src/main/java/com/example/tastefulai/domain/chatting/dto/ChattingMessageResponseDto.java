package com.example.tastefulai.domain.chatting.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class ChattingMessageResponseDto {

    private final String senderEmail;
    private final String senderNickname;
    private final String message;
    private final Long chattingroomId;

    @JsonCreator
    public ChattingMessageResponseDto(
            @JsonProperty("senderEmail") String senderEmail,
            @JsonProperty("senderNickname") String senderNickname,
            @JsonProperty("message") String message,
            @JsonProperty("chattingroomId") Long chattingroomId) {
        this.senderEmail = senderEmail;
        this.senderNickname = senderNickname;
        this.message = message;
        this.chattingroomId = chattingroomId;
    }
}
