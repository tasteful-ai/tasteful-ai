package com.example.tastefulai.domain.taste.repository.likefoods;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.likefoods.TasteLikeFoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TasteLikeFoodsRepository extends JpaRepository<TasteLikeFoods, Long> {

    List<TasteLikeFoods> findByMember(Member member);
    void deleteByMember(Member member);
}