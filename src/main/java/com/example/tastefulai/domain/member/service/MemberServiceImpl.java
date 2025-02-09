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

    @Override
    @Transactional
    public void updateNickname(Long memberId, String nickname) {

        Member member = findById(memberId);

        member.updateNickname(nickname);

        memberRepository.save(member);
    }

    @Override
    public ProfileResponseDto getMemberProfile(Long memberId) {

        Member member = findById(memberId);

        return ProfileResponseDto.fromMember(member);
    }

    @Override
    public Member findMemberWithTasteById(Long memberId) {
        return memberRepository.findMemberWithTasteGenresById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    @Override
    @Transactional(readOnly = true)
    public TasteDto getMemberTaste(Long memberId) {

        Member member = findMemberWithTasteById(memberId);

        member = memberRepository.findMemberWithTasteLikeFoodsByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member = memberRepository.findMemberWithTasteDislikeFoodsByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member = memberRepository.findMemberWithTasteDietaryPreferencesByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        member = memberRepository.findMemberWithTasteSpicyLevelsByMember(member)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return new TasteDto(
                member.getTasteGenres().stream().map(tg -> tg.getGenres().getGenreName()).toList(),
                member.getTasteLikeFoods().stream().map(tl -> tl.getLikeFoods().getLikeName()).toList(),
                member.getTasteDislikeFoods().stream().map(td -> td.getDislikeFoods().getDislikeName()).toList(),
                member.getTasteDietaryPreferences().stream().map(tp -> tp.getDietaryPreferences().getPreferenceName()).toList(),
                member.getTasteSpicyLevels().isEmpty() ? null : member.getTasteSpicyLevels().getFirst().getSpicyLevel().getSpicyLevel()
        );
    }
}


//    @Override
//    public Member findMemberWithTasteById(Long memberId) {
//        return memberRepository.findMemberWithTasteGenresById(memberId)
//                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
//    }
//
//    @Override
//    @Transactional(readOnly = true)
//    public TasteDto getMemberTaste(Long memberId) {
//
//        Member member = findMemberWithTasteById(memberId);
//
//        List<Member> members = List.of(member);
//        memberRepository.findMemberWithOtherTastesByMembers(members);
//
//        return new TasteDto(
//                member.getTasteGenres().stream().map(tg -> tg.getGenres().getGenreName()).toList(),
//                member.getTasteLikeFoods().stream().map(tl -> tl.getLikeFoods().getLikeName()).toList(),
//                member.getTasteDislikeFoods().stream().map(td -> td.getDislikeFoods().getDislikeName()).toList(),
//                member.getTasteDietaryPreferences().stream().map(tp -> tp.getDietaryPreferences().getPreferenceName()).toList(),
//                member.getTasteSpicyLevels().isEmpty() ? null : member.getTasteSpicyLevels().getFirst().getSpicyLevel().getSpicyLevel()
//        );
//    }
//}