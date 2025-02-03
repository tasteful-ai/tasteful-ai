package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;

public interface TasteGetService {
    TasteResponseDto getGenres(Long memberId);
    TasteResponseDto getLikeFoods(Long memberId);
    TasteResponseDto getDislikeFoods(Long memberId);
    TasteResponseDto getDietaryPreferences(Long memberId);
    TasteResponseDto getSpicyLevel(Long memberId);

    // 모든 취향 정보 한 번에 조회 (SQL문 5개)
    TasteResponseDto getCompleteTaste(Long memberId);
}

