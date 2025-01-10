package com.example.tastefulai.domain.taste.entity;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.BaseEntity;
import com.example.tastefulai.global.error.errorcode.ErrorCode;
import com.example.tastefulai.global.error.exception.CustomException;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "taste")
public class Taste extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genres", nullable = true)
    private String genres;     // 선호 음식 장르

    @Column(name = "like_foods", nullable = true)
    private String likeFoods;    // 좋아하는 음식

    @Column(name = "dislike_foods", nullable = true)
    private String dislikeFoods;   //비선호 음식 장르

    @Column(name = "dietary_preferences", nullable = true)
    private String dietaryPreferences; //식단 성향

    @Column(name = "spicy_level", nullable = true)
    private Integer spicyLevel;    // 매운 정도

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 생성자
    public Taste(String genres, String likeFoods, String dislikeFoods,
                 String dietaryPreferences, Integer spicyLevel, Member member){
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
        this.member = member;
    }

    public void updateGenres(String newGenres) {
        this.genres = newGenres != null && !newGenres.isEmpty() ? newGenres : null;
    }

    public void updateLikeFoods(String newLikeFoods) {
        this.likeFoods = newLikeFoods != null && !newLikeFoods.isEmpty() ? newLikeFoods : null;
    }

    public void updateDisLikeFoods(String newDisLikeFoods) {
        this.dislikeFoods = newDisLikeFoods != null && !newDisLikeFoods.isEmpty() ? newDisLikeFoods : null;
    }

    public void updateDietaryPreferences(String newDietaryPreferences) {
        this.dietaryPreferences = newDietaryPreferences != null && !newDietaryPreferences.isEmpty() ? newDietaryPreferences : null;
    }

    public void updateSpicyLevel(Integer newSpicyLevel) {
        // 1부터 5까지의 유효한 값만 받아들임
        if (newSpicyLevel == null || (newSpicyLevel >= 1 && newSpicyLevel <= 5)) {
            this.spicyLevel = newSpicyLevel;
        } else {
            throw new CustomException(ErrorCode.INVALID_SPICY_LEVEL);
        }
    }
}

