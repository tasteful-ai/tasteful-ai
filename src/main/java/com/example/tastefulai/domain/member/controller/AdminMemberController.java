package com.example.tastefulai.domain.member.controller;

import com.example.tastefulai.domain.member.service.AdminMemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admins/members")
@RequiredArgsConstructor
public class AdminMemberController {

    private final AdminMemberService adminMemberService;

    // 회원 삭제 - 관리자용
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{memberId}")
    public ResponseEntity<CommonResponseDto<Void>> deleteMemberByAdmin(@PathVariable Long memberId) {
        adminMemberService.deleteMemberByAdmin(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("회원 삭제 완료",null), HttpStatus.OK);
    }
}
