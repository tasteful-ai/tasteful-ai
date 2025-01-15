package com.example.tastefulai.domain.chatting.websocket.enums;

//단일 채팅방이라도 채팅방 이름 관리(하드코딩 방지)
//= 채팅 메시지를 주고 받는 Redis 채널
public enum RedisChannel {
    CHATROOM("chatroom");

    private final String name;

    RedisChannel(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
