package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.entity.Member;

public interface MemberService {

    Member findById(Long memberId);
}
