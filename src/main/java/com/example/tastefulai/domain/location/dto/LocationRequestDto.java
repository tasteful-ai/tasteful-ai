package com.example.tastefulai.domain.location.dto;

import lombok.Getter;

@Getter
public class LocationRequestDto {

//    private final String userId;
    private final String keyword;
//    private final Double latitude;  // 위도
//    private final Double longitude; // 경도

    public LocationRequestDto(String keyword) {
        this.keyword = keyword;
    }
}
