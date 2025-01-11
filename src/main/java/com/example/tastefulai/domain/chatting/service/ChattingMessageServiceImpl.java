package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.redis.service.RedisMessageService;
import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChattingMessageServiceImpl implements ChattingMessageService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingroomRepository chattingroomRepository;
    private final MemberService memberService;
    private final RedisMessageService redisMessageService;

    @Override
    @Transactional
    public ChattingMessageResponseDto saveMessage(String memberEmail, ChattingMessageRequestDto chattingMessageRequestDto) {
        Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();

        Member member = memberService.findByEmail(memberEmail);

        //DB에 메시지 저장
        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, member, chattingMessageRequestDto.getMessage());
        chattingMessageRepository.save(chattingMessage);

        //Redis 캐싱 저장
        ChattingMessageResponseDto chattingMessageResponseDto = new ChattingMessageResponseDto(member.getNickname(), chattingMessage.getMessage());
        redisMessageService.saveMessage(chattingMessageResponseDto);

        return chattingMessageResponseDto;
    }

    @Override
    public List<ChattingMessageResponseDto> getMessages() {

        //Redis에서 메시지 조회
        List<ChattingMessageResponseDto> cachedMessages = redisMessageService.getRecentMessages();

        if (cachedMessages.isEmpty()) {
            Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();
            List<ChattingMessage> messages = chattingMessageRepository.findTop50ByChattingroomOrderByCreatedAtDesc(chattingroom);

            return messages.stream()
                    .map(message -> new ChattingMessageResponseDto(message.getSenderNickname(), message.getMessage()))
                    .collect(Collectors.toList());
        }

        return cachedMessages;
    }

    @Override
    public void processReceivedMessage(ChattingMessageResponseDto chattingMessageResponseDto) {
        Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();

        Member sender = memberService.findByEmail(chattingMessageResponseDto.getSenderNickname());

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, sender, chattingMessageResponseDto.getMessage());
        chattingMessageRepository.save(chattingMessage);

        redisMessageService.saveMessage(chattingMessageResponseDto);
    }
}
