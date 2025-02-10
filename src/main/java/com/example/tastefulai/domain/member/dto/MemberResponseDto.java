package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class MemberResponseDto{

    private final Long id;
    private final MemberRole memberRole;
    private final String email;
    private final String nickname;


    public MemberResponseDto(Long id, MemberRole memberRole, String email, String nickname) {
        this.id = id;
        this.memberRole = memberRole;
        this.email = email;
        this.nickname = nickname;
    }

    public static MemberResponseDto fromEntity(Member member) {
        return new MemberResponseDto(
                member.getId(),
                member.getMemberRole(),
                member.getEmail(),
                member.getNickname()
                );
    }
}
