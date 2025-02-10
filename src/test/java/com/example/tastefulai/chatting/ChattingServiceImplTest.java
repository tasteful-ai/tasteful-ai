package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.service.RedisMessageService;
import com.example.tastefulai.domain.chatting.service.RedisMessageServiceImpl;
import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.chatting.service.ChattingServiceImpl;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.AdminMemberService;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.error.exception.UnAuthorizedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChattingServiceImplTest {

    @InjectMocks
    private ChattingServiceImpl chattingService;

    @Mock
    private ChattingroomRepository chattingroomRepository;

    @Mock
    private ChattingMessageRepository chattingMessageRepository;

    @Mock
    private MemberService memberService;

    @Mock
    private AdminMemberService adminMemberService;

    @Mock
    private RedisMessageService redisMessageService;

    private Member admin;
    private Member user;
    private Chattingroom chattingroom;

    @BeforeEach
    void setUp() {
        admin = new Member(MemberRole.ADMIN, "adminUser@example.com", "Password123!", "Admin", 30, GenderRole.FEMALE, null);
        user = new Member(MemberRole.USER, "testUser@example.com", "passwOrd123@", "User", 24, GenderRole.MALE, null);
        chattingroom = new Chattingroom(1L, "Test Room", admin);
    }

    @Test
    @DisplayName("채팅방 생성 성공")
    void createChattingroom_Success() {
        Long adminId = 1L;

        doNothing().when(adminMemberService).validateAdminRole(adminId);

        when(memberService.findById(adminId)).thenReturn(admin);
        when(chattingroomRepository.findByRoomName("Test Room")).thenReturn(Optional.empty());
        when(chattingroomRepository.save(any(Chattingroom.class))).thenReturn(chattingroom);

        ChattingroomResponseDto chattingroomResponseDto = chattingService.createChattingroom("Test Room", adminId);

        assertNotNull(chattingroomResponseDto);
        assertEquals("Test Room", chattingroomResponseDto.getRoomName());
        assertEquals("Admin", chattingroomResponseDto.getCreatorNickname());

        verify(chattingroomRepository, times(1)).save(any(Chattingroom.class));
    }

    @Test
    @DisplayName("예외 - 채팅방 이름 중복")
    void createChattingroom_DuplicateName() {
        Long adminId = 1L;

        when(chattingroomRepository.findByRoomName("Test Room")).thenReturn(Optional.of(chattingroom));

        CustomException customException = assertThrows(CustomException.class,
                () -> chattingService.createChattingroom("Test Room", adminId));

        assertEquals(ErrorCode.DUPLICATE_CHATROOM_NAME, customException.getErrorCode());
    }

    @Test
    @DisplayName("예외 - 관리자 권한이 아닌 사용자가 채팅방 생성 시")
    void createChattingroom_NotAdmin() {
        Long userId = 2L;

        doThrow(new UnAuthorizedException(ErrorCode.FORBIDDEN_ADMIN_ONLY)).when(adminMemberService).validateAdminRole(userId);

        UnAuthorizedException unAuthorizedException = assertThrows(UnAuthorizedException.class,
                () -> chattingService.createChattingroom("Test Room", userId));

        assertEquals(ErrorCode.FORBIDDEN_ADMIN_ONLY, unAuthorizedException.getErrorCode());
    }

    @Test
    @DisplayName("채팅방 목록 조회 성공")
    void getAllChattingrooms_Success() {
        when(chattingroomRepository.findAll()).thenReturn(List.of(chattingroom));

        List<ChattingroomResponseDto> chattingrooms = chattingService.getAllChattingrooms();

        assertEquals(1, chattingrooms.size());
        assertEquals("Test Room", chattingrooms.get(0).getRoomName());
    }

    @Test
    @DisplayName("메시지 전송 성공")
    void createMessage_Success() {
        Long roomId = 1L;
        Long memberId = 2L;
        ChattingMessageRequestDto chattingMessageRequestDto = new ChattingMessageRequestDto("Hello");

        when(memberService.findById(memberId)).thenReturn(user);
        when(chattingroomRepository.findById(roomId)).thenReturn(Optional.of(chattingroom));

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, user, "Hello");

        when(chattingMessageRepository.save(any(ChattingMessage.class))).thenReturn(chattingMessage);

        ChattingMessageResponseDto chattingMessageResponseDto = chattingService.createMessage(roomId, memberId, chattingMessageRequestDto);

        assertNotNull(chattingMessageResponseDto);
        assertEquals("User", chattingMessageResponseDto.getSenderNickname());
        assertEquals("Hello", chattingMessageResponseDto.getMessage());

        verify(chattingMessageRepository, times(1)).save(any(ChattingMessage.class));
        verify(redisMessageService, times(1)).saveMessage(eq(roomId), any(ChattingMessageResponseDto.class));
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 채팅방")
    void createMessage_ChattingroomNotFound() {
        Long roomId = 1L;
        Long memberId = 2L;

        when(chattingroomRepository.findById(roomId))
                .thenThrow(new NotFoundException(ErrorCode.NOT_FOUND_CHATTINGROOM));

        CustomException customException = assertThrows(CustomException.class,
                () -> chattingService.createMessage(roomId, memberId, new ChattingMessageRequestDto("Hello")));

        assertEquals(ErrorCode.NOT_FOUND_CHATTINGROOM, customException.getErrorCode());
    }

    @Test
    @DisplayName("예외 - 메시지를 작성하지 않고 전송 시")
    void createMessage_EmptyMessage() {
        Long roomId = 1L;
        Long memberId = 2L;

        when(memberService.findById(memberId)).thenReturn(user);
        when(chattingroomRepository.findById(roomId)).thenReturn(Optional.of(chattingroom));

        CustomException customException = assertThrows(CustomException.class,
                () -> chattingService.createMessage(roomId, memberId, new ChattingMessageRequestDto("")));

        assertEquals(ErrorCode.INVALID_INPUT, customException.getErrorCode());
    }

    @Test
    @DisplayName("Redis에서 메시지 조회 성공")
    void getMessages_CachedMessages() {
        Long roomId = 1L;
        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(2L, "User", "Hello", roomId);

        when(redisMessageService.getRecentMessages(roomId)).thenReturn(List.of(chattingMessageResponseDto));

        List<ChattingMessageResponseDto> messages = chattingService.getMessages(roomId);

        assertFalse(messages.isEmpty());
        assertEquals(1, messages.size());
        assertEquals("Hello", messages.get(0).getMessage());
    }

    @Test
    @DisplayName("채팅방 삭제")
    void deleteChattingroom_Success() {
        Long roomId = 1L;
        when(chattingroomRepository.findById(roomId)).thenReturn(Optional.of(chattingroom));

        chattingService.deleteChattingroom(roomId);

        verify(chattingroomRepository, times(1)).delete(chattingroom);
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 채팅방")
    void deleteChattingroom_NotFound() {
        Long roomId = 99L;
        when(chattingroomRepository.findById(roomId))
                .thenThrow(new NotFoundException(ErrorCode.NOT_FOUND_CHATTINGROOM));

        assertThrows(NotFoundException.class, () -> chattingService.deleteChattingroom(roomId));
    }

    @Test
    @DisplayName("채팅방 이름 변경")
    void updateChattingroom_Success() {
        Long roomId = 1L;
        String newRoomName = "Updated Room";

        when(chattingroomRepository.findById(roomId)).thenReturn(Optional.of(chattingroom));

        ChattingroomResponseDto responseDto = chattingService.updateChattingroom(roomId, newRoomName);

        assertEquals(newRoomName, responseDto.getRoomName());
    }

    @Test
    @DisplayName("예외 - 존재하지 않는 채팅방)")
    void updateChattingroom_NotFound() {
        Long roomId = 99L;
        String newRoomName = "New Room Name";

        when(chattingroomRepository.findById(roomId))
                .thenThrow(new NotFoundException(ErrorCode.NOT_FOUND_CHATTINGROOM));

        assertThrows(NotFoundException.class, () -> chattingService.updateChattingroom(roomId, newRoomName));
    }

}
