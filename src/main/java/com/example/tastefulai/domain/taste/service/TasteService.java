package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.entity.Taste;

public interface TasteService {

    TasteResponseDto updateGenres(Long memberId, String genres);
    TasteResponseDto updateLikeFoods(Long memberId, String likeFood);
    TasteResponseDto updateDislikeFoods(Long memberId, String dislikeFood);
    TasteResponseDto updateDietaryPreferences(Long memberId, String dietaryPreferences);
    TasteResponseDto updateSpicyLevel(Long memberId, Integer spicyLevel);

    TasteResponseDto getGenres(Long memberId);
    TasteResponseDto getLikeFoods(Long memberId);
    TasteResponseDto getDislikeFoods(Long memberId);
    TasteResponseDto getDietaryPreferences(Long memberId);
    TasteResponseDto getSpicyLevel(Long memberId);

    Taste findOrCreateTaste(Member member);
}