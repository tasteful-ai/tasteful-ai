package com.example.tastefulai.domain.taste.repository.dietarypreferences;

import com.example.tastefulai.domain.taste.entity.dietarypreferences.DietaryPreferences;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DietaryPreferencesRepository extends JpaRepository<DietaryPreferences, Long> {

    Optional<DietaryPreferences> findByPreferenceName(String genreName);
}
