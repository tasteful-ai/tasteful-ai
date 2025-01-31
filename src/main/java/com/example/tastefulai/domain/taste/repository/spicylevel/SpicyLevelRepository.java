package com.example.tastefulai.domain.taste.repository.spicylevel;

import com.example.tastefulai.domain.taste.entity.spicylevel.SpicyLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SpicyLevelRepository extends JpaRepository<SpicyLevel, Long> {

    Optional<SpicyLevel> findBySpicyLevel(Integer spicyLevel);
}