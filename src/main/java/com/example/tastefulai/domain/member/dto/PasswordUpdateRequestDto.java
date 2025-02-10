package com.example.tastefulai.domain.member.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class PasswordUpdateRequestDto {

    @NotBlank(message = "현재 비밀번호는 필수 입력 값입니다.")
    private final String currentPassword;

    @NotBlank(message = "새 비밀번호는 필수 입력 값입니다.")
    private final String newPassword;

    public PasswordUpdateRequestDto(String currentPassword, String newPassword) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
