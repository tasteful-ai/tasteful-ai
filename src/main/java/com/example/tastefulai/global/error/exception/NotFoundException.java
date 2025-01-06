package com.example.tastefulai.global.error.exception;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class NotFoundException extends CustomException {

    public NotFoundException(ErrorCode errorCode) {
        super(errorCode);
    }
}
