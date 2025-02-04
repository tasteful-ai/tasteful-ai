package com.example.tastefulai.domain.chatting.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.service.ChattingService;
import com.example.tastefulai.global.common.dto.CommonResponseDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chatting")
public class ChattingContoller {

    private final ChattingService chattingService;

    @PostMapping("/rooms")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<ChattingroomResponseDto>> createChattingroom(@Valid @RequestBody ChattingroomRequestDto chattingroomRequestDto,
                                                                                         @AuthenticationPrincipal MemberDetailsImpl memberDetails) {

        Long adminId = memberDetails.getId();

        ChattingroomResponseDto chattingroomResponseDto = chattingService.createChattingroom(chattingroomRequestDto.getRoomName(), adminId);

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 생성 성공", chattingroomResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/rooms")
    public ResponseEntity<CommonResponseDto<List<ChattingroomResponseDto>>> getAllChattingrooms() {

        List<ChattingroomResponseDto> chattingrooms = chattingService.getAllChattingrooms();

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 목록 조회 성공", chattingrooms), HttpStatus.OK);
    }

    @DeleteMapping("/rooms/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<Void>> deleteChattingroom(@PathVariable Long roomId) {

        chattingService.deleteChattingroom(roomId);

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 삭제 완료", null), HttpStatus.OK);
    }

    @PatchMapping("/rooms/{roomId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CommonResponseDto<ChattingroomResponseDto>> updateChattingroom(@PathVariable Long roomId,
                                                                                         @Valid @RequestBody ChattingroomRequestDto chattingroomRequestDto) {

        ChattingroomResponseDto updatedChattingroom = chattingService.updateChattingroom(roomId, chattingroomRequestDto.getRoomName());

        return new ResponseEntity<>(new CommonResponseDto<>("채팅방 이름 변경 성공", updatedChattingroom), HttpStatus.OK);
    }

    @PostMapping("/rooms/{roomId}/messages")
    public ResponseEntity<CommonResponseDto<ChattingMessageResponseDto>> sendMessage(@Valid @RequestBody ChattingMessageRequestDto chattingMessageRequestDto,
                                                                                     @AuthenticationPrincipal MemberDetailsImpl memberDetails,
                                                                                     @PathVariable Long roomId) {

        Long memberId = memberDetails.getId();
        ChattingMessageResponseDto chattingMessageResponseDto = chattingService.createMessage(roomId, memberId, chattingMessageRequestDto);

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 전송 성공", chattingMessageResponseDto), HttpStatus.CREATED);
    }

    @GetMapping("/rooms/{roomId}/messages")
    public ResponseEntity<CommonResponseDto<List<ChattingMessageResponseDto>>> getMessages(@PathVariable Long roomId) {

        List<ChattingMessageResponseDto> messages = chattingService.getMessages(roomId);

        return new ResponseEntity<>(new CommonResponseDto<>("메시지 조회 성공", messages), HttpStatus.OK);
    }

}
