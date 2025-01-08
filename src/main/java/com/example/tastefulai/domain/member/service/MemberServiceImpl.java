package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.LoginRequestDto;
import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.dto.PasswordChangeRequestDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.common.dto.TokenService;
import com.example.tastefulai.global.config.RedisConfig;
import com.example.tastefulai.global.config.auth.SignUpValidation;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final SignUpValidation signUpValidation;
    private final JwtProvider jwtProvider;
    private final RedisConfig redisConfig;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final RedisTemplate<String, Object> redisTemplate;



    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    /**
     * 1. 회원 가입 :
     * - 중복 닉네임 확인,
     * - 이메일 중복 여부 확인,
     * - 이메일 형식 확인,
     * - 비밀번호 패턴 확인,
     * @param memberRequestDto 회원가입 요청 데이터
     * @return 회원가입 완료된 회원의 응답 데이터
     */
    public MemberResponseDto signup(MemberRequestDto memberRequestDto) {

        // 중복 닉네임 확인
        if (memberRepository.existsByNickname(memberRequestDto.getNickname())) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
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

    /**
     * 2. 로그인
     * - 사용자 확인
     * - 비밀번호 확인
     * - 인증 객체 생성 및 유효성 확인
     */
    public JwtAuthResponse login(LoginRequestDto loginRequestDto) {
        // 사용자 확인
        Member member = this.memberRepository.findActiveByEmail(loginRequestDto.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(loginRequestDto.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        // 인증 객체 생성 및 유효성 확인
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword())
        );

        // JMT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(loginRequestDto.getEmail());

        return new JwtAuthResponse("Bearer", accessToken, refreshToken);
    }

    /**
     * 3. 로그아웃
     * - Redis에 토큰을 블랙리스트로 등록
     */
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiryMillis;

    @Override
    public void logout(String token) {
        // Redis에 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                token,                     // Key: 토큰
                "logout",                  // Value: 상태값
                accessTokenExpiryMillis,   // TTL: 설정된 만료 시간
                TimeUnit.MILLISECONDS      // 시간 단위
        );
    }

    /**
     * 4. 비밀번호 변경
     * - 회원 정보 확인
     * - 비밀번호 확인
     * - newPassword 설정
     */
    public void changePassword(Long memberId, PasswordChangeRequestDto passwordChangeRequestDto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(passwordChangeRequestDto.getCurrentPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        if (passwordEncoder.matches(passwordChangeRequestDto.getNewPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        member.setPassword(passwordEncoder.encode(passwordChangeRequestDto.getNewPassword()));
        memberRepository.save(member);
    }

    /**
     * 5. 회원 탈퇴
     * - 현재 비밀번호 입력 후 탈퇴 진행
     */
    public void deleteMember(Long memberId,String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if(!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        member.softDelete();
        memberRepository.save(member);

        redisTemplate.delete(member.getEmail());
    }
}
