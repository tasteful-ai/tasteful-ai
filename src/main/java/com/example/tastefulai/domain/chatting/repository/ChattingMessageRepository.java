package com.example.tastefulai.domain.chatting.repository;

import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

    List<ChattingMessage> findTop50ByChattingroomOrderByCreatedAtDesc(Chattingroom chattingroom);
}
