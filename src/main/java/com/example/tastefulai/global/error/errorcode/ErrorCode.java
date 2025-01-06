package com.example.tastefulai.global.error.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잠시 후 다시 시도해주세요."),
    INVALID_EMAIL(HttpStatus.BAD_REQUEST, "중복된 이메일이 존재합니다."),
    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 형식의 파일입니다."),
    LARGE_FILE(HttpStatus.BAD_REQUEST, "파일 용량 크기를 초과하였습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "로그인 해주세요."),
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호 입니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "본인이 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    INVALID_VALUE(HttpStatus.BAD_REQUEST,"올바르지 않은 형식입니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
