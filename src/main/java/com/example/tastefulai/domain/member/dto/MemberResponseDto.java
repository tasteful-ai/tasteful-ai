package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.global.common.dto.BaseDtoType;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberResponseDto implements BaseDtoType {

    private Long id;
    private MemberRole memberRole;
    private String email;
    private String nickname;

    public MemberResponseDto(Long id, MemberRole memberRole, String email, String nickname) {
        this.id = id;
        this.memberRole = memberRole;
        this.email = email;
        this.nickname = nickname;
    }
}
