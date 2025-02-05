package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class MemberListResponseDto {

    private final Long memberId;
    private final String createdAt;
    private final String nickname;
    private final String email;
    private final GenderRole genderRole;
    private final MemberRole memberRole;
    private final String deletedAt;

    public MemberListResponseDto (Long memberId, String createdAt, String nickname,
                                  String email, GenderRole genderRole, MemberRole memberRole,
                                  String deletedAt) {
        this.memberId = memberId;
        this.createdAt = createdAt;
        this.nickname = nickname;
        this.email = email;
        this.genderRole = genderRole;
        this.memberRole = memberRole;
        this.deletedAt = deletedAt;
    }

    public static MemberListResponseDto fromEntity(Member member) {
        return new MemberListResponseDto(
                member.getId(),
                member.getCreatedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")),
                member.getNickname(),
                member.getEmail(),
                member.getGenderRole(),
                member.getMemberRole(),
                (member.getDeletedAt() != null) ? member.getDeletedAt().format(DateTimeFormatter.ofPattern("yy/MM/dd")) : null
        );
    }
}