package com.example.tastefulai.domain.taste.repository.genres;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.genres.TasteGenres;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteGenresRepository extends JpaRepository<TasteGenres, Long> {

    List<TasteGenres> findByMember(Member member);
    void deleteByMember(Member member);
}
