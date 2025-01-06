package com.example.tastefulai.domain.chatting.repository;

import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {
}
