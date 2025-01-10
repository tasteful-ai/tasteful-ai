package com.example.tastefulai.domain.taste.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TasteRequestDto {

    @Size(min = 0, max = 255)
    private String genres;

    @Size(min = 0, max = 255)
    private String likeFoods;

    @Size(min = 0, max = 255)
    private String dislikeFoods;

    @Size(min = 0, max = 255)
    private String dietaryPreferences;

    @Max(value = 5)
    private Integer spicyLevel;

    public TasteRequestDto(String genres, String likeFoods, String dislikeFoods, String dietaryPreferences, Integer spicyLevel) {
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
    }
}
