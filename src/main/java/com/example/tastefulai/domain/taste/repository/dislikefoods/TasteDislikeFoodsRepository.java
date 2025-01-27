package com.example.tastefulai.domain.taste.repository.dislikefoods;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.dislikefoods.TasteDislikeFoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteDislikeFoodsRepository extends JpaRepository<TasteDislikeFoods, Long> {

    List<TasteDislikeFoods> findByMember(Member member);
    void deleteByMember(Member member);
}