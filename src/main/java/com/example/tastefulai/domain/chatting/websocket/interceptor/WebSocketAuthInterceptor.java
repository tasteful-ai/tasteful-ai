package com.example.tastefulai.domain.chatting.websocket.interceptor;

import com.example.tastefulai.global.util.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
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

        if (serverHttpRequest instanceof ServletServerHttpRequest servletServerHttpRequest) {
            String token = extractTokenFromRequest(servletServerHttpRequest);

            if (token != null && jwtProvider.validateToken(token)) {
                String email = jwtProvider.getEmailFromToken(token);
                attributes.put("email", email);
                log.info("WebSocket 인증 성공: 이메일={}", email);
                return true;
            } else {
                log.warn("WebSocket 인증 실패: 유효하지 않은 토큰");
            }
        } else {
            log.warn("WebSocket 인증 실패: 서버 HTTP 요청이 아님");
        }
        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
                               WebSocketHandler webSocketHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket handshake 오류: {}", exception.getMessage());
        }
    }

    private String extractTokenFromRequest(ServletServerHttpRequest servletServerHttpRequest) {
        try {
            HttpServletRequest httpServletRequest = servletServerHttpRequest.getServletRequest();
            String token = httpServletRequest.getParameter("token"); // Body 또는 Query Parameter에서 토큰 추출
            if (token == null) {
                log.warn("요청에 토큰이 포함되지 않음");
            }
            return token;
        } catch (Exception e) {
            log.error("토큰 추출 중 오류 발생: {}", e.getMessage());
            return null;
        }
    }
}