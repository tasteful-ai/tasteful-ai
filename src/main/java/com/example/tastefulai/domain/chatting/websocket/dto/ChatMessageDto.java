package com.example.tastefulai.domain.chatting.websocket.dto;

import com.example.tastefulai.domain.chatting.websocket.enums.MessageType;
import lombok.Getter;

@Getter
public class ChatMessageDto {

    private final MessageType type;
    private final String sender;
    private final String message;

    public ChatMessageDto(MessageType type, String sender, String message) {
        this.type = type;
        this.sender = sender;
        this.message = message;
    }
}
