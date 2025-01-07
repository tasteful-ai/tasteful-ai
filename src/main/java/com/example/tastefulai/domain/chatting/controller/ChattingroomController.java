package com.example.tastefulai.domain.chatting.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.service.ChattingroomService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user/chattingroom")
public class ChattingroomController {

    private final ChattingroomService chattingroomService;

    @GetMapping
    public ResponseEntity<CommonResponseDto<ChattingroomResponseDto>> getChattingroom() {
        ChattingroomResponseDto chattingroomResponseDto = chattingroomService.getChattingroom();

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 조회 성공", chattingroomResponseDto), HttpStatus.OK);
    }
}
