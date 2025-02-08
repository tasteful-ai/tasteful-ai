package com.example.tastefulai.domain.image.repository;

import com.example.tastefulai.domain.image.entity.Image;
import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.member.enums.GenderRole;
import com.example.tastefulai.domain.member.enums.MemberRole;
import com.example.tastefulai.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ImageRepositoryTest {

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    MemberRepository memberRepository;

    private Member member;

    private Image image;

    @BeforeEach
    void setUp() {
        member = memberRepository.save(new Member(MemberRole.USER, "testUser@example.com", "Password123!", "TestUser", 20, GenderRole.MALE, null));

        image = imageRepository.save(new Image("image.jpg", "image/jpeg", 10L, "https://imageUrl.jpg", member));
    }

    @Test
    @DisplayName("멤버 Id로 사용자 검색 시 존재하는 이미지 반환")
    void findByMemberId_success() {

        Image foundImage = imageRepository.findByMemberId(member.getId());

        assertNotNull(foundImage);
        assertEquals(member.getId(), foundImage.getMember().getId());
        assertEquals(1L, foundImage.getId());
        assertEquals("image.jpg", foundImage.getFileName());
        assertEquals("image/jpeg", foundImage.getFileType());
        assertEquals(10L, foundImage.getFileSize());
        assertEquals("https://imageUrl.jpg", foundImage.getImageUrl());
    }

    @Test
    @DisplayName("멤버 Id로 사용자 검색 시 이미지 부재")
    void findByMemberId_noImage() {

        Image foundImage = imageRepository.findByMemberId(99L);

        assertNull(foundImage);
    }
}