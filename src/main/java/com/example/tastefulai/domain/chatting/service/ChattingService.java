package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ChattingService {

    ChattingroomResponseDto createChattingroom(String roomName, Long adminId);

    List<ChattingroomResponseDto> getAllChattingrooms();

    ChattingMessageResponseDto createMessage(Long chattingroomId, Long memberId, ChattingMessageRequestDto chattingMessageRequestDto);

    List<ChattingMessageResponseDto> getMessages(Long chattingroomId);

    void processReceivedMessage(ChattingMessageResponseDto chattingMessageResponseDto);
}
