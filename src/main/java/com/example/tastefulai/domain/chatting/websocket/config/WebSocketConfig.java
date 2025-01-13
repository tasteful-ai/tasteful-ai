package com.example.tastefulai.domain.chatting.websocket.config;

//import com.example.tastefulai.domain.chatting.websocket.Interceptor.WebSocketAuthInterceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker //websocket 메시지 브로커 활성화(stomp 기반)
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

//    private final WebSocketAuthInterceptor webSocketAuthInterceptor;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //메시지 브로커 설정
        registry.enableSimpleBroker("/sub", "/queue"); //클라이언트가 메시지를 보는 경로
        registry.setApplicationDestinationPrefixes("/pub"); //메시지를 서버로 보낼 때 사용하는 경로
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-chat") //websocket 사용 시 엔드포인트
                .setAllowedOrigins("*"); //모든 도메인 허용 -> 나중에 프론트엔드 도메인 주소만 허용
//                .addInterceptors(webSocketAuthInterceptor);
//                .withSockJS();//websocket이 지원되지 않으면 SockJS 사용
//                .setSuppressCors(true);
    }

}
