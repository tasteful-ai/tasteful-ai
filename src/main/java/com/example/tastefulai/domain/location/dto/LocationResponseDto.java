package com.example.tastefulai.domain.location.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LocationResponseDto {

    private String placeName;
    private String address;
    private String phone;
    private Double latitude;
    private Double longitude;

    public LocationResponseDto(String placeName, String address, String phone, Double latitude, Double longitude) {
        this.placeName = placeName;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
