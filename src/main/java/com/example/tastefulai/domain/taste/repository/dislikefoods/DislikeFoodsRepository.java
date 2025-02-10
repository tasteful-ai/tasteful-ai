package com.example.tastefulai.domain.taste.repository.dislikefoods;

import com.example.tastefulai.domain.taste.entity.dislikefoods.DislikeFoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DislikeFoodsRepository extends JpaRepository<DislikeFoods, Long> {

    Optional<DislikeFoods> findByDislikeName (String dislikeName);
}