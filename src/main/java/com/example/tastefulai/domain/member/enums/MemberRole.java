package com.example.tastefulai.domain.member.enums;

import java.util.Collection;
import java.util.List;

public enum MemberRole {

    /**
     * ADMIN : 관리자,
     * USER : 일반 회원,
     * OWNER : 사업자
     */
    ADMIN,
    USER,
    OWNER;

    public Collection<String> getAuthorities() {
        return List.of("ROLE_" + this.name());
    }

}
