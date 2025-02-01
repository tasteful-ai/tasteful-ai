package com.example.tastefulai.domain.aichat.controller;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.aichat.service.AiChatService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
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

        /*
         Record의 경우 accessor가 recommendation() 이고,
         CommonResponseDto의 타입 파라미터는 AiChatResponse 입니다.
         내부 값을 꺼내서 보여주고 싶은 경우 아래의 방법으로만 가능합니다.. (컨벤션)
        */
        String recommendation = aiChatResponseDto.recommendation();
        AiChatResponseDto responseDto = new AiChatResponseDto(recommendation);

        return ResponseEntity.ok(new CommonResponseDto<>("AI 메뉴 추천 완료", responseDto));
    }

    @DeleteMapping("/clear")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<CommonResponseDto<Void>> clearChatHistory(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {
        Long memberId = memberDetailsImpl.getId();
        aiChatService.clearChatHistory(memberId);

        return ResponseEntity.ok(new CommonResponseDto<>("AI 채팅 히스토리 삭제 완료", null));
    }
}