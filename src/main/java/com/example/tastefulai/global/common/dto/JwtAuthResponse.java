package com.example.tastefulai.global.common.dto;

import lombok.Getter;

@Getter
public class JwtAuthResponse {

    private final String accessToken;
    private final String refreshToken;

    public JwtAuthResponse(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
