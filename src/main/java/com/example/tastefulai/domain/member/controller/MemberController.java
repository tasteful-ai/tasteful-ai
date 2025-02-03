package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 1. 회원가입
    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<MemberResponseDto>> signup(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        MemberResponseDto memberResponseDto = memberService.signup(
                memberRequestDto.getMemberRole(),
                memberRequestDto.getEmail(),
                memberRequestDto.getPassword(),
                memberRequestDto.getNickname(),
                memberRequestDto.getAge(),
                memberRequestDto.getGenderRole()
        );

        return new ResponseEntity<>(new CommonResponseDto<>("회원가입 완료", memberResponseDto), HttpStatus.CREATED);
    }


    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<JwtAuthResponse>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        JwtAuthResponse jwtAuthResponse = memberService.login(loginRequestDto.getEmail(), loginRequestDto.getPassword());

        return new ResponseEntity<>(new CommonResponseDto<>("로그인 성공", jwtAuthResponse), HttpStatus.OK);
    }


    // 3. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<String>> logout(@RequestHeader("Authorization") String token) {
       if (!token.startsWith("Bearer ")) {
           throw new CustomException(ErrorCode.INVALID_TOKEN_FORMAT);
       }
        String jwtToken = token.substring(7);
        memberService.logout(jwtToken);

        return new ResponseEntity<>(new CommonResponseDto<>("로그아웃 완료", null), HttpStatus.OK);
    }


    // 4. 비밀번호 변경
    @PatchMapping("/passwords")
    public ResponseEntity<CommonResponseDto<Void>> updatePassword(@Valid @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto,
                                                                  @AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl,
                                                                  @RequestHeader("Authorization") String authorizationHeader) {
        String email = memberDetailsImpl.getUsername();
        String currentAccessToken = authorizationHeader.substring(7);
        memberService.updatePassword(
                email,
                passwordUpdateRequestDto.getCurrentPassword(),
                passwordUpdateRequestDto.getNewPassword(),
                currentAccessToken
        );

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 변경 완료", null), HttpStatus.OK);
    }


    // 5. 비밀번호 검증
    @PostMapping("/members/{memberId}/check")
    public ResponseEntity<CommonResponseDto<Void>> verifyPassword(@Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto,
                                                                  @AuthenticationPrincipal MemberDetailsImpl currentUser,
                                                                  @PathVariable Long memberId) {
        String password = passwordVerifyRequestDto.getPassword();

        // 현재 로그인된 사용자와 사용자 ID 비교
        if(!currentUser.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        memberService.verifyPassword(memberId, password);

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 검증 완료", null), HttpStatus.OK);
    }


    // 6. 계정 삭제
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOwnAccount(@PathVariable Long memberId) {

       memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }
}