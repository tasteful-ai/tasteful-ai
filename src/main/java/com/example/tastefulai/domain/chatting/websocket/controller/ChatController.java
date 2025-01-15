package com.example.tastefulai.domain.chatting.websocket.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisPublisher redisPublisher;

    //WebSocket을 통해 클라이언트로부터 메시지 수신
    @MessageMapping("/chat")
    @SendToUser("/sub/chat")
    public ChattingMessageResponseDto publishMessage(ChatMessageDto chatMessageDto) {
        //메시지가 정상적으로 수신되고 발행되는지 기록
        log.info("Received message from client: {}", chatMessageDto);

        redisPublisher.publishMessage(new ChattingMessageResponseDto(chatMessageDto.getSender(), chatMessageDto.getMessage()));

        return new ChattingMessageResponseDto(chatMessageDto.getSender(), chatMessageDto.getMessage());
    }
}
