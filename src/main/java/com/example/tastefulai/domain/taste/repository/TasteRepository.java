package com.example.tastefulai.domain.taste.repository;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.domain.taste.entity.Taste;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TasteRepository extends JpaRepository<Taste, Long> {

    Optional<Taste> findByMember(Member member); // 인증 인가 이후 수정
}
