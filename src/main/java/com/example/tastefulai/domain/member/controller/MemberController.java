package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 비밀번호 변경
    @PatchMapping("/password")
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


    // 비밀번호 검증
    @PostMapping("/password/check")
    public ResponseEntity<CommonResponseDto<Void>> verifyPassword(@Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto,
                                                                  @AuthenticationPrincipal MemberDetailsImpl currentUser) {
        String password = passwordVerifyRequestDto.getPassword();
        Long memberId = currentUser.getId();

        memberService.verifyPassword(memberId, password);

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 검증 완료", null), HttpStatus.OK);
    }


    // 회원 탈퇴
    @DeleteMapping("/me")
    public ResponseEntity<CommonResponseDto<Void>> deleteOwnAccount(@AuthenticationPrincipal MemberDetailsImpl MemberDetailsImpl) {

        Long memberId = MemberDetailsImpl.getId();
        memberService.deleteMember(memberId);

        return ResponseEntity.noContent().build();
    }


    // 닉네임 수정
    @PatchMapping("/profile/nickname")
    public ResponseEntity<CommonResponseDto<Void>> updateNickname(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl,
                                                                  @RequestBody ProfileRequestDto profileRequestDto) {

        Long memberId = memberDetailsImpl.getId();

        memberService.updateNickname(memberId, profileRequestDto.getNickname());

        return new ResponseEntity<>(new CommonResponseDto<>("닉네임 변경 완료", null), HttpStatus.OK);
    }

    // 프로필 조회
    @GetMapping("/profiles")
    public ResponseEntity<CommonResponseDto<ProfileResponseDto>> getProfile(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();

        ProfileResponseDto profileResponseDto = memberService.getMemberProfile(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 조회 완료", profileResponseDto), HttpStatus.OK);
    }

}