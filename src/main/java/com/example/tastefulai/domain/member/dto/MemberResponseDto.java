package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.global.common.dto.BaseDtoType;
import lombok.Getter;

@Getter
public class MemberResponseDto implements BaseDtoType {

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
}
