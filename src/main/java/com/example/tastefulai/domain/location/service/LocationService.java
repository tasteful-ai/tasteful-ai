package com.example.tastefulai.domain.location.service;

import com.example.tastefulai.domain.location.dto.LocationResponseDto;

import java.util.List;

public interface LocationService {
    List<LocationResponseDto> findByKeyword(String keyword);

}