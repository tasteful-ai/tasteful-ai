//package com.example.tastefulai.domain.chatting.websocket.Interceptor;
//
//import com.example.tastefulai.global.util.JwtProvider;
//import jakarta.servlet.http.Cookie;
//import jakarta.servlet.http.HttpServletRequest;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServerHttpResponse;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.HandshakeInterceptor;
//
//import java.util.Map;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class WebSocketAuthInterceptor implements HandshakeInterceptor {
//
//    private final JwtProvider jwtProvider;
//
//    @Override
//    public boolean beforeHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
//                                   WebSocketHandler webSocketHandler, Map<String, Object> attributes) {
//
//        if (serverHttpRequest instanceof ServletServerHttpRequest) {
//            ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) serverHttpRequest;
//            String token = extractTokenFromCookie(((ServletServerHttpRequest) serverHttpRequest).getServletRequest());
//
//            if (token != null && jwtProvider.validateToken(token)) {
//                String email = jwtProvider.getEmailFromToken(token);
//                attributes.put("email", email);
//                log.info("WebSocket 인증 성공: 이메일={}", email);
//                return true;
//            }
//        }
//        log.warn("WebSocket 인증 실패: {}", serverHttpRequest.getHeaders());
//        return false;
//    }
//
//    @Override
//    public void afterHandshake(ServerHttpRequest serverHttpRequest, ServerHttpResponse serverHttpResponse,
//                               WebSocketHandler webSocketHandler, Exception exception) {
//
//        if (exception != null) {
//            log.error("WebSocket handshake 오류: {}", exception.getMessage());
//        }
//    }
//
//    private String extractTokenFromCookie(HttpServletRequest httpServletRequest) {
//        if (httpServletRequest.getCookies() != null) {
//            for (Cookie cookie : httpServletRequest.getCookies()) {
//                if ("access-token".equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//}
