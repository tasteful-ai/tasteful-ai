package com.example.tastefulai.domain.chatting.entity;

import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ChattingMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chattingroom_id", nullable = false)
    private Chattingroom chattingroom;

    @Column(nullable = false)
    private Long memberId;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String message;

    public ChattingMessage(Chattingroom chattingroom, Long memberId, String message){
        this.chattingroom = chattingroom;
        this.memberId = memberId;
        this.message = message;
    }
}
