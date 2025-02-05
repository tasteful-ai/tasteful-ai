package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberListResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.AdminMemberRepository;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminMemberServiceImpl implements AdminMemberService {

    private final AdminMemberRepository adminMemberRepository;

    @Qualifier("blacklistTemplate")
    private final RedisTemplate<String, String> blacklistTemplate;

    private static final String VERIFY_PASSWORD_KEY = "verify-password:";


    // 회원 탈퇴
    @Override
    @Transactional
    public void deleteMemberByAdmin(Long memberId) {

        validateAdminPermission();
        Member targetMember = findMemberById(memberId);
        validateNotAdminRoleForDeletion(targetMember);

        targetMember.softDelete();
        adminMemberRepository.save(targetMember);
        clearPasswordVerification(memberId);

    }

    /**
     * ADMIN 권한 검증
     */
    private void validateAdminPermission() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));

        // 현재 사용자가 ADMIN 권한을 가지고 있지 않으면 예외 처리
        if (!isAdmin) {
            throw new CustomException(ErrorCode.ADMIN_PERMISSION_REQUIRED);
        }
    }

    /**
     * ADMIN 권한이 아닌 경우에만 삭제 가능
     */
    private void validateNotAdminRoleForDeletion(Member targetMember) {
        if (targetMember.getMemberRole() == MemberRole.ADMIN) {
            throw new CustomException(ErrorCode.ADMIN_CANNOT_REMOVE_ADMIN);
        }
    }

    private Member findMemberById(Long memberId) {
        return adminMemberRepository.findById(memberId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));
    }

    /**
     * 검증 상태 제거
     */
    public void clearPasswordVerification(Long memberId) {
        blacklistTemplate.delete(VERIFY_PASSWORD_KEY + memberId);
    }


    //  회원 전체 조회
    @Override
    public List<MemberListResponseDto> getAllMembers() {
        return convertToMemberListResponse(adminMemberRepository.findAll());
    }

    private List<MemberListResponseDto> convertToMemberListResponse(List<Member> members) {
        return members.stream()
                .map(this::convertToMemberListResponseDto)
                .collect(Collectors.toList());
    }

    private MemberListResponseDto convertToMemberListResponseDto(Member member) {
        return new MemberListResponseDto(
                member.getId(),
                member.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")),
                member.getNickname(),
                member.getEmail(),
                member.getGenderRole().name(),
                member.getMemberRole().name(),
                (member.getDeletedAt() != null) ? member.getDeletedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")) : null
        );
    }
}
