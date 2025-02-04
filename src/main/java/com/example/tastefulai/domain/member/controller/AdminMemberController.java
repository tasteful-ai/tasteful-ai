package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.dto.MemberListResponseDto;
import com.example.tastefulai.domain.member.service.AdminMemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    // 1. 회원 삭제 - ADMIN 전용
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteMemberByAdmin(@PathVariable Long memberId) {
        adminMemberService.deleteMemberByAdmin(memberId);

        return ResponseEntity.noContent().build();
    }


    // 2. 회원 전체 조회 - ADMIN 전용
    @GetMapping
    public ResponseEntity<CommonResponseDto<List<MemberListResponseDto>>> getAllMembers() {
        List<MemberListResponseDto> members = adminMemberService.getAllMembers();

        return new ResponseEntity<>(new CommonResponseDto<>("회원 목록 조회 완료", members), HttpStatus.OK);
    }
}
