package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;

import java.util.List;

public interface RedisMessageService {

    void saveMessage(Long chattingroomId, ChattingMessageResponseDto chattingMessageResponseDto);

    List<ChattingMessageResponseDto> getRecentMessages(Long chattingroomId);
}
