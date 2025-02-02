package com.example.tastefulai.domain.chatting.websocket.config;

import com.example.tastefulai.domain.chatting.websocket.interceptor.StompHandler;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker // websocket 메시지 브로커 활성화(stomp 기반)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // 메시지 브로커 설정
        registry.enableSimpleBroker("/sub"); // 클라이언트가 메시지를 보는 경로
        registry.setApplicationDestinationPrefixes("/pub"); // 메시지를 서버로 보낼 때 사용하는 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") //websocket 사용 시 엔드포인트
                .setAllowedOrigins("http://localhost:8080", "http://localhost:3000")
                .setAllowedOriginPatterns("*")
                .withSockJS();// websocket이 지원되지 않으면 SockJS 사용
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }

}
