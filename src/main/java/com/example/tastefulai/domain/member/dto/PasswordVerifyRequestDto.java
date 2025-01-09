package com.example.tastefulai.domain.member.dto;

import lombok.Getter;

@Getter
public class PasswordVerifyRequestDto {

    private final String password;

    public PasswordVerifyRequestDto(String password) {
        this.password = password;
    }
}
