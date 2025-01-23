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
    public ChattingroomResponseDto createChattingroom(String roomName, String adminEmail) {
        Member admin = memberService.findByEmail(adminEmail);

        validateAdminRole(admin);

        Chattingroom chattingroom = new Chattingroom(roomName, admin);
        chattingroomRepository.save(chattingroom);

        return new ChattingroomResponseDto(chattingroom.getId(), chattingroom.getCreatedAt(), admin.getNickname());
    }

    @Override
    public ChattingroomResponseDto getChattingroom(Long id) {
        Chattingroom chattingroom = chattingroomRepository.findChattingroomByIdOrThrow(id);

        return new ChattingroomResponseDto(chattingroom.getId(), chattingroom.getCreatedAt(), chattingroom.getCreator().getNickname());
    }

    @Override
    @Transactional
    public ChattingMessageResponseDto createMessage(String memberEmail, ChattingMessageRequestDto chattingMessageRequestDto) {
        Member member = memberService.findByEmail(memberEmail);
        Chattingroom chattingroom = chattingroomRepository.findChattingroomByIdOrThrow(chattingMessageRequestDto.getChattingroomId());

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, member, chattingMessageRequestDto.getMessage());
        chattingMessageRepository.save(chattingMessage);

        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(
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
        Member sender = memberService.findByEmail(chattingMessageResponseDto.getSenderNickname());

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, sender, chattingMessageResponseDto.getMessage());
        chattingMessageRepository.save(chattingMessage);
    }

    private void validateAdminRole(Member admin) {
        if (!admin.getMemberRole().equals(MemberRole.ADMIN)) {
            throw new UnAuthorizedException(ErrorCode.FORBIDDEN_ADMIN_ONLY);
        }
    }
}
