//package com.example.tastefulai.chatting;
//
//import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
//import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
//import com.example.tastefulai.domain.chatting.websocket.controller.WebSocketChatController;
//import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
//import com.example.tastefulai.domain.chatting.websocket.enums.MessageType;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//
//class WebSocketChatControllerTest {
//
//    @Mock
//    private RedisPublisher redisPublisher;
//
//    @InjectMocks
//    private WebSocketChatController webSocketChatController;
//
//    @Test
//    void publishMessageTest() {
//        //NullPointerException 방지를 위해 mock 객체 초기화 -> injectMocks 필드가 null
//        MockitoAnnotations.openMocks(this);
//
//        ChatMessageDto chatMessageDto = new ChatMessageDto(MessageType.TALK, "testUser", "hello");
//
//        webSocketChatController.publishMessage(chatMessageDto);
//
//        verify(redisPublisher, times(1)).publishMessage(any(ChattingMessageResponseDto.class));
//    }
//}
