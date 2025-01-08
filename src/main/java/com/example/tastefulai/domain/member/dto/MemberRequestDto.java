package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberRequestDto {

    private final MemberRole memberRole;
    private final String email;
    private final String password;
    private final String nickname;
    private final Integer age;
    private final GenderRole genderRole;

    public MemberRequestDto(MemberRole memberRole, String email, String password,
                            String nickname, Integer age, GenderRole genderRole) {
        this.memberRole = memberRole;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.age = age;
        this.genderRole = genderRole;
    }
}