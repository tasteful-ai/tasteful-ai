package com.example.tastefulai.global.config;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import org.springframework.stereotype.Component;

import static com.example.tastefulai.global.config.ValidationPatterns.EMAIL_PATTERN;
import static com.example.tastefulai.global.config.ValidationPatterns.PASSWORD_PATTERN;

@Component
public class SignUpValidation {

    public void validateMemberRequest(MemberRequestDto memberRequestDto, MemberRepository memberRepository) {
        // 이메일 형식 확인
        if (!isValidEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_FORM_ERROR);
        }

        // 비밀번호 패턴 확인
        if (!isValidPassword(memberRequestDto.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }

        // 중복 닉네임/이메일 확인
        if (memberRepository.existsByEmailOrNickname(memberRequestDto.getNickname(), memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
    }

    public boolean isValidPassword(String password) {
        return password != null && PASSWORD_PATTERN.matcher(password).matches();
    }

    public boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}