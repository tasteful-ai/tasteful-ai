package com.example.tastefulai.global.common.dto;

import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;

@Getter
public class JwtAuthResponse {

    private final Long memberId;
    private final String nickname;
    private final MemberRole memberRole;
    private final String accessToken;
    private final String refreshToken;

    public JwtAuthResponse(Long memberId, String nickname, MemberRole memberRole, String accessToken, String refreshToken) {

        this.memberId = memberId;
        this.nickname = nickname;
        this.memberRole = memberRole;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
