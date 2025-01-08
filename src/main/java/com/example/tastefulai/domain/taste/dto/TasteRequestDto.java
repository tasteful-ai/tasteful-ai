package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

@Getter
public class TasteRequestDto {

    private final String genre;
    private final String likeFood;
    private final String dislikeFood;
    private final String dietaryPreference;
    private final Integer spicyLevel;

    public TasteRequestDto(String genre, String likeFood, String dislikeFood, String dietaryPreference, Integer spicyLevel) {
        this.genre = genre;
        this.likeFood = likeFood;
        this.dislikeFood = dislikeFood;
        this.dietaryPreference = dietaryPreference;
        this.spicyLevel = spicyLevel;
    }
}
