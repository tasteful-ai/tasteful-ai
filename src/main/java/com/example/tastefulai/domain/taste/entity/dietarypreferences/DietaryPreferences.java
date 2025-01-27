package com.example.tastefulai.domain.taste.entity.dietarypreferences;

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
@Table(name = "dietary_preferences")
public class DietaryPreferences extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "preference_name", nullable = true)
    private String preferenceName;

    @OneToMany(mappedBy = "dietaryPreferences")
    private List<TasteDietaryPreferences> tasteDietaryPreferences = new ArrayList<>();

    public DietaryPreferences(String preferenceName) {
        this.preferenceName = preferenceName;
    }
}
