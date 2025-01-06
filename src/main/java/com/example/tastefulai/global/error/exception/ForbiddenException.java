package com.example.tastefulai.global.error.exception;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import lombok.Getter;

@Getter
public class ForbiddenException extends CustomException {

    public ForbiddenException(ErrorCode errorCode) {
        super(errorCode);
    }
}
