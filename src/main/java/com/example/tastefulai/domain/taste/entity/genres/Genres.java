package com.example.tastefulai.domain.taste.entity.genres;

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
@Table(name = "genres")
public class Genres extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "genre_name", nullable = true)
    private String genreName;

    @OneToMany(mappedBy = "genres")
    private List<TasteGenres> tasteGenres = new ArrayList<>();

    public Genres(String genreName) {
        this.genreName = genreName;
    }
}
