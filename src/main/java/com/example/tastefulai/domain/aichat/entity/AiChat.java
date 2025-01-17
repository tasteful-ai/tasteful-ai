package com.example.tastefulai.domain.aichat.entity;

import lombok.Getter;

@Getter
public class AiChat {

    private String id;
    private Long tasteId;
    private String recommendation;

    public AiChat(String id, Long tasteId, String recommendation) {
        this.id = id;
        this.tasteId = tasteId;
        this.recommendation = recommendation;
    }
}
