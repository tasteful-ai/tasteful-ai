package com.example.tastefulai.domain.chatting.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingroomResponseDto {

    private final Long id;
    private final LocalDateTime createdAt;
    private final String creatorNickname;

    public ChattingroomResponseDto(Long id, LocalDateTime createdAt, String creatorNickname) {
        this.id = id;
        this.createdAt = createdAt;
        this.creatorNickname = creatorNickname;
    }
}
