package com.example.tastefulai.domain.member.validation;

import com.example.tastefulai.domain.member.dto.LoginRequestDto;
import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.PasswordUpdateRequestDto;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.example.tastefulai.domain.member.validation.ValidationPatterns.EMAIL_PATTERN;
import static com.example.tastefulai.domain.member.validation.ValidationPatterns.PASSWORD_PATTERN;

@Component
@RequiredArgsConstructor
public class MemberValidation {

    private final MemberRepository memberRepository;

    // 회원가입 유효성 검사
    public void validateSignUp(MemberRequestDto memberRequestDto) {
        if (memberRequestDto == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        validateEmail(memberRequestDto.getEmail());
        validatePassword(memberRequestDto.getPassword());
        validateNicknameOrEmail(memberRequestDto.getEmail(), memberRequestDto.getNickname());
    }

    // 로그인 유효성 검사
    public void validateLogin(String email, String password) {
        if (email == null || password == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
    }

    // 비밀번호 변경 유효성 검사
    public void validatePasswordUpdate(String currentPassword, String newPassword) {
        if (currentPassword == null || newPassword == null) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        if (currentPassword.equals(newPassword)) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        validatePassword(newPassword);
    }

    // 비밀번호 검증
    public void validatePassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        if (!isValidPassword(password)) {
            throw new CustomException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }
    }

    // 이메일 검증
    public void validateEmail(String email) {
        if (!isValidEmail(email)) {
            throw new CustomException(ErrorCode.EMAIL_FORM_ERROR);
        }
    }

    // 닉네임/이메일 중복 확인
    public void validateNicknameOrEmail(String email, String nickName) {
        if (memberRepository.existsByEmailOrNickname(email, nickName)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
    }

    // 유효성 검사 - 비밀번호 패턴
    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    // 유효성 검사 - 이메일 형식
    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}