package com.example.tastefulai.domain.member.dto;

import com.example.tastefulai.domain.member.entity.Member;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ProfileResponseDto {

    private String nickname;

    private String imageUrl;

    private LocalDate createdAt;

    private List<String> genres;

    private List<String> likeFoods;

    private List<String> dislikeFoods;

    private List<String> dietaryPreferences;

    private List<Integer> spicyLevels;

    public ProfileResponseDto(String nickname,
                              String imageUrl,
                              LocalDate createdAt,
                              List<String> genres,
                              List<String> likeFoods,
                              List<String> dislikeFoods,
                              List<String> dietaryPreferences,
                              List<Integer> spicyLevels) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevels = spicyLevels;
    }

    public static ProfileResponseDto fromMember(Member member) {
        return new ProfileResponseDto(
                member.getNickname(),
                (member.getImages().isEmpty()) ? null : member.getImages().getFirst().getImageUrl(),
                member.getCreatedAt().toLocalDate(),
                member.getTasteGenres().stream().map(tg->tg.getGenres().getGenreName()).toList(),
                member.getTasteLikeFoods().stream().map(lf->lf.getLikeFoods().getLikeName()).toList(),
                member.getTasteDislikeFoods().stream().map(df->df.getDislikeFoods().getDislikeName()).toList(),
                member.getTasteDietaryPreferences().stream().map(dp->dp.getDietaryPreferences().getPreferenceName()).toList(),
                member.getTasteSpicyLevels().stream().map(sl->sl.getSpicyLevel().getSpicyLevel()).toList()
        );
    }
}
