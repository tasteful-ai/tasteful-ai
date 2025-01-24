package com.example.tastefulai.domain.image.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileResponseDto {

    private String nickname;

    private String imageUrl;

    private LocalDate createdAt;

    private String tastes;

    public ProfileResponseDto(String nickname, String imageUrl, LocalDate createdAt, String tastes) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.tastes = tastes;
    }
}
