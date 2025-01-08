package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.LoginRequestDto;
import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.dto.JwtAuthResponse;

public interface MemberService {

    Member findById(Long memberId);
    MemberResponseDto signup(MemberRequestDto memberRequestDto);
    JwtAuthResponse login(LoginRequestDto loginRequestDto);
    void logout(String token);
}