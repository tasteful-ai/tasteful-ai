package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.service.MemberService;
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
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<MemberResponseDto>> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.signup(memberRequestDto);
        return new ResponseEntity<>(new CommonResponseDto<>("회원가입 완료", memberResponseDto), HttpStatus.CREATED);
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<JwtAuthResponse>> login(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        LoginRequestDto loginRequestDto = new LoginRequestDto(memberRequestDto.getEmail(), memberRequestDto.getPassword());
        JwtAuthResponse jwtAuthResponse = memberService.login(loginRequestDto);
        return new ResponseEntity<>(new CommonResponseDto<>("로그인 성공",jwtAuthResponse), HttpStatus.OK);
    }

    // 3. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<String>> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        memberService.logout(jwtToken);
        return new ResponseEntity<>(new CommonResponseDto<>("로그아웃 완료", null), HttpStatus.OK);
    }

    // 4. 비밀번호 변경
    @PatchMapping("/{memberId}/passwords")
    public ResponseEntity<CommonResponseDto<Void>> changePassword(@PathVariable Long memberId,
                                                 @Valid @RequestBody PasswordChangeRequestDto passwordChangeRequestDto) {
        memberService.changePassword(memberId, passwordChangeRequestDto);
        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 변경 완료",null), HttpStatus.OK);
    }

    // 5. 회원탈퇴
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> delete(@PathVariable Long memberId,
                                                          @Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto) {
        memberService.deleteMember(memberId, passwordVerifyRequestDto.getPassword());
        return new ResponseEntity<>(new CommonResponseDto<>("회원 탈퇴 완료",null), HttpStatus.OK);
    }
}