package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.entity.Member;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

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

    public static MemberListResponseDto fromEntity(Member member) {
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