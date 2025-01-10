package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.util.JwtProvider;
import jakarta.transaction.Transactional;
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
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final RedisTemplate<String, Object> redisTemplate;
    private static final String VERIFY_PASSWORD_KEY = "verify-password:";

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 1. 회원 가입 :
     * - 중복 닉네임 확인,
     * - 이메일 중복 여부 확인,
     * - 이메일 형식 확인,
     * - 비밀번호 패턴 확인,
     */
    public MemberResponseDto signup(String email, String password, String nickname,
                                    Integer age, GenderRole genderRole, MemberRole memberRole) {

        // 중복 닉네임 확인
        if (memberRepository.existsByNickname(nickname)) {
            throw new CustomException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }
        // 이메일 중복 여부 확인
        if (memberRepository.existsByEmail(email)) {
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE);
        }

        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member(email, encodedPassword, nickname, age, genderRole, memberRole, null);
        memberRepository.save(member);
        return new MemberResponseDto(member.getId(), member.getMemberRole(), member.getEmail(), member.getNickname());
    }

    /**
     * 2. 로그인 :
     * - 사용자 확인
     * - 비밀번호 확인
     * - 인증 객체 생성 및 유효성 확인
     */
    public JwtAuthResponse login(String email, String password) {
        // 사용자 확인
        Member member = this.memberRepository.findActiveByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 비밀번호 확인
        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        // 인증 객체 생성 및 유효성 확인
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password)
        );

        // JMT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(authentication);
        String refreshToken = jwtProvider.generateRefreshToken(email);

        return new JwtAuthResponse("Bearer", accessToken, refreshToken);
    }

    /**
     * 3. 로그아웃 :
     * - Redis 에 토큰을 블랙리스트로 등록
     */
    @Value("${jwt.access-token-expiration}")
    private long accessTokenExpiryMillis;

    @Override
    public void logout(String token) {
        // Redis 에 토큰 블랙리스트 등록
        redisTemplate.opsForValue().set(
                token,                     // Key: 토큰
                "logout",                  // Value: 상태값
                accessTokenExpiryMillis,   // TTL: 설정된 만료 시간
                TimeUnit.MILLISECONDS      // 시간 단위
        );
    }

    /**
     * 4. 비밀번호 변경 :
     * - 회원 정보 확인
     * - 비밀번호 확인
     * - newPassword 설정
     */
    public void changePassword(Long memberId, String currentPassword, String newPassword) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }

        if (passwordEncoder.matches(newPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }

        member.setPassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
    }

    /**
     * 5. 비밀번호 검증 :
     * - 회원 정보 확인
     * - 비밀번호 검증
     */
    public void verifyPassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
        // 검증 상태 저장(Redis에 저장)
        redisTemplate.opsForValue().set(VERIFY_PASSWORD_KEY + memberId, "true");
    }

    /**
     * 6. 회원 탈퇴 :
     * - 현재 비밀번호 입력 후 탈퇴 진행
     */
    public void deleteMember(Long memberId) {
        if (!isPasswordVerified(memberId)) {
            throw new CustomException(ErrorCode.VERIFY_PASSWORD_REQUIRED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));


        member.softDelete();
        memberRepository.save(member);

        // 검증 상태 제거
        clearPasswordVerification(memberId);
    }

    /**
     *
     * 7. 닉네임 수정
     */
    @Override
    @Transactional
    public ProfileResponseDto updateNickname(Long memberId, String nickname) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateNickname(nickname);

        return Member.toDto(member);
    }

    // 검증 상태 확인
    public boolean isPasswordVerified(Long memberId) {
        String isVerified = (String) redisTemplate.opsForValue().get(VERIFY_PASSWORD_KEY + memberId);
        return "true".equals(isVerified);
    }

    // 검증 상태 제거
    public void clearPasswordVerification(Long memberId) {
        redisTemplate.delete(VERIFY_PASSWORD_KEY + memberId);
    }
}
