package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.image.repository.ImageRepository;
import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;
import com.example.tastefulai.global.common.service.RedisService;
import com.example.tastefulai.global.config.SignUpValidation;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.util.JwtProvider;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final SignUpValidation signUpValidation;
    private final MemberRepository memberRepository;
    private final ImageRepository imageRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisService redisService;
    private static final String VERIFY_PASSWORD_KEY = "verify-password:";
    private static final String REFRESH_TOKEN_KEY = "refreshToken:";

    // 1. 회원가입
    public MemberResponseDto signup(MemberRole memberRole, String email, String password, String nickname,
                                    Integer age, GenderRole genderRole) {
        // 유효성 검사
        MemberRequestDto memberRequestDto = new MemberRequestDto(memberRole, email, password, nickname, age, genderRole);
        signUpValidation.validateMemberRequest(memberRequestDto, memberRepository);

        // 비밀번호 암호화 및 회원 생성
        String encodedPassword = passwordEncoder.encode(password);
        Member member = new Member(memberRole, email, encodedPassword, nickname, age, genderRole, null);
        memberRepository.save(member);

        return new MemberResponseDto(member.getId(), member.getMemberRole(), member.getEmail(), member.getNickname());
    }

    // 2. 로그인
    public JwtAuthResponse login(String email, String password) {
        // 사용자 확인
        Member member = this.memberRepository.findActiveByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        // 비밀번호 확인
        validatePassword(password, member.getPassword());

        // JWT 토큰 생성
        String accessToken = jwtProvider.generateAccessToken(email);
        String refreshToken = jwtProvider.generateRefreshToken(email);

        // RefreshToken을 Redis에 저장
        storeRefreshToken(email, refreshToken);

        return new JwtAuthResponse(accessToken, refreshToken);
    }


    // 3. 로그아웃
    @Override
    public void logout(String token) {
        // AccessToken 블랙리스트 등록
        redisTemplate.opsForValue().set(
                "blacklist:" + token,
                "invalid",
                jwtProvider.getAccessTokenExpiryMillis(),
                TimeUnit.MILLISECONDS
        );
        log.info("AccessToken 블랙리스트 등록 완료: {}", token);
    }


    // 4. 비밀번호 변경
    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword, String currentAccessToken) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        // 현재 비밀번호 검증
        validatePassword(currentPassword, member.getPassword());

        if (passwordEncoder.matches(newPassword, member.getPassword())) {
            throw new CustomException(ErrorCode.PASSWORD_SAME_AS_OLD);
        }
        // 비밀번호 변경
        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);

        removeRefreshToken(email);
    }

    // 5. 비밀번호 검증
    public void verifyPassword(Long memberId, String password) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(password, member.getPassword())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
        // 검증 상태 저장(Redis에 저장)
        redisTemplate.opsForValue().set(VERIFY_PASSWORD_KEY + memberId, "true");
    }


    // 6. 회원 탈퇴
    @Transactional
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


    // 7. 닉네임 수정
    @Transactional
    public ProfileResponseDto updateNickname(Member member, String nickname) {

        member.updateNickname(nickname);

        memberRepository.save(member);

        String imageUrl = imageRepository.findImageUrlByMemberId(member.getId());

        return new ProfileResponseDto(member, imageUrl);
    }

    public ProfileResponseDto getMemberProfile(Member member) {

        String imageUrl = imageRepository.findImageUrlByMemberId(member.getId());

        return new ProfileResponseDto(member, imageUrl);
    }

    // **** 공통 메서드 **** //

    // 검증 상태 확인
    public boolean isPasswordVerified(Long memberId) {
        String isVerified = (String) redisTemplate.opsForValue().get(VERIFY_PASSWORD_KEY + memberId);
        return "true".equals(isVerified);
    }

    // 검증 상태 제거
    public void clearPasswordVerification(Long memberId) {
        redisTemplate.delete(VERIFY_PASSWORD_KEY + memberId);
    }

    // 비밀번호 검증 공통 로직
    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
    }

    private void storeRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForValue().set(
                REFRESH_TOKEN_KEY + email,
                refreshToken,
                jwtProvider.getRefreshTokenExpiryMillis(),
                TimeUnit.MILLISECONDS
        );
    }

    private void removeRefreshToken(String email) {
        redisTemplate.delete(REFRESH_TOKEN_KEY + email);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

}
