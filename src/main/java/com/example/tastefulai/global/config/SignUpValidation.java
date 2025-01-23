package com.example.tastefulai.global.config;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import org.springframework.stereotype.Component;
import java.util.regex.Pattern;

@Component
public class SignUpValidation {

    // 비밀번호 및 이메일 정규식 상수화
    private final Pattern passwordPattern = Pattern.compile(ValidationPatterns.PASSWORD_PATTERN);
    private final Pattern emailPattern = Pattern.compile(ValidationPatterns.EMAIL_PATTERN);

    public void validateMemberRequest(MemberRequestDto memberRequestDto, MemberRepository memberRepository) {
        // 이메일 형식 확인
        if (!isValidEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_FORM_ERROR);
        }

        // 비밀번호 패턴 확인
        if (!isValidPassword(memberRequestDto.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }

        // 중복 닉네임 확인
        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        // 이메일 중복 여부 확인
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
    }

    public boolean isValidPassword(String password) {
        return password != null && passwordPattern.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        return email != null && emailPattern.matcher(email).matches();
    }
}