package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;
import com.example.tastefulai.domain.member.entity.Member;

public interface MemberService {

    Member findById(Long memberId);
    MemberResponseDto signup(MemberRequestDto memberRequestDto);
}