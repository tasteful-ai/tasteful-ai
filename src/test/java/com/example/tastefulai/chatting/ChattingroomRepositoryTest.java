package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ChattingroomRepositoryTest {

    @Autowired
    private ChattingroomRepository chattingroomRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Chattingroom chattingroom;
    private Member admin;

    @BeforeEach
    void setUp() {
        admin = memberRepository.save(new Member(MemberRole.ADMIN, "adminUser@example.com", "Password123!", "Admin", 27, GenderRole.FEMALE, null));

        chattingroom = chattingroomRepository.save(new Chattingroom("Test Room", admin));
    }

    @Test
    @DisplayName("채팅방 찾기 - 성공")
    void findChattingroomById_Success() {
        Optional<Chattingroom> foundChattingroom = chattingroomRepository.findById(chattingroom.getId());

        assertThat(foundChattingroom).isPresent();
        assertThat(foundChattingroom.get().getRoomName()).isEqualTo("Test Room");
    }

    @Test
    @DisplayName("채팅방 찾기 - 존재하지 않음")
    void findChattingroomById_NotFound() {
        Optional<Chattingroom> foundChattingroom = chattingroomRepository.findById(999L);

        assertThat(foundChattingroom).isEmpty();
    }

    @Test
    @DisplayName("채팅방 이름 존재 여부 확인")
    void existsByRoomName() {
        Optional<Chattingroom> foundChattingroom = chattingroomRepository.findByRoomName("Test Room");

        assertThat(foundChattingroom).isPresent();
        assertThat(foundChattingroom.get().getRoomName()).isEqualTo("Test Room");
    }
}
