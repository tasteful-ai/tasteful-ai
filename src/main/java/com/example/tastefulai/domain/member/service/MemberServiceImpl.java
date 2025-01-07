package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.config.RedisConfig;
import com.example.tastefulai.global.config.auth.SignUpValidation;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidation signUpValidation;
    private final JwtProvider jwtProvider;
    private final RedisConfig redisConfig;

    /**
     * 1. 회원 가입 :
     * - 이메일 중복 여부 확인,
     * - 이메일 형식 확인,
     * - 비밀번호 패턴 확인,
     * @param memberRequestDto 회원가입 요청 데이터
     * @return 회원가입 완료된 회원의 응답 데이터
     */
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {

        // 이메일 중복 여부 확인
        if (memberRepository.existsByEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }
        // 이메일 형식 확인
        if (!signUpValidation.isValidEmail(memberRequestDto.getEmail())) {
            throw new CustomException(ErrorCode.EMAIL_FORM_ERROR);
        }
        // 비밀번호 패턴 확인
        if (!signUpValidation.isValidPassword(memberRequestDto.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_PATTERN_ERROR);
        }

        // 비밀번호 암호화
        String encodedPassword = passwordEncoder.encode(memberRequestDto.getPassword());

        Member member = new Member(
                memberRequestDto.getEmail(),
                encodedPassword,
                memberRequestDto.getNickname(),
                memberRequestDto.getAge(),
                memberRequestDto.getGenderRole(),
                memberRequestDto.getMemberRole(),
                null
        );
        memberRepository.save(member);
        return new MemberResponseDto(member.getId(), member.getMemberRole(), member.getEmail(), member.getNickname());
    }
}