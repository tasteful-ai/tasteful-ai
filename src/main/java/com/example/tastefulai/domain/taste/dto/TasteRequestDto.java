package com.example.tastefulai.domain.taste.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.util.List;

@Getter
public class TasteRequestDto {

    @Size(max = 5, message = "장르는 최대 5개까지만 선택 가능합니다.")
    private List<String> genres;

    @Size(max = 5, message = "선호하는 음식은 최대 5개까지만 선택 가능합니다.")
    private List<String> likeFoods;

    @Size(max = 5, message = "싫어하는 음식은 최대 5개까지만 선택 가능합니다.")
    private List<String> dislikeFoods;

    @Size(max = 5, message = "식단 성향은 최대 5개까지만 선택 가능합니다.")
    private List<String> dietaryPreferences;

    @Max(value = 5)
    private Integer spicyLevel;

    public TasteRequestDto(List<String> genres,
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
