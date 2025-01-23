package com.example.tastefulai.domain.image.dto;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.Taste;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ProfileResponseDto {

    private String nickname;

    private String imageUrl;

    private LocalDate createdAt;

    private List<Taste> tastes;

    public ProfileResponseDto(String nickname, String imageUrl, LocalDate createdAt, List<Taste> tastes) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.tastes = tastes;
    }

    public ProfileResponseDto(Member member, String imageUrl) {
        this.nickname = member.getNickname();
        this.imageUrl = imageUrl;
        this.createdAt = member.getCreatedAt().toLocalDate();
        this.tastes = member.getTastes();
    }
}
