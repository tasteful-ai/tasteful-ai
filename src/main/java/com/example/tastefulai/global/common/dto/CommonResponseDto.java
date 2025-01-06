package com.example.tastefulai.global.common.dto;

import lombok.Getter;

@Getter
public class CommonResponseDto<T extends BaseDtoType> {

    private final String message;

    private final T data;

    public CommonResponseDto(String message, T data) {
        this.message = message;
        this.data = data;
    }
}
