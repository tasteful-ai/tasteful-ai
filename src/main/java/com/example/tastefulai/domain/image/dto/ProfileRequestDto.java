package com.example.tastefulai.domain.image.dto;

import lombok.Getter;

@Getter
public class ProfileRequestDto {

    private final String nickname;

    public ProfileRequestDto(String nickname) {
        this.nickname = nickname;
    }
}
