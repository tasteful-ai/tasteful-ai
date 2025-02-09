package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.AdminMemberService;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingServiceImpl implements ChattingService {

    private final ChattingroomRepository chattingroomRepository;
    private final ChattingMessageRepository chattingMessageRepository;
    private final MemberService memberService;
    private final RedisMessageService redisMessageService;
    private final AdminMemberService adminMemberService;

    @Override
    public Chattingroom findChattingroomById(Long roomId) {
        return chattingroomRepository.findById(roomId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_CHATTINGROOM));
    }

    @Override
    @Transactional
    public ChattingroomResponseDto createChattingroom(String roomName, Long adminId) {
        adminMemberService.validateAdminRole(adminId);

        Member admin = memberService.findById(adminId);

        if (chattingroomRepository.findByRoomName(roomName).isPresent()) {
            throw new CustomException(ErrorCode.DUPLICATE_CHATROOM_NAME);
        }

        Chattingroom chattingroom = chattingroomRepository.save(new Chattingroom(roomName, admin));
        return new ChattingroomResponseDto(chattingroom.getId(), chattingroom.getRoomName(), admin.getNickname(), chattingroom.getCreatedAt());
    }

    @Override
    public List<ChattingroomResponseDto> getAllChattingrooms() {

        return chattingroomRepository.findAll().stream()
                .map(ChattingroomResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteChattingroom(Long roomId) {

        Chattingroom chattingroom = findChattingroomById(roomId);
        chattingroomRepository.delete(chattingroom);
    }

    @Override
    @Transactional
    public ChattingroomResponseDto updateChattingroom(Long roomId, String newRoomName) {

        Chattingroom chattingroom = findChattingroomById(roomId);
        chattingroom.updateRoomName(newRoomName);

        return new ChattingroomResponseDto(chattingroom.getId(), chattingroom.getRoomName(), chattingroom.getCreator().getNickname(), chattingroom.getCreatedAt());
    }

    @Override
    @Transactional
    public ChattingMessageResponseDto createMessage(Long roomId, Long memberId, ChattingMessageRequestDto chattingMessageRequestDto) {

        Member member = memberService.findById(memberId);

        Chattingroom chattingroom = findChattingroomById(roomId);

        if (chattingMessageRequestDto.getMessage() == null || chattingMessageRequestDto.getMessage().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, member, chattingMessageRequestDto.getMessage());
        chattingMessageRepository.save(chattingMessage);

        ChattingMessageResponseDto chattingMessageResponseDto = ChattingMessageResponseDto.fromEntity(chattingMessage);

        redisMessageService.saveMessage(chattingroom.getId(), chattingMessageResponseDto);
        return chattingMessageResponseDto;
    }

    @Override
    public List<ChattingMessageResponseDto> getMessages(Long chattingroomId) {
        List<ChattingMessageResponseDto> cachedMessages = redisMessageService.getRecentMessages(chattingroomId);

        if (cachedMessages == null || cachedMessages.isEmpty()) {
            List<ChattingMessage> messages = chattingMessageRepository.findTop50ByChattingroomIdOrderByCreatedAtAsc(chattingroomId);

            return messages.stream()
                    .map(ChattingMessageResponseDto::fromEntity)
                    .collect(Collectors.toList());
        }
        return cachedMessages;
    }

    @Override
    @Transactional
    public void processReceivedMessage(ChattingMessageResponseDto chattingMessageResponseDto) {
        Chattingroom chattingroom = findChattingroomById(chattingMessageResponseDto.getChattingroomId());
        Member sender = memberService.findById(chattingMessageResponseDto.getSenderId());

        if (chattingMessageResponseDto.getMessage() == null || chattingMessageResponseDto.getMessage().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, sender, chattingMessageResponseDto.getMessage());
        chattingMessageRepository.save(chattingMessage);
    }
}
