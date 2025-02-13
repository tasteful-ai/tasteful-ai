package com.example.tastefulai.domain.chatting.websocket.controller;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class WebSocketChatController {

    private final RedisPublisher redisPublisher;
    private final JwtProvider jwtProvider;
    private final MemberService memberService;

    @MessageMapping("/chat")
    public void publishMessage(ChatMessageDto chatMessageDto) {

        String token = chatMessageDto.getToken();
        
        if (token == null || !token.startsWith("Bearer ")) {
            log.error("WebSocket 메시지에서 JWT 토큰이 올바르지 않음: '{}'", token);
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        token = token.substring(7).trim();
        log.info("Extracted Token after Trim: '{}'", token);

        if (!jwtProvider.validateToken(token)) {
            log.error("JWT 검증 실패");
            throw new CustomException(ErrorCode.INVALID_TOKEN);
        }

        String email = jwtProvider.getEmailFromToken(token);
        log.info("토큰에서 추출된 이메일: {}", email);

        Member sender = memberService.findByEmail(email);

        if (!sender.getNickname().equals(chatMessageDto.getSender())) {
            throw new CustomException(ErrorCode.UNAUTHORIZED_MEMBER);
        }

        log.info("메시지 발신자: {}", chatMessageDto.getSender());

        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(
                sender.getId(),
                sender.getNickname(),
                chatMessageDto.getMessage(),
                chatMessageDto.getChattingroomId());

        redisPublisher.publishMessage(chatMessageDto.getChattingroomId(), chattingMessageResponseDto);
        log.info("Redis 메시지 전송 완료");
    }
}
