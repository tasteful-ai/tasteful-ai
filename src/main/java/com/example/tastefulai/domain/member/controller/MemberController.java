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


    @PostMapping("/password/check")
    public ResponseEntity<CommonResponseDto<Void>> verifyPassword(@Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto,
                                                                  @AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {
        String password = passwordVerifyRequestDto.getPassword();
        Long memberId = memberDetailsImpl.getId();

        memberService.verifyPassword(memberId, password);

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 검증 완료", null), HttpStatus.OK);
    }


    @DeleteMapping("/delete")
    public ResponseEntity<CommonResponseDto<String>> deleteOwnAccount(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();
        memberService.deleteMember(memberId);

        return ResponseEntity.ok(new CommonResponseDto<>("회원 삭제 완료", null));
    }


    @PatchMapping("/profiles/nickname")
    public ResponseEntity<CommonResponseDto<String>> updateNickname(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl,
                                                                  @RequestBody ProfileRequestDto profileRequestDto) {

        Long memberId = memberDetailsImpl.getId();

        memberService.updateNickname(memberId, profileRequestDto.getNickname());

        return new ResponseEntity<>(new CommonResponseDto<>("닉네임 변경 완료", null), HttpStatus.OK);
    }


    @GetMapping("/profiles")
    public ResponseEntity<CommonResponseDto<ProfileResponseDto>> getProfile(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();

        ProfileResponseDto profileResponseDto = memberService.getMemberProfile(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("프로필 조회 완료", profileResponseDto), HttpStatus.OK);
    }

}