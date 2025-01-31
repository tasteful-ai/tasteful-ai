package com.example.tastefulai.global.common.dto;

import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;

@Getter
public class JwtAuthResponse {

    private final Long memberId;
    private final MemberRole memberRole;
    private final String accessToken;
    private final String refreshToken;

    public JwtAuthResponse(Long memberId, MemberRole memberRole, String accessToken, String refreshToken) {

        this.memberId = memberId;
        this.memberRole = memberRole;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
