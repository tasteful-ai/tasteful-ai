package com.example.tastefulai.domain.chatting.repository;

import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

    List<ChattingMessage> findTop50ByChattingroomIdOrderByCreatedAtDesc(Long chattingroomId);

    @Transactional
    int deleteByCreatedAtBefore(LocalDateTime dateTime);
}
