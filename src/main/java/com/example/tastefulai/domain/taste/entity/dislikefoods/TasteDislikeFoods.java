package com.example.tastefulai.domain.taste.entity.dislikefoods;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "taste_dislike_foods")
public class TasteDislikeFoods extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dislike_food_id")
    private DislikeFoods dislikeFoods;

    public TasteDislikeFoods(Member member, DislikeFoods dislikeFoods) {
        this.member = member;
        this.dislikeFoods = dislikeFoods;
    }
}
