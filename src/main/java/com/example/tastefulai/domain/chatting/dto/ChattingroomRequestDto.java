package com.example.tastefulai.domain.chatting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingroomRequestDto {
    private String roomName;

    public ChattingroomRequestDto(String roomName) {
        this.roomName = roomName;
    }
}
