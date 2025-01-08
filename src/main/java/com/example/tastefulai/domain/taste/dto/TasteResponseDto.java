package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

@Getter
public class TasteResponseDto {

    private String genre;
    private String likeFood;
    private String dislikeFood;
    private String dietaryPreference;
    private Integer spicyLevel;

    public TasteResponseDto(String genre, String likeFood,
                            String dislikeFood, String dietaryPreference, Integer spicyLevel) {
        this.genre = genre;
        this.likeFood = likeFood;
        this.dislikeFood = dislikeFood;
        this.dietaryPreference = dietaryPreference;
        this.spicyLevel = spicyLevel;
    }
}
