package com.example.tastefulai.domain.member.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {

    private String nickname;

    public ProfileRequestDto(String nickname) {
        this.nickname = nickname;
    }

    public ProfileRequestDto() {
    }
}
