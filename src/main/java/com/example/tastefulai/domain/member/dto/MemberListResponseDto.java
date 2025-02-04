package com.example.tastefulai.domain.member.dto;

import lombok.Getter;

@Getter
public class MemberListResponseDto {

    private final Long memberId;
    private final String createdAt;
    private final String nickname;
    private final String email;
    private final String gender;
    private final String role;
    private final String deletedAt;

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