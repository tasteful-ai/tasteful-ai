package com.example.tastefulai.domain.chatting.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ChattingMessageRequestDto {

    @NotBlank
    @Size(max = 255, message = "메시지는 최대 255자까지 가능합니다.")
    private String message;

    @NotNull(message = "채팅방 ID는 필수입니다.")
    private Long chattingroomId;

    public ChattingMessageRequestDto(String message, Long chattingroomId) {
        this.message = message;
        this.chattingroomId = chattingroomId;
    }
}
