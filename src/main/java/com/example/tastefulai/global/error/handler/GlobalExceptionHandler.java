package com.example.tastefulai.global.error.handler;

import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.BadRequestException;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.ForbiddenException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.error.exception.UnAuthorizedException;
import com.example.tastefulai.global.error.response.ErrorResponse;
import io.jsonwebtoken.JwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({CustomException.class, BadRequestException.class, ForbiddenException.class, NotFoundException.class, UnAuthorizedException.class})
    public ResponseEntity<ErrorResponse> handleBadRequestException(CustomException customException) {

        return ErrorResponse.toResponseEntity(customException.getErrorCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException customException) {

        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_VALUE);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(AuthenticationException customException) {

        return ErrorResponse.toResponseEntity(ErrorCode.INVALID_TOKEN);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException customException) {

        return ErrorResponse.toResponseEntity(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException customException) {
        return ErrorResponse.toResponseEntity(ErrorCode.FORBIDDEN);
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorResponse> handleJwtException(JwtException customException) {
        return ErrorResponse.toResponseEntity(ErrorCode.EXPIRED_TOKEN);
    }
}
