//package com.example.tastefulai.chatting;
//
//import com.example.tastefulai.domain.chatting.websocket.config.WebSocketConfig;
//import org.junit.jupiter.api.Test;
//import org.springframework.messaging.simp.config.MessageBrokerRegistry;
//import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
//import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
//import org.springframework.web.socket.config.annotation.StompWebSocketEndpointRegistration;
//
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//@EnableWebSocketMessageBroker
//class WebSocketConfigTest {
//
//    @Test
//    void testWebSocketConfig() {
//        WebSocketConfig webSocketConfig = new WebSocketConfig();
//
//        StompEndpointRegistry endpointRegistry = mock(StompEndpointRegistry.class);
//        StompWebSocketEndpointRegistration registration = mock(StompWebSocketEndpointRegistration.class);
//        MessageBrokerRegistry brokerRegistry = mock(MessageBrokerRegistry.class);
//
//        when(endpointRegistry.addEndpoint("/ws-chat")).thenReturn(registration);
//        when(registration.setAllowedOrigins("localhost:8080")).thenReturn(registration);
//
//        webSocketConfig.registerStompEndpoints(endpointRegistry);
//        verify(endpointRegistry, times(1)).addEndpoint("/ws-chat");
//        verify(registration, times(1)).setAllowedOrigins("localhost:8080");
//        verify(registration, times(1)).withSockJS();
//
//        webSocketConfig.configureMessageBroker(brokerRegistry);
//        verify(brokerRegistry, times(1)).setApplicationDestinationPrefixes("/pub");
//        verify(brokerRegistry, times(1)).enableSimpleBroker("/sub");
//    }
//
//}
