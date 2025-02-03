package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import com.example.tastefulai.global.error.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
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
    @DisplayName("채팅방 ID로 찾기")
    void findChattingroomById_Success() {
        Chattingroom foundChattingroom = chattingroomRepository.findChattingroomByIdOrThrow(chattingroom.getId());

        assertThat(foundChattingroom).isNotNull();
        assertThat(foundChattingroom.getRoomName()).isEqualTo("Test Room");
    }

    @Test
    @DisplayName("예외 - 없는 채팅방 ID")
    void findChattingroomByIdOrThrow_NotFound() {
        assertThrows(NotFoundException.class, () -> {
            chattingroomRepository.findChattingroomByIdOrThrow(999L);
        });
    }

    @Test
    @DisplayName("예외 - 존재하는 채팅방 이름")
    void existsByRoomName_Exists() {

        boolean exists = chattingroomRepository.existsByRoomName("Test Room");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("예외 -존재하지 않는 채팅방 이름")
    void existsByRoomName_NotExists() {

        boolean exists = chattingroomRepository.existsByRoomName("Nonexistent Room");

        assertThat(exists).isFalse();
    }
}
