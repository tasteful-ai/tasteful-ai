package com.example.tastefulai.domain.member.service;

import com.example.tastefulai.domain.member.dto.MemberListResponseDto;

import java.util.List;

public interface AdminMemberService {

    void deleteMemberByAdmin(Long memberId);

    List<MemberListResponseDto> getAllMembers();

    void validateAdminRole(Long memberId);

}
