package com.example.tastefulai.domain.taste.repository.spicylevel;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.spicylevel.TasteSpicyLevel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasteSpicyLevelRepository extends JpaRepository<TasteSpicyLevel, Long> {

    @EntityGraph(attributePaths = {"spicyLevel"})
    Optional <TasteSpicyLevel> findByMember(Member member);
    void deleteByMember(Member member);
}