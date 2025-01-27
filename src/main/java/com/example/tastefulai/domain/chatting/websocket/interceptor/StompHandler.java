package com.example.tastefulai.domain.chatting.websocket.interceptor;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.config.auth.MemberDetailsImpl;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final MemberRepository memberRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel messageChannel) {

        StompHeaderAccessor stompHeaderAccessor = StompHeaderAccessor.wrap(message);

        if (StompCommand.CONNECT.equals(stompHeaderAccessor.getCommand())) {
            String token = stompHeaderAccessor.getFirstNativeHeader("Authorization");

            if (token == null || !jwtProvider.validateToken(token)) {
                throw new CustomException(ErrorCode.INVALID_TOKEN);
            }

            String email = jwtProvider.getEmailFromToken(token);
            log.info("Email from Token: {}", email);

            Member member = memberRepository.findByEmail(email)
                    .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

            Authentication authentication = new UsernamePasswordAuthenticationToken(new MemberDetailsImpl(member), null, member.getAuthorities());
            log.info(member.getEmail());
            log.info("Authentication before setting SecurityContext: {}", authentication);

            SecurityContextHolder.getContext().setAuthentication(authentication);
            stompHeaderAccessor.setUser(authentication);
        }

        return message;
    }
}
