package com.example.tastefulai.domain.image.dto;

import lombok.Getter;

@Getter
public class ImageResponseDto {

    private String imageUrl;

    public ImageResponseDto(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
