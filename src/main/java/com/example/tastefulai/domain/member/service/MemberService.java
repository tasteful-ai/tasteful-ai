package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;

public interface MemberService {

    Member findById(Long memberId);

    MemberResponseDto signup(String email, String password, String nickname, Integer age, GenderRole genderRole, MemberRole memberRole);

    JwtAuthResponse login(String email, String password);

    void logout(String token);

    void changePassword(String email, String currentPassword, String newPassword, String currentAccessToken);

    void verifyPassword(Long memberId, String password);

    void deleteMember(Long memberId);

    ProfileResponseDto updateNickname(Long memberId, String nickname);
}