package com.example.tastefulai.domain.chatting.repository;

import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChattingroomRepository extends JpaRepository<Chattingroom, Long> {

    Optional<Chattingroom> findByRoomName(String roomName);
}
