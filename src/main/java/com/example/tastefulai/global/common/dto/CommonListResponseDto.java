package com.example.tastefulai.global.common.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class CommonListResponseDto<T extends BaseDtoType> {

    private final String message;
    private final List<T> data;

    public CommonListResponseDto(String message, List<T> data) {
        this.message = message;
        this.data = data;
    }
}
