package com.example.tastefulai.domain.taste.service;

import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;

public interface TasteService {

    TasteResponseDto updateTaste(Long memberId, TasteRequestDto tasteRequestDto);
}
