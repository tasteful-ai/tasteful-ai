package com.example.tastefulai.domain.chatting.redis;

import org.springframework.stereotype.Component;

@Component
public class RedisSubscriber {

    //redis 채널에서 수신한 메시지 처리
    public void handleMessage(String message) {
        System.out.println("받은 메시지:" + message);
    }
}
