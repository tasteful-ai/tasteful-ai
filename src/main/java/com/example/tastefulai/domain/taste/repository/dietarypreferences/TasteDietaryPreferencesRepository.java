package com.example.tastefulai.domain.taste.repository.dietarypreferences;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.dietarypreferences.TasteDietaryPreferences;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteDietaryPreferencesRepository extends JpaRepository<TasteDietaryPreferences, Long> {

    @EntityGraph(attributePaths = {"dietaryPreferences"})
    List<TasteDietaryPreferences> findByMember(Member member);
    void deleteByMember(Member member);
}
