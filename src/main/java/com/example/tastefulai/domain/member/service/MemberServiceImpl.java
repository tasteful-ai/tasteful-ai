package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.ProfileResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.domain.member.validation.MemberValidation;
import com.example.tastefulai.domain.taste.dto.TasteDto;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberValidation memberValidation;
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Qualifier("blacklistTemplate")
    private final RedisTemplate<String, String> blacklistTemplate;

    private static final String VERIFY_PASSWORD_KEY = "verify-password:";
    private static final String REFRESH_TOKEN_KEY = "refreshToken:";

    private void validatePassword(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_PASSWORD);
        }
    }

    @Override
    public Member findById(Long memberId) {
        return memberRepository.findById(memberId).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }


    // 비밀번호 변경
    @Override
    @Transactional
    public void updatePassword(String email, String currentPassword, String newPassword, String currentAccessToken) {

        memberValidation.validatePasswordUpdate(currentPassword, newPassword);

        Member member = findByEmail(email);

        validatePassword(currentPassword, member.getPassword());

        member.updatePassword(passwordEncoder.encode(newPassword));
        memberRepository.save(member);
        deleteRefreshToken(email);
    }

    @Override
    public Member findByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    private void deleteRefreshToken(String email) {
        blacklistTemplate.delete(REFRESH_TOKEN_KEY + email);
    }


    // 비밀번호 검증
    @Override
    public void verifyPassword(Long memberId, String password) {

        memberValidation.validatePassword(password);

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        validatePassword(password, member.getPassword());
        savePasswordVerification(memberId);
    }

    private void savePasswordVerification(Long memberId) {
        blacklistTemplate.opsForValue().set(VERIFY_PASSWORD_KEY + memberId, "true");
    }


    // 회원 탈퇴
    @Override
    @Transactional
    public void deleteMember(Long memberId) {
        if (!isPasswordVerified(memberId)) {
            throw new CustomException(ErrorCode.VERIFY_PASSWORD_REQUIRED);
        }

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.softDelete();
        memberRepository.save(member);
        clearPasswordVerification(memberId);
    }

    /**
     * 검증 상태 확인
     */
    public boolean isPasswordVerified(Long memberId) {
        String isVerified = (String) blacklistTemplate.opsForValue().get(VERIFY_PASSWORD_KEY + memberId);
        return "true".equals(isVerified);
    }

    /**
     * 검증 상태 제거
     */
    public void clearPasswordVerification(Long memberId) {
        blacklistTemplate.delete(VERIFY_PASSWORD_KEY + memberId);
    }

    // 닉네임 수정
    @Override
    @Transactional
    public void updateNickname(Long memberId, String nickname) {

        Member member = findById(memberId);

        member.updateNickname(nickname);

        memberRepository.save(member);
    }

    // 프로필 조회
    @Override
    public ProfileResponseDto getMemberProfile(Long memberId) {

        Member member = findById(memberId);

        return ProfileResponseDto.fromMember(member);
    }

    @Override
    public Member findMemberWithTasteById(Long memberId) {
        return memberRepository.findMemberWithTasteById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public TasteDto getMemberTaste(Long memberId) {

        Member member = findMemberWithTasteById(memberId);

        List<String> genres = member.getTasteGenres().stream()
                .map(tg -> tg.getGenres().getGenreName()).toList();
        List<String> likeFoods = member.getTasteLikeFoods().stream()
                .map(tl -> tl.getLikeFoods().getLikeName()).toList();
        List<String> dislikeFoods = member.getTasteDislikeFoods().stream()
                .map(td -> td.getDislikeFoods().getDislikeName()).toList();
        List<String> dietaryPreferences = member.getTasteDietaryPreferences().stream()
                .map(tp -> tp.getDietaryPreferences().getPreferenceName()).toList();
        Integer spicyLevel = member.getTasteSpicyLevels().isEmpty() ? null :
                member.getTasteSpicyLevels().getFirst().getSpicyLevel().getSpicyLevel();

        return new TasteDto(genres, likeFoods, dislikeFoods, dietaryPreferences, spicyLevel);
    }
}