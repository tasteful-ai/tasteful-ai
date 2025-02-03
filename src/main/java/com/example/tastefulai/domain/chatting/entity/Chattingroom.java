package com.example.tastefulai.domain.chatting.entity;

import com.example.tastefulai.domain.member.entity.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.VisibleForTesting;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "chattingroom")
public class Chattingroom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String roomName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id", nullable = false)
    private Member creator;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public Chattingroom(String roomName, Member creator) {
        this.roomName = roomName;
        this.creator = creator;
    }

    public void updateRoomName(String newRoomName) {
        this.roomName = newRoomName;
    }

    @VisibleForTesting
    public Chattingroom(Long id, String roomName, Member creator) {
        this.id = id;
        this.roomName = roomName;
        this.creator = creator;
    }
}
