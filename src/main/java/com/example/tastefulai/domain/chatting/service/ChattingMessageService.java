package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;

import java.util.List;

public interface ChattingMessageService {

    ChattingMessageResponseDto saveMessage(String memberEmail, ChattingMessageRequestDto chattingMessageRequestDto);

    List<ChattingMessageResponseDto> getMessages();

    void processReceivedMessage(ChattingMessageResponseDto chattingMessageResponseDto);
}
