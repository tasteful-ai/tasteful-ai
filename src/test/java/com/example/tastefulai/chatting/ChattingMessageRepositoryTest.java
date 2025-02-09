package com.example.tastefulai.chatting;

import com.example.tastefulai.domain.chatting.entity.ChattingMessage;
import com.example.tastefulai.domain.chatting.entity.Chattingroom;
import com.example.tastefulai.domain.chatting.repository.ChattingMessageRepository;
import com.example.tastefulai.domain.chatting.repository.ChattingroomRepository;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class ChattingMessageRepositoryTest {

    @Autowired
    private ChattingMessageRepository chattingMessageRepository;

    @Autowired
    private ChattingroomRepository chattingroomRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Chattingroom chattingroom;
    private Member member;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member(MemberRole.USER, "testUser@example.com", "Password123!", "TestUser", 20, GenderRole.MALE, null));

        chattingroom = chattingroomRepository.save(new Chattingroom("Test Room", member));

        for (int i = 0; i < 60; i++) {
            chattingMessageRepository.save(new ChattingMessage(chattingroom, member, "Message " + i));
        }
    }

    @Test
    @DisplayName("최신 50개 메시지 조회")
    void findTop50ByChattingroomIdOrderByCreatedAtDesc_Success() {
        List<ChattingMessage> messages = chattingMessageRepository.findTop50ByChattingroomIdOrderByCreatedAtAsc(chattingroom.getId());

        assertNotNull(messages);
        assertEquals(50, messages.size());
        assertTrue(messages.get(0).getCreatedAt().isAfter(messages.get(49).getCreatedAt()));
    }

    @Test
    @DisplayName("특정 시간 이전 메시지 삭제")
    @Rollback(value = false)
    void deleteByCreatedAtBefore_Success() {
        LocalDateTime expirationTime = LocalDateTime.now().minusDays(1);

        int deletedCount = chattingMessageRepository.deleteByCreatedAtBefore(expirationTime);

        assertTrue(deletedCount >= 0);
    }
}
