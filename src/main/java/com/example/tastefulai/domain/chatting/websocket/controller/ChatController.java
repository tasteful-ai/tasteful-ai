package com.example.tastefulai.domain.chatting.websocket.controller;

import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ChatController {

    private final RedisTemplate<String, Object> redisTemplate;

    @MessageMapping("/chat")
    public void publishMessage(ChatMessageDto chatMessageDto) {
        //메시지가 정상적으로 수신되고 발행되는지 기록
        log.info("Received message from client: {}", chatMessageDto);

        redisTemplate.convertAndSend("chatroom", chatMessageDto);

        log.info("Published message to Redis channel: chatroom");
    }
}
