package com.example.tastefulai.domain.chatting.websocket.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(
            MessageMatcherDelegatingAuthorizationManager.Builder messages) {

        messages
                .nullDestMatcher().authenticated()
                .simpDestMatchers("/pub/**").authenticated()
                .simpSubscribeDestMatchers("/sub/**").authenticated()
                .simpTypeMatchers(MESSAGE).authenticated()
                .simpTypeMatchers(SUBSCRIBE).authenticated()
                .anyMessage().denyAll();

        return messages.build();
    }

    @Bean
    protected boolean sameOriginDisabled() {
        return true;
    }
}
