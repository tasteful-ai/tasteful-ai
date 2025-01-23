package com.example.tastefulai.domain.chatting.websocket.dto;

import com.example.tastefulai.domain.chatting.websocket.enums.MessageType;
import lombok.Getter;

@Getter
public class ChatMessageDto {

    private final MessageType type;
    private final String sender;
    private final String message;
    private final Long chattingroomId;

    public ChatMessageDto(MessageType type, String sender, String message, Long chattingroomId) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.chattingroomId = chattingroomId;
    }
}
