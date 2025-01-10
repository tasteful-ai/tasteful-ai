package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

@Getter
public class TasteResponseDto {

    private String genres;
    private String likeFoods;
    private String dislikeFoods;
    private String dietaryPreferences;
    private Integer spicyLevel;

    public TasteResponseDto(String genres, String likeFoods,
                            String dislikeFoods, String dietaryPreferences, Integer spicyLevel) {
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
    }
}
