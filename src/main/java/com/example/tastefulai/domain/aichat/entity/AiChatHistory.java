package com.example.tastefulai.domain.aichat.entity;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ai_chat_history")
public class AiChatHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ai_chat_session_id", nullable = false)
    private String sessionId;

    @Column(name = "recommendation", nullable = false)
    private String recommendation;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(columnDefinition = "Text", nullable = true)
    private String tasteData;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public AiChatHistory(String sessionId, String recommendation, String description, String tasteData, Member member) {
        this.sessionId = sessionId;
        this.recommendation = recommendation;
        this.description = description;
        this.tasteData = tasteData;
        this.member = member;
    }
}
