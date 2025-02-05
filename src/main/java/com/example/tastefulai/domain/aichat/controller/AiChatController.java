package com.example.tastefulai.domain.aichat.controller;

import com.example.tastefulai.domain.aichat.dto.AiChatRequestDto;
import com.example.tastefulai.domain.aichat.dto.AiChatResponseDto;
import com.example.tastefulai.domain.aichat.service.AiChatHistoryService;
import com.example.tastefulai.domain.aichat.service.AiChatHistoryServiceImpl;
import com.example.tastefulai.domain.aichat.service.AiChatService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/aiChats")
public class AiChatController {

    private final AiChatService aiChatService;
    private final AiChatHistoryService aiChatHistoryService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<AiChatResponseDto>> createMenuRecommendation(@RequestBody AiChatRequestDto aiChatRequestDto,
                                                                                         @AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();
        AiChatResponseDto aiChatResponseDto = aiChatService.createMenuRecommendation(aiChatRequestDto, memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("AI 메뉴 추천 완료", aiChatResponseDto), HttpStatus.OK);
    }

    @GetMapping("/history")
    public ResponseEntity<CommonResponseDto<List<String>>> getChatHistory(
            @AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {

        Long memberId = memberDetailsImpl.getId();
        List<String> chatHistory = aiChatHistoryService.getChatHistory(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("AI 채팅 히스토리 조회 완료", chatHistory), HttpStatus.OK);
    }

    @DeleteMapping("/clear")
    public ResponseEntity<CommonResponseDto<String>> clearChatHistory(@AuthenticationPrincipal MemberDetailsImpl memberDetailsImpl) {
        Long memberId = memberDetailsImpl.getId();
        aiChatHistoryService.clearChatHistory(memberId);

        return new ResponseEntity<>(new CommonResponseDto<>("AI 채팅 히스토리 삭제 완료", null), HttpStatus.OK);
    }
}