package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.ProfileResponseDto;
import com.example.tastefulai.domain.member.entity.Member;

public interface MemberService {

    Member findByEmail(String email);

    Member findById(Long memberId);

    void updatePassword(String email, String currentPassword, String newPassword, String currentAccessToken);

    void verifyPassword(Long memberId, String password);

    void deleteMember(Long memberId);

    void updateNickname(Long memberId, String nickname);

    ProfileResponseDto getMemberProfile(Long memberId);
}