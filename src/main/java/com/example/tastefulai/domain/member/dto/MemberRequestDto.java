package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRequestDto {

    private MemberRole memberRole;
    private String email;
    private String password;
    private String nickname;
    private Integer age;
    private GenderRole genderRole;

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
