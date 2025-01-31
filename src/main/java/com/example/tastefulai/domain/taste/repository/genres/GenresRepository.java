package com.example.tastefulai.domain.taste.repository.genres;

import com.example.tastefulai.domain.taste.entity.genres.Genres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenresRepository extends JpaRepository<Genres, Long> {

    Optional<Genres> findByGenreName(String genreName);
}
