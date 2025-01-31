package com.example.tastefulai.domain.aichat.controller;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.aichat.service.AiChatService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users/aiChats")
public class AiChatController {

    private final AiChatService aiChatService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponseDto<AiChatResponseDto>> createMenuRecommendation(@RequestBody AiChatRequestDto aiChatRequestDto,
                                                                                         @AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();
        AiChatResponseDto aiChatResponseDto = aiChatService.createMenuRecommendation(aiChatRequestDto, memberId);

        return new ResponseEntity<>(
                new CommonResponseDto<>("AI 메뉴 추천 완료", aiChatResponseDto),
                HttpStatus.OK
        );
    }
}