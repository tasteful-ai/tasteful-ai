package com.example.tastefulai.domain.image.dto;

import com.example.tastefulai.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ProfileResponseDto {

    private String nickname;

    private String imageUrl;

    private LocalDate createdAt;

    private String tastes;

    public ProfileResponseDto(String nickname, String imageUrl, LocalDate createdAt, String tastes) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.tastes = tastes;
    }

    public static ProfileResponseDto from(Member member) {
        return new ProfileResponseDto(
                member.getNickname(),
                (member.getImages().isEmpty()) ? null : member.getImages().getFirst().getImageUrl(),
                member.getCreatedAt().toLocalDate(),
                "");
    }
}
