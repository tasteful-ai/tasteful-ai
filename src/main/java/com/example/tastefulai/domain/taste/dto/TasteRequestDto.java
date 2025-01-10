package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

@Getter
public class TasteRequestDto {

    private final String genres;
    private final String likeFoods;
    private final String dislikeFoods;
    private final String dietaryPreferences;
    private final Integer spicyLevel;

    public TasteRequestDto(String genres, String likeFoods, String dislikeFoods, String dietaryPreferences, Integer spicyLevel) {
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
    }
}
