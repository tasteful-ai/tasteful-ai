package com.example.tastefulai.global.common.dto;

import lombok.Getter;

@Getter

public class TokenDto implements BaseDtoType {

    private final String accessToken;
    private final String refreshToken;

    public TokenDto(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
