package com.example.tastefulai.domain.chatting.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.service.ChattingMessageService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chattingmessages")
public class ChattingMessageContoller {

    private final ChattingMessageService chattingMessageService;

    @PostMapping
    public ResponseEntity<CommonResponseDto<ChattingMessageResponseDto>> sendMessage(
            Authentication authentication,
            @RequestBody ChattingMessageRequestDto chattingMessageRequestDto) {

        String memberEmail = authentication.getName();
        ChattingMessageResponseDto chattingMessageResponseDto = chattingMessageService.saveMessage(memberEmail, chattingMessageRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 전송 성공", chattingMessageResponseDto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<CommonResponseDto<List<ChattingMessageResponseDto>>> getMessages() {
        List<ChattingMessageResponseDto> messages = chattingMessageService.getMessages();

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 조회 성공", messages), HttpStatus.OK);
    }
}
