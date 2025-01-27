package com.example.tastefulai.domain.chatting.websocket.dto;

import com.example.tastefulai.domain.chatting.websocket.enums.MessageType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class ChatMessageDto {

    private final MessageType type;
    private final String sender;
    private final String message;
    private final Long chattingroomId;

    @JsonCreator
    public ChatMessageDto(@JsonProperty("type") MessageType type,
                          @JsonProperty("sender") String sender,
                          @JsonProperty("message") String message,
                          @JsonProperty("chattingroomId") Long chattingroomId) {
        this.type = type;
        this.sender = sender;
        this.message = message;
        this.chattingroomId = chattingroomId;
    }
}
