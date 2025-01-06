package com.example.tastefulai.domain.taste.entity;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "taste")
public class Taste extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ElementCollection
    @CollectionTable(name = "taste_genres", joinColumns = @JoinColumn(name = "taste_id"))
    @Column
    private List<String> genre;     // 선호 음식 장르

    @ElementCollection
    @CollectionTable(name = "taste_like_food", joinColumns = @JoinColumn(name = "taste_id"))
    @Column
    private List<String> likeFood;    // 좋아하는 음식

    @ElementCollection
    @CollectionTable(name = "taste_dislike_food", joinColumns = @JoinColumn(name = "taste_id"))
    @Column
    private List<String> dislikeFood;   //비선호 음식 장르

    @ElementCollection
    @CollectionTable(name = "dietaryPreference", joinColumns = @JoinColumn(name = "taste_id"))
    @Column
    private List<String> dietaryPreference; //식단 성향

    @ElementCollection
    @CollectionTable(name = "spicy_level", joinColumns = @JoinColumn(name = "taste_id"))
    @Column
    private List<String> spicyLevel;    // 매운 정도

    // 연관 관계
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    // 생성자
    public Taste(List<String> genre, List<String> likeFood,
                 List<String> dietaryPreference, List<String> spicyLevel, Member member){
        this.genre = genre;
        this.likeFood = likeFood;
        this.dietaryPreference = dietaryPreference;
        this.spicyLevel = spicyLevel;
        this.member = member;
    }

}
