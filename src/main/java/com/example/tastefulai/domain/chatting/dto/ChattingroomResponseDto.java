package com.example.tastefulai.domain.chatting.dto;

import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ChattingroomResponseDto {

    private final Long id;
    private final String roomName;
    private final String creatorNickname;
    private final LocalDateTime createdAt;

    public ChattingroomResponseDto(Long id, String roomName, String creatorNickname, LocalDateTime createdAt) {
        this.id = id;
        this.roomName = roomName;
        this.creatorNickname = creatorNickname;
        this.createdAt = createdAt;
    }

    public static ChattingroomResponseDto fromEntity(Chattingroom chattingroom) {
        return new ChattingroomResponseDto(
                chattingroom.getId(),
                chattingroom.getRoomName(),
                chattingroom.getCreator().getNickname(),
                chattingroom.getCreatedAt()
        );
    }
}
