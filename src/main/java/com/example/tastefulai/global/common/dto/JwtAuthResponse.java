package com.example.tastefulai.global.common.dto;

import lombok.Getter;

@Getter
public class JwtAuthResponse {

    private String tokenType = "Bearer";
    private String accessToken;
    private String refreshToken;

    public JwtAuthResponse(String tokenType, String accessToken, String refreshToken) {
        this.tokenType = tokenType;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
