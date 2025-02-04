package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.taste.dto.TasteResponseDto;

import java.util.List;

public interface TasteUpdateService {

    TasteResponseDto updateGenres(Long memberId, List<String> genresRequest);
    TasteResponseDto updateLikeFoods(Long memberId, List<String> likeFoodsRequest);
    TasteResponseDto updateDislikeFoods(Long memberId, List<String> dislikeFoodsRequest);
    TasteResponseDto updateDietaryPreferences(Long memberId, List<String> dietaryPreferencesRequest);
    TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevel);
}
