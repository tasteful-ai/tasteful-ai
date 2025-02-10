package com.example.tastefulai.domain.member.enums;

import java.util.Collection;
import java.util.List;

/**
 * ADMIN : 관리자,
 * USER : 일반 회원
 */
public enum MemberRole {

    ADMIN,
    USER;

    public Collection<String> getAuthorities() {
        return List.of("ROLE_" + this.name());
    }
}
