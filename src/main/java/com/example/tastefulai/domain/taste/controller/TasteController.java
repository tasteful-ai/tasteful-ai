package com.example.tastefulai.domain.taste.controller;

import com.example.tastefulai.domain.taste.dto.TasteRequestDto;
import com.example.tastefulai.domain.taste.dto.TasteResponseDto;
import com.example.tastefulai.domain.taste.service.TasteService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/users/members/{memberId}/tastes")
public class TasteController {

    private final TasteService tasteService;

    @PatchMapping
    public ResponseEntity<CommonResponseDto> updateUserTaste(@RequestHeader("Member-Id") Long memberId,
                                                             @RequestBody TasteRequestDto tasteRequestDto) {
        // 인증/인가 로직 토큰 검증 (인증된 사용자 정보)

        TasteResponseDto tasteResponseDto = tasteService.updateTaste(memberId, tasteRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>("카테고리 입력 완료", tasteResponseDto), HttpStatus.OK);
    }
}
