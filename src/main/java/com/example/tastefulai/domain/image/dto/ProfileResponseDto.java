package com.example.tastefulai.domain.image.dto;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.dietarypreferences.DietaryPreferences;
import com.example.tastefulai.domain.taste.entity.dietarypreferences.TasteDietaryPreferences;
import com.example.tastefulai.domain.taste.entity.genres.Genres;
import com.example.tastefulai.domain.taste.entity.genres.TasteGenres;
import com.example.tastefulai.domain.taste.entity.likefoods.LikeFoods;
import com.example.tastefulai.domain.taste.entity.likefoods.TasteLikeFoods;
import com.example.tastefulai.domain.taste.entity.spicylevel.SpicyLevel;
import com.example.tastefulai.domain.taste.entity.spicylevel.TasteSpicyLevel;
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

    private List<String> dietaryPreferences;

    private List<Integer> tasteSpicyLevels;

    public ProfileResponseDto(String nickname,
                              String imageUrl,
                              LocalDate createdAt,
                              List<String> genres,
                              List<String> likeFoods,
                              List<String> dietaryPreferences,
                              List<Integer> tasteSpicyLevels) {
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.tasteSpicyLevels = tasteSpicyLevels;
    }

    public static ProfileResponseDto fromMember(Member member) {
        return new ProfileResponseDto(
                member.getNickname(),
                (member.getImages().isEmpty()) ? null : member.getImages().getFirst().getImageUrl(),
                member.getCreatedAt().toLocalDate(),
                member.getTasteGenres().stream().map(tg->tg.getGenres().getGenreName()).toList(),
                member.getTasteLikeFoods().stream().map(lf->lf.getLikeFoods().getLikeName()).toList(),
                member.getTasteDietaryPreferences().stream().map(dp->dp.getDietaryPreferences().getPreferenceName()).toList(),
                member.getTasteSpicyLevels().stream().map(sl->sl.getSpicyLevel().getSpicyLevel()).toList()
        );
    }
}
