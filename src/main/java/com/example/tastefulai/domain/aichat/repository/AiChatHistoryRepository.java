package com.example.tastefulai.domain.aichat.repository;

import com.example.tastefulai.domain.aichat.entity.AiChatHistory;
import com.example.tastefulai.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AiChatHistoryRepository extends JpaRepository<AiChatHistory, Long> {

    List<AiChatHistory> findByMemberIdOrderByCreatedAtDesc(Long memberId);

    void deleteByMember(Member member);
}
