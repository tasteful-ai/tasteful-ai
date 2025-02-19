package com.example.tastefulai.global.error.errorcode;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // 400 BAD_REQUEST
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잠시 후 다시 시도해주세요."),
    DUPLICATE_RESOURCE(HttpStatus.BAD_REQUEST, "이미 사용 중인 이메일 또는 닉네임입니다."),
    EMAIL_FORM_ERROR(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    PASSWORD_PATTERN_ERROR(HttpStatus.BAD_REQUEST, "비밀번호는 최소 8자 이상이어야 하며, 숫자, 특수문자, 대문자를 포함해야 합니다."),
    PASSWORD_CANNOT_BE_EMPTY(HttpStatus.BAD_REQUEST, "비밀번호는 필수 입력 값입니다. "),
    INVALID_TOKEN_FORMAT(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식입니다."),
    INVALID_CREDENTIALS(HttpStatus.BAD_REQUEST, "잘못된 인증 정보 입니다."),
    PASSWORD_SAME_AS_OLD(HttpStatus.BAD_REQUEST, "새 비밀번호는 기존 비밀번호와 다르게 설정해야 합니다."),
    VERIFY_PASSWORD_REQUIRED(HttpStatus.BAD_REQUEST, "비밀번호 검증이 필요합니다."),
    CANNOT_MODIFY_DEACTIVATED_MEMBER(HttpStatus.BAD_REQUEST, "탈퇴한 사용자는 권한을 변경할 수 없습니다."),
    CANNOT_CHANGE_TO_SAME_ROLE(HttpStatus.BAD_REQUEST, "이미 동일한 권한입니다."),

    INVALID_FILE(HttpStatus.BAD_REQUEST, "지원하지 않는 형식의 파일입니다."),
    LARGE_FILE(HttpStatus.BAD_REQUEST, "파일 용량 크기를 초과하였습니다."),
    FILE_ACCESS_DENIED(HttpStatus.BAD_REQUEST, "파일에 접근할 수 없습니다."),
    NO_IMAGE_TO_DELETE (HttpStatus.BAD_REQUEST, "삭제할 파일이 존재하지 않습니다."),

    INVALID_SPICY_LEVEL (HttpStatus.BAD_REQUEST, "유효하지 않은 매운 정도 값입니다."),
    INVALID_INPUT_DATA(HttpStatus.BAD_REQUEST, "카테고리별 데이터는 최대 5개까지만 입력할 수 있습니다."),
    DUPLICATE_DATA(HttpStatus.BAD_REQUEST, "중복된 데이터가 포함되어 있습니다."),

    INVALID_JSON(HttpStatus.BAD_REQUEST, "JSON 데이터 형식이 올바르지 않습니다."),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청 정보입니다."),
    DUPLICATE_CHATROOM_NAME(HttpStatus.BAD_REQUEST, "이미 사용 중인 채팅방 이름입니다."),
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "메시지를 입력해주세요."),

    RECOMMENDATION_PARSING_ERROR(HttpStatus.BAD_REQUEST, "추천할 메뉴가 없습니다."),
    DESCRIPTION_PARSING_ERROR(HttpStatus.BAD_REQUEST, "설명이 제공되지 않았습니다."),
    JSON_PROCESSING_ERROR(HttpStatus.BAD_REQUEST, "JSON 처리 중 오류가 발생하였습니다."),

    // 401 UNAUTHORIZED
    UNAUTHORIZED_PASSWORD(HttpStatus.UNAUTHORIZED, "잘못된 비밀번호입니다."),
    UNAUTHORIZED_MEMBER(HttpStatus.UNAUTHORIZED, "본인이 아닙니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않는 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),

    // 403 FORBIDDEN
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),
    FORBIDDEN_ADMIN_ONLY(HttpStatus.FORBIDDEN, "ADMIN 권한만 사용할 수 있습니다."),
    ADMIN_PERMISSION_REQUIRED(HttpStatus.FORBIDDEN, "관리자 권한이 없습니다."),
    ADMIN_CANNOT_REMOVE_ADMIN(HttpStatus.FORBIDDEN, "관리자는 다른 관리자를 삭제할 권한이 없습니다."),

    // 404 NOT_FOUND
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다."),
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "멤버 정보를 찾을 수 없습니다."),
    NOT_FOUND_CHATTINGROOM(HttpStatus.NOT_FOUND, "채팅방이 존재하지 않습니다."),
    DATA_NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 데이터를 찾을 수 없습니다."),

    // 429 TOO_MANY_REQUESTS
    TOO_MANY_REQUESTS(HttpStatus.TOO_MANY_REQUESTS, "오늘의 메뉴 추천 요청 횟수를 초과했습니다."),

    //500 INTERNAL_SERVER_ERROR
    REDIS_SERIALIZATION_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Redis 메시지 직렬화 오류입니다."),
    S3CLIENT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "프로필 이미지 업데이트에 실패하였습니다. 다시 시도해주세요.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
