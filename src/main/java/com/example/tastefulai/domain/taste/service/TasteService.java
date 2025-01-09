package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.taste.dto.TasteResponseDto;

public interface TasteService {

    TasteResponseDto updateGenre(Long memberId, String genre);
    TasteResponseDto updateLikeFood(Long memberId, String likeFood);
    TasteResponseDto updateDislikeFood(Long memberId, String dislikeFood);
    TasteResponseDto updateDietaryPreference(Long memberId, String dietaryPreference);
    TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevel);
}