package com.example.tastefulai.domain.image.entity;

import com.example.tastefulai.domain.member.entity.Member;
import com.example.tastefulai.global.common.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import org.jetbrains.annotations.VisibleForTesting;

@Getter
@Entity
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String fileType;

    @Column(nullable = false)
    private Long fileSize;

    @Column(nullable = false)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, unique = true)
    private Member member;

    @VisibleForTesting
    public Image(String fileName, String fileType, Long fileSize, String imageUrl) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.imageUrl = imageUrl;
    }

    public Image() {
    }

    @VisibleForTesting
    public Image(String fileName, String fileType, Long fileSize, String imageUrl, Member member) {
        this.fileName = fileName;
        this.fileType = fileType;
        this.fileSize = fileSize;
        this.imageUrl = imageUrl;
        this.member = member;
    }

    public void updateMember(Member member) {
        this.member = member;
    }
}
