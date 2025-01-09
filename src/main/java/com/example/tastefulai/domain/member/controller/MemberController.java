package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
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
        String email = memberRequestDto.getEmail();
        String password = memberRequestDto.getPassword();
        String nickname = memberRequestDto.getNickname();
        Integer age = memberRequestDto.getAge();
        GenderRole genderRole = memberRequestDto.getGenderRole();
        MemberRole memberRole = memberRequestDto.getMemberRole();

        MemberResponseDto memberResponseDto = memberService.signup(email, password, nickname, age, genderRole, memberRole);
        return new ResponseEntity<>(new CommonResponseDto<>("회원가입 완료", memberResponseDto), HttpStatus.CREATED);
    }

    // 2. 로그인
    @PostMapping("/login")
    public ResponseEntity<CommonResponseDto<JwtAuthResponse>> login(@Valid @RequestBody MemberRequestDto memberRequestDto) {
        String email = memberRequestDto.getEmail();
        String password = memberRequestDto.getPassword();

        JwtAuthResponse jwtAuthResponse = memberService.login(email, password);
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
        String currentPassword = passwordChangeRequestDto.getCurrentPassword();
        String newPassword = passwordChangeRequestDto.getNewPassword();

        memberService.changePassword(memberId, currentPassword, newPassword);
        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 변경 완료",null), HttpStatus.OK);
    }

    // 5. 비밀번호 검증
    @PostMapping("/members/{memberId}/check")
    public ResponseEntity<CommonResponseDto<Void>> verifyPassword(@PathVariable Long memberId,
                                                                  @Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto) {
        String password = passwordVerifyRequestDto.getPassword();
        memberService.verifyPassword(memberId, password);
        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 검증 완료",null), HttpStatus.OK);
    }

    // 6. 회원탈퇴
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> delete(@PathVariable Long memberId) {
        memberService.deleteMember(memberId);
        return new ResponseEntity<>(new CommonResponseDto<>("회원 탈퇴 완료",null), HttpStatus.OK);
    }
}