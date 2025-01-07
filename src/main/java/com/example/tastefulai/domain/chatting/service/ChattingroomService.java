package com.example.tastefulai.domain.chatting.service;

import com.example.tastefulai.domain.chatting.dto.ChattingroomResponseDto;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChattingroomService {

    private final ChattingroomRepository chattingroomRepository;

    public ChattingroomResponseDto getChattingroom() {
        Chattingroom chattingroom = chattingroomRepository.getSingleChattingroom();

        return new ChattingroomResponseDto(chattingroom.getId(), chattingroom.getCreatedAt());
    }
}
