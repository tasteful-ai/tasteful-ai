package com.example.tastefulai.domain.chatting.repository;

import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.NotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingroomRepository extends JpaRepository<Chattingroom, Long> {

    default Chattingroom findChattingroomByIdOrThrow(Long id) {
        return findById(id).orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND));
    }

    boolean existsByRoomName(String roomName);
}
