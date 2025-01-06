package com.example.tastefulai.global.error.response;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class ErrorResponse {

    private final LocalDateTime timestamp = LocalDateTime.now();
    private final int status;
    private final String error;
    private final String code;
    private final String message;

    public static ResponseEntity<ErrorResponse> toResponseEntity(ErrorCode errorCode) {

        return new ResponseEntity<>(new ErrorResponse(
                errorCode.getHttpStatus().value(),
                errorCode.getHttpStatus().name(),
                errorCode.name(),
                errorCode.getMessage()),
                errorCode.getHttpStatus()
        );
    }
}
