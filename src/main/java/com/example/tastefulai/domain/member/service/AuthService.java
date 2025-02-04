package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;

public interface AuthService {

    Member findByEmail(String email);

    MemberResponseDto signup(MemberRole memberRole, String email, String password, String nickname, Integer age, GenderRole genderRole);

    JwtAuthResponse login(String email, String password);

    void logout(String token);
}
