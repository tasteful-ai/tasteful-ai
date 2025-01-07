package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberRequestDto;
import com.example.tastefulai.domain.member.dto.MemberResponseDto;

public interface MemberService {

    MemberResponseDto signup(MemberRequestDto memberRequestDto);

}
