package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<MemberResponseDto>> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.signup(memberRequestDto);
        return new ResponseEntity<>(new CommonResponseDto<>("회원가입 완료", memberResponseDto), HttpStatus.CREATED);
    }
}