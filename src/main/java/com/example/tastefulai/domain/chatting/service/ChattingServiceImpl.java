package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.redis.service.RedisMessageService;
import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.service.MemberService;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import com.example.tastefulai.global.error.exception.NotFoundException;
import com.example.tastefulai.global.error.exception.UnAuthorizedException;
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

    @Override
    @Transactional
    public ChattingroomResponseDto createChattingroom(String roomName, Long adminId) {
        Member admin = memberService.findById(adminId);

        validateAdminRole(admin);

        if (chattingroomRepository.existsByRoomName(roomName)) {
            throw new CustomException(ErrorCode.DUPLICATE_CHATROOM_NAME);
        }

        Chattingroom chattingroom = new Chattingroom(roomName, admin);
        chattingroomRepository.save(chattingroom);

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
    public ChattingMessageResponseDto createMessage(Long chattingroomId, Long memberId, ChattingMessageRequestDto chattingMessageRequestDto) {

        Member member = memberService.findById(memberId);

        Chattingroom chattingroom = chattingroomRepository.findChattingroomByIdOrThrow(chattingroomId);

        if (chattingMessageRequestDto.getMessage() == null || chattingMessageRequestDto.getMessage().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, member, chattingMessageRequestDto.getMessage());
        chattingMessageRepository.save(chattingMessage);

        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(
                member.getId(),
                member.getNickname(),
                chattingMessage.getMessage(),
                chattingroom.getId());

        redisMessageService.saveMessage(chattingroom.getId(), chattingMessageResponseDto);
        return chattingMessageResponseDto;
    }

    @Override
    public List<ChattingMessageResponseDto> getMessages(Long chattingroomId) {
        List<ChattingMessageResponseDto> cachedMessages = redisMessageService.getRecentMessages(chattingroomId);

        if (cachedMessages.isEmpty()) {
            List<ChattingMessage> messages = chattingMessageRepository.findTop50ByChattingroomIdOrderByCreatedAtDesc(chattingroomId);

            return messages.stream()
                    .map(message -> new ChattingMessageResponseDto(
                            message.getMember().getId(),
                            message.getSenderNickname(),
                            message.getMessage(),
                            chattingroomId
                    )).collect(Collectors.toList());
        }
        return cachedMessages;
    }

    @Override
    @Transactional
    public void processReceivedMessage(ChattingMessageResponseDto chattingMessageResponseDto) {
        Chattingroom chattingroom = chattingroomRepository.findChattingroomByIdOrThrow(chattingMessageResponseDto.getChattingroomId());
        Member sender = memberService.findById(chattingMessageResponseDto.getSenderId());

        if (chattingMessageResponseDto.getMessage() == null || chattingMessageResponseDto.getMessage().trim().isEmpty()) {
            throw new CustomException(ErrorCode.INVALID_INPUT);
        }

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, sender, chattingMessageResponseDto.getMessage());
        chattingMessageRepository.save(chattingMessage);
    }

    private void validateAdminRole(Member admin) {
        if (!admin.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new UnAuthorizedException(ErrorCode.FORBIDDEN_ADMIN_ONLY);
        }
    }
}
