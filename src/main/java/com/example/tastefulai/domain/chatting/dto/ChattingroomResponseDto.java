package com.example.tastefulai.domain.chatting.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingroomResponseDto {

    private final Long id;
    private final LocalDateTime createdAt;

    public ChattingroomResponseDto(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
