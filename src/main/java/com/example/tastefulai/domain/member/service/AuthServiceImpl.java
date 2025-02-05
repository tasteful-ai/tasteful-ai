package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.domain.member.validation.MemberValidation;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final MemberValidation memberValidation;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Qualifier("blacklistTemplate")
    private final RedisTemplate<String, String> blacklistTemplate;
    private final BlacklistService blacklistService;

    private static final String REFRESH_TOKEN_KEY = "refreshToken:";

    @Override
    public Member findByEmail(String email) {

        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


    // 회원가입
    @Override
    @Transactional
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {
        memberValidation.validateSignUp(memberRequestDto);

        String encodedPassword = passwordEncoder.encode(memberRequestDto.getPassword());
        Member member = new Member(
                memberRequestDto.getMemberRole(),
                memberRequestDto.getEmail(),
                encodedPassword,
                memberRequestDto.getNickname(),
                memberRequestDto.getAge(),
                memberRequestDto.getGenderRole(),
                null
        );
        memberRepository.save(member);

        return MemberResponseDto.fromEntity(member);
    }


    // 로그인
    @Override
    public JwtAuthResponse login(String email, String password) {
        memberValidation.validateLogin(email, password);

        Member member = findByEmail(email);

        if (member.isDeleted()) {
            throw new CustomException(ErrorCode.MEMBER_NOT_FOUND);
        }

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);

        storeRefreshToken(email, refreshToken);

        return new JwtAuthResponse(member.getId(), member.getNickname(), member.getMemberRole(), accessToken, refreshToken);
    }


    private void storeRefreshToken(String email, String refreshToken) {
        blacklistTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY + email,
                refreshToken,
                jwtProvider.getRefreshTokenExpiryMillis(),
                TimeUnit.MILLISECONDS
        );
    }


    // 로그아웃
    @Override
    public void logout(String token) {
        blacklistService.addToBlacklist(token, jwtProvider.getAccessTokenExpiryMillis());
        log.info("AccessToken 블랙리스트 등록 완료: {}", token);
    }
}