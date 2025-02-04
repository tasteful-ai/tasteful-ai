package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.LoginRequestDto;
import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.service.AuthService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<MemberResponseDto>> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = authService.signup(memberRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>("회원가입 완료", memberResponseDto), HttpStatus.CREATED);
    }


    // 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<JwtAuthResponse>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtAuthResponse jwtAuthResponse = authService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return new ResponseEntity<>(new CommonResponseDto<>("로그인 성공", jwtAuthResponse), HttpStatus.OK);
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<String>> logout(@RequestHeader("Authorization") String token) {
        if (!token.startsWith("Bearer ")) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_FORMAT);
        }
        String jwtToken = token.substring(7);
        authService.logout(jwtToken);

        return new ResponseEntity<>(new CommonResponseDto<>("로그아웃 완료", null), HttpStatus.OK);
    }
}