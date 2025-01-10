package com.example.tastefulai.domain.image.controller;

import com.example.tastefulai.domain.image.dto.ProfileRequestDto;
import com.example.tastefulai.domain.image.dto.ProfileResponseDto;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members/{memberId}/profiles")
public class MemberProfileController {

    private final MemberService memberService;

    @PatchMapping
    public ResponseEntity<CommonResponseDto<ProfileResponseDto>> updateNickname(@PathVariable Long memberId,
                                                                                @RequestBody ProfileRequestDto profileRequestDto) {

        ProfileResponseDto profileResponseDto = memberService.updateNickname(memberId, profileRequestDto.getNickname());

        return new ResponseEntity<>(new CommonResponseDto<>("닉네임 변경 완료", profileResponseDto), HttpStatus.OK);
    }
}
