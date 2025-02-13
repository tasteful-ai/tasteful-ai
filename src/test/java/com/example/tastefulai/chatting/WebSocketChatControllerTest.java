package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.redis.RedisPublisher;
import com.example.tastefulai.domain.chatting.websocket.controller.WebSocketChatController;
import com.example.tastefulai.domain.chatting.websocket.dto.ChatMessageDto;
import com.example.tastefulai.domain.chatting.websocket.enums.MessageType;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.util.JwtProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WebSocketChatControllerTest {

    @InjectMocks
    private WebSocketChatController webSocketChatController;

    @Mock
    private RedisPublisher redisPublisher;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private MemberService memberService;

    private static final String VALID_TOKEN = "valid_token";
    private static final String INVALID_TOKEN = "invalid_token";
    private static final String TEST_EMAIL = "testUser@example.com";
    private static final Long CHATROOM_ID = 1L;
    private static final String MESSAGE_CONTENT = "Hello";
    private static final String SENDER_NAME = "User";

    private Member member;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        member = new Member(MemberRole.USER, "testUser@example.com", "passwOrd123@", "User", 24, GenderRole.MALE, null);
    }

    @Test
    @DisplayName("WebSocket 메시지 정상 처리")
    void publishMessage_Success() {
        when(jwtProvider.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(VALID_TOKEN)).thenReturn(TEST_EMAIL);
        when(memberService.findByEmail(TEST_EMAIL)).thenReturn(member);

        ChatMessageDto chatMessageDto = new ChatMessageDto(
                MessageType.TALK, SENDER_NAME, MESSAGE_CONTENT, CHATROOM_ID, "Bearer " + VALID_TOKEN);

        assertDoesNotThrow(() -> webSocketChatController.publishMessage(chatMessageDto));

        verify(redisPublisher, times(1)).publishMessage(anyLong(), any(ChattingMessageResponseDto.class));
    }

    @Test
    @DisplayName("예외 - 잘못된 토큰")
    void publishMessage_InvalidToken() {
        when(jwtProvider.validateToken(INVALID_TOKEN)).thenReturn(false);
        when(jwtProvider.getEmailFromToken(INVALID_TOKEN)).thenThrow(new CustomException(ErrorCode.INVALID_TOKEN));

        ChatMessageDto chatMessageDto = new ChatMessageDto(
                MessageType.TALK, SENDER_NAME, MESSAGE_CONTENT, CHATROOM_ID, "Bearer " + INVALID_TOKEN);

        CustomException customException = assertThrows(CustomException.class,
                () -> webSocketChatController.publishMessage(chatMessageDto));

        assertEquals(ErrorCode.INVALID_TOKEN, customException.getErrorCode());
        verify(redisPublisher, never()).publishMessage(anyLong(), any(ChattingMessageResponseDto.class));
    }

    @Test
    @DisplayName("예외 - 메시지를 보내는 사람이 일치하지 않을 경우")
    void publishMessage_UnauthorizedSender() {
        when(jwtProvider.validateToken(VALID_TOKEN)).thenReturn(true);
        when(jwtProvider.getEmailFromToken(VALID_TOKEN)).thenReturn(TEST_EMAIL);

        Member differentMember = new Member(MemberRole.USER, "wrong@example.com", "password", "WrongUser", 30, GenderRole.FEMALE, null);

        when(memberService.findByEmail(TEST_EMAIL)).thenReturn(differentMember);

        ChatMessageDto chatMessageDto = new ChatMessageDto(
                MessageType.TALK, "WrongSender", MESSAGE_CONTENT, CHATROOM_ID, "Bearer " + VALID_TOKEN);

        CustomException customException = assertThrows(CustomException.class,
                () -> webSocketChatController.publishMessage(chatMessageDto));

        assertEquals(ErrorCode.UNAUTHORIZED_MEMBER, customException.getErrorCode());
        verify(redisPublisher, never()).publishMessage(anyLong(), any(ChattingMessageResponseDto.class));
    }

}
