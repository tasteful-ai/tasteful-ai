package com.example.tastefulai.domain.chatting.websocket.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.messaging.SessionConnectEvent;

import java.util.Objects;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final RedisPublisher redisPublisher;

    // WebSocket을 통해 클라이언트로부터 메시지 수신
    @MessageMapping("/chat")
    public void publishMessage(@AuthenticationPrincipal MemberDetailsImpl memberDetails, ChatMessageDto chatMessageDto, SessionConnectEvent event) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(event.getMessage(), StompHeaderAccessor.class);

        Authentication authentication = (Authentication) Objects.requireNonNull(accessor.getUser());
        String username = authentication.getName();

        if (memberDetails == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        String sender = memberDetails.getUsername();
        log.info("메시지 발신자: {}", sender);

        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(sender, chatMessageDto.getMessage(), chatMessageDto.getChattingroomId());
        redisPublisher.publishMessage(chatMessageDto.getChattingroomId(), chattingMessageResponseDto);
        log.info("Redis 메시지 전송 완료");
    }
}
