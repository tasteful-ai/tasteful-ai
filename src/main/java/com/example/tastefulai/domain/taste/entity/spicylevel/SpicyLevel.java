package com.example.tastefulai.domain.taste.entity.spicylevel;

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
@Table(name = "spicy_level")
public class SpicyLevel extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spicy_level", nullable = true)
    private Integer spicyLevel;

    @OneToMany(mappedBy = "spicyLevel")
    private List<TasteSpicyLevel> tasteSpicyLevels = new ArrayList<>();

    public SpicyLevel(Integer spicyLevel) {
        this.spicyLevel = spicyLevel;
    }
}
