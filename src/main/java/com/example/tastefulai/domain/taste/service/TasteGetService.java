package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.taste.dto.TasteResponseDto;

public interface TasteGetService {
    TasteResponseDto getGenres(Long memberId);
    TasteResponseDto getLikeFoods(Long memberId);
    TasteResponseDto getDislikeFoods(Long memberId);
    TasteResponseDto getDietaryPreferences(Long memberId);
    TasteResponseDto getSpicyLevel(Long memberId);
}

