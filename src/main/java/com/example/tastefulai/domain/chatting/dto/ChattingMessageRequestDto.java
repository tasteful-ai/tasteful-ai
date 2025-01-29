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

    public ChattingMessageRequestDto(String message) {
        this.message = message;
    }
}
