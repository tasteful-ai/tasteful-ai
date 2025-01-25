package com.example.tastefulai.domain.taste.entity.dislikefoods;

import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "dislike_foods")
public class DislikeFoods extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "dislike_name", nullable = true)
    private String dislike_name;

    @OneToMany(mappedBy = "dislike_foods")
    private List<TasteDislikeFoods> tasteDislikeFoods = new ArrayList<>();
}
