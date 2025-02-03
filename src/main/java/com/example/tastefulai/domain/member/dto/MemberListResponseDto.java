package com.example.tastefulai.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberListResponseDto {

    private final Long memberId;
    private final String createdAt;   // 가입일 (생성 날짜)
    private final String nickname;    // 닉네임
    private final String email;       // 이메일
    private final String gender;      // 성별
    private final String role;        // 권한
    private final String deletedAt;   // 탈퇴 날짜 (탈퇴한 경우만 값 있음)

    public MemberListResponseDto (Long memberId, String createdAt, String nickname,
                                  String email, String gender, String role,
                                  String deletedAt) {
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.deletedAt = deletedAt;
    }
}