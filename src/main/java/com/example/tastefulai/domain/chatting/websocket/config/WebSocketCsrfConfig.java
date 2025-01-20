//package com.example.tastefulai.domain.chatting.websocket.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.messaging.Message;
//import org.springframework.messaging.MessageChannel;
//import org.springframework.messaging.support.ChannelInterceptor;
//
//@Configuration
//public class WebSocketCsrfConfig {
//
//    @Bean(name = "csrfChannelInterceptor")
//    public ChannelInterceptor csrfChannelInterceptor() {
//        return new ChannelInterceptor() {
//            @Override
//            public Message<?> preSend(Message<?> message, MessageChannel channel) {
//                return message;
//            }
//        };
//    }
//}
