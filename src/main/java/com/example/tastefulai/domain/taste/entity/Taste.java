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

    @Column(name = "genre", nullable = true)
    private String genre;     // 선호 음식 장르

    @Column(name = "like_food", nullable = true)
    private String likeFood;    // 좋아하는 음식

    @Column(name = "dislike_food", nullable = true)
    private String dislikeFood;   //비선호 음식 장르

    @Column(name = "dietary_preference", nullable = true)
    private String dietaryPreference; //식단 성향

    @Column(name = "spicy_level", nullable = true)
    private Integer spicyLevel;    // 매운 정도

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 생성자
    public Taste(String genre, String likeFood, String dislikeFood,
                 String dietaryPreference, Integer spicyLevel, Member member){
        this.genre = genre;
        this.likeFood = likeFood;
        this.dislikeFood = dislikeFood;
        this.dietaryPreference = dietaryPreference;
        this.spicyLevel = spicyLevel;
        this.member = member;
    }

    public void updateGenre(String newGenre) {
        if (newGenre != null) {
            this.genre = newGenre.isEmpty() ? null : newGenre; // 빈 문자열은 null로 처리
        }
    }

    public void updateLikeFood(String newLikeFood) {
        if (newLikeFood != null) {
            this.likeFood = newLikeFood.isEmpty() ? null : newLikeFood;
        }
    }

    public void updateDisLikeFood(String newDisLikeFood) {
        if (newDisLikeFood != null) {
            this.dislikeFood = newDisLikeFood.isEmpty() ? null : newDisLikeFood;
        }
    }

    public void updateDietaryPreference(String newDietaryPreference) {
        if (newDietaryPreference != null) {
            this.dietaryPreference = newDietaryPreference.isEmpty() ? null : newDietaryPreference;
        }
    }

    public void updateSpicyLevel(Integer newSpicyLevel) {
        // null을 허용하고, 1부터 5까지의 유효한 값만 받아들임
        if (newSpicyLevel == null || (newSpicyLevel >= 1 && newSpicyLevel <= 5)) {
            this.spicyLevel = newSpicyLevel;  // spicy level or null
        } else {
            throw new CustomException(ErrorCode.INVALID_SPICY_LEVEL);
        }
    }
}
