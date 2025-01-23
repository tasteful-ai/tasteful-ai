package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberRequestDto {

    @NotNull(message = "회원 역할을 입력해주세요.(ADMIN, USER)")
    private final MemberRole memberRole;

    @NotBlank(message = "이메일을 입력해주세요.")
    private final String email;

    @NotBlank(message = "비밀번호를 입력해주세요.")
    private final String password;

    @NotBlank(message = "닉네임을 입력해주세요.")
    @Size(max = 20, message = "닉네임은 최대 20자까지 가능합니다.")
    private final String nickname;

    @NotNull(message = "나이를 입력해주세요.")
    @Min(value = 1, message = "나이는 1 이상이어야 합니다.")
    private final Integer age;

    @NotNull(message = "성별 정보를 입력해주세요. (MALE, FEMALE, OTHER)")
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