package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class TasteDto {

    private List<String> genres;
    private List<String> likeFoods;
    private List<String> dislikeFoods;
    private List<String> dietaryPreferences;
    private Integer spicyLevel;

    public TasteDto(List<String> genres,
                            List<String> likeFoods,
                            List<String> dislikeFoods,
                            List<String> dietaryPreferences,
                            Integer spicyLevel) {
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
    }
}
