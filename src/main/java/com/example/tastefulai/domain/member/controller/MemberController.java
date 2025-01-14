package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.*;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
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
    public ResponseEntity<CommonResponseDto<Void>> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        // 로그인 처리
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();

        JwtAuthResponse jwtAuthResponse = memberService.login(email, password);

        // AccessToken 쿠키 설정
        ResponseCookie accessTokenCookie = createCookie("access-token", jwtAuthResponse.getAccessToken(), 3600);
        // RefreshToken 쿠키 설정
        ResponseCookie refreshTokenCookie = createCookie("refresh-token", jwtAuthResponse.getRefreshToken(), 7 * 24 * 3600);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, accessTokenCookie.toString())
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(new CommonResponseDto<>("로그인 성공", null));
    }

    // 3. 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<CommonResponseDto<String>> logout(@RequestHeader("Authorization") String token) {
        String jwtToken = token.replace("Bearer ", "");
        memberService.logout(jwtToken);
        return new ResponseEntity<>(new CommonResponseDto<>("로그아웃 완료", null), HttpStatus.OK);
    }

    // 4. 비밀번호 변경
    @Transactional
    @PatchMapping("/passwords")
    public ResponseEntity<CommonResponseDto<Void>> updatePassword(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl,
                                                                  @RequestHeader("Authorization") String authorizationHeader,
                                                                  @Valid @RequestBody PasswordUpdateRequestDto passwordUpdateRequestDto) {

        String email = memberDetailsImpl.getUsername();
        String currentPassword = passwordUpdateRequestDto.getCurrentPassword();
        String newPassword = passwordUpdateRequestDto.getNewPassword();
        String currentAccessToken = authorizationHeader.replace("Bearer ", "");

        memberService.updatePassword(email, currentPassword, newPassword, currentAccessToken);

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 변경 완료", null), HttpStatus.OK);
    }

    // 5. 비밀번호 검증
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    @PostMapping("/members/{memberId}/check")
    public ResponseEntity<CommonResponseDto<Void>> verifyPassword(@PathVariable Long memberId,
                                                                  @Valid @RequestBody PasswordVerifyRequestDto passwordVerifyRequestDto) {
        String password = passwordVerifyRequestDto.getPassword();

        // 현재 로그인된 사용자 정보 조회
        MemberDetailsImpl currentUser = (MemberDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // 현재 로그인된 사용자와 사용자 ID 비교
        if(!currentUser.getId().equals(memberId)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        memberService.verifyPassword(memberId, password);

        return new ResponseEntity<>(new CommonResponseDto<>("비밀번호 검증 완료",null), HttpStatus.OK);
    }

    // 6. 일반 사용자 = 자신의 계정 삭제
    @PreAuthorize("hasRole('USER') or hasRole('OWNER')")
    @DeleteMapping("/members/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteOwnAccount(@PathVariable Long memberId) {

        memberService.deleteMember(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("회원 탈퇴 완료",null), HttpStatus.OK);
    }


    // ** 공통 로직 **
    private ResponseCookie createCookie(String name, String value, long maxAge) {
        return ResponseCookie.from(name, value)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(maxAge)
                .build();
    }
}