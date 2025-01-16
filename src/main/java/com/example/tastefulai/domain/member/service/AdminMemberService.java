package com.example.tastefulai.domain.member.service;

public interface AdminMemberService {

    void deleteMemberByAdmin(Long memberId);

    void updateMemberRole(Long memberId, String memberRole);
}
