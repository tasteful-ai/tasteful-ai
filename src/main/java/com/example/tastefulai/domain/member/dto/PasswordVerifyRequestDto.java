package com.example.tastefulai.domain.member.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;

import lombok.Getter;

@Getter
public class PasswordVerifyRequestDto {

    @NotBlank(message = "비밀번호를 입력해주세요")
    private final String password;

    @JsonCreator
    public PasswordVerifyRequestDto(@JsonProperty("password") String password) {
        this.password = password;
    }
}
