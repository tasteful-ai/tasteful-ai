package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingMessageRequestDto;
import com.example.tastefulai.domain.chatting.dto.ChattingMessageResponseDto;
import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
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

    @Override
    @Transactional
    public ChattingMessageResponseDto saveMessage(String memberEmail, ChattingMessageRequestDto chattingMessageRequestDto) {
        Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();

        Member member = memberService.findByEmail(memberEmail);

        ChattingMessage chattingMessage = new ChattingMessage(chattingroom, member, chattingMessageRequestDto.getMessage());
        ChattingMessage savedMessage = chattingMessageRepository.save(chattingMessage);

        return new ChattingMessageResponseDto(savedMessage.getSenderNickname(), savedMessage.getMessage());
    }

    @Override
    public List<ChattingMessageResponseDto> getMessages() {
        Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();

        List<ChattingMessage> messages = chattingMessageRepository.findTop50ByChattingroomOrderByCreatedAtDesc(chattingroom);

        return messages.stream()
                .map(message -> new ChattingMessageResponseDto(message.getSenderNickname(), message.getMessage()))
                .collect(Collectors.toList());
    }

    @Cacheable(value = "members", key = "#memberEmail")
    public Member findMember(String memberEmail) {
        return memberService.findByEmail(memberEmail);
    }
}
