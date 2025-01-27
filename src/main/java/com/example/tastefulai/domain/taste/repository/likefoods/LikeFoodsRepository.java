package com.example.tastefulai.domain.taste.repository.likefoods;

import com.example.tastefulai.domain.taste.entity.likefoods.LikeFoods;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikeFoodsRepository extends JpaRepository<LikeFoods, Long> {

    Optional<LikeFoods> findByLikeName(String genreName);
}
