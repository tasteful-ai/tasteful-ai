package com.example.tastefulai.domain.chatting.websocket.Interceptor;

import com.example.tastefulai.global.util.JwtProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketAuthInterceptor implements HandshakeInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) {

        String token = extractToken(serverHttpRequest);

        if (token != null && jwtProvider.validateToken(token)) {
            String email = jwtProvider.getEmailFromToken(token);
            attributes.put("email", email);
            log.info("WebSocket 인증 성공: 이메일={}", email);
            return true;
        }
        log.warn("WebSocket 인증 실패: {}", token);
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception exception) {

        if (exception != null) {
            log.error("WebSocket handshake 오류: {}", exception.getMessage());
        }
    }

    private String extractToken(ServerHttpRequest serverHttpRequest) {
        //Authorization 헤더에서 추출
        String header = serverHttpRequest.getHeaders().getFirst("Authorization");
        if (header != null && header.startsWith("Bearer")) {
            return header.substring(7);
        }

        //URL 에서 추출
        if (serverHttpRequest instanceof ServletServerHttpRequest servletServerHttpRequest) {
            String token = servletServerHttpRequest.getServletRequest().getParameter("token");

            if (token != null && token.startsWith("Bearer ")) {
                return token.substring(7);
            }
        }

        return null;
    }
}
