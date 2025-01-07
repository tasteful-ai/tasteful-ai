package com.example.tastefulai.domain.chatting.dto;

import com.example.tastefulai.global.common.dto.BaseDtoType;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingroomResponseDto implements BaseDtoType {

    private final Long id;
    private final LocalDateTime createdAt;

    public ChattingroomResponseDto(Long id, LocalDateTime createdAt) {
        this.id = id;
        this.createdAt = createdAt;
    }
}
