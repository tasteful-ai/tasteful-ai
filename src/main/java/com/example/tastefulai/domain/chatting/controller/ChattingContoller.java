package com.example.tastefulai.domain.chatting.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.service.ChattingService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
public class ChattingContoller {

    private final ChattingService chattingService;

    @PostMapping("/room")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<ChattingroomResponseDto>> createChattingroom(@RequestParam String roomName,
                                                                                         @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        String adminEmail = memberDetails.getUsername();
        ChattingroomResponseDto chattingroomResponseDto = chattingService.createChattingroom(roomName, adminEmail);

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 생성 성공", chattingroomResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/room/{id}")
    public ResponseEntity<CommonResponseDto<ChattingroomResponseDto>> getChattingroom(@PathVariable Long id) {

        ChattingroomResponseDto chattingroomResponseDto = chattingService.getChattingroom(id);

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 조회 성공", chattingroomResponseDto), HttpStatus.OK);
    }

    @PostMapping("/messages")
    public ResponseEntity<CommonResponseDto<ChattingMessageResponseDto>> sendMessage(@RequestBody ChattingMessageRequestDto chattingMessageRequestDto,
                                                                                     @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        String memberEmail = memberDetails.getUsername();
        ChattingMessageResponseDto chattingMessageResponseDto = chattingService.createMessage(memberEmail, chattingMessageRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 전송 성공", chattingMessageResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/room/{id}/messages")
    public ResponseEntity<CommonResponseDto<List<ChattingMessageResponseDto>>> getMessage(@PathVariable Long id) {

        List<ChattingMessageResponseDto> messages = chattingService.getMessages(id);

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 조회 성공", messages), HttpStatus.OK);
    }

}
