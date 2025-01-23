package com.example.tastefulai.domain.image.repository;

import com.example.tastefulai.domain.image.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {

    String findImageUrlByMemberId(Long memberId);

    Image findByMemberId(Long memberId);
}
