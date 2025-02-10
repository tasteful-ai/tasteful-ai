package com.example.tastefulai.domain.taste.dto;

import lombok.Getter;

import java.util.List;

/**
 * 회원의 음식 취향 정보를 담는 DTO.
 *
 * <p>AI에게 음식 추천을 요청할 때, 회원의 취향 데이터를 포함하여 전달하기 위해 사용됨.
 * 선호하는 장르, 좋아하는 음식, 선호하지 않는 음식, 식단 성향, 매운맛 선호도를 포함
 */
@Getter
public class TasteDto {

    private List<String> genres;
    private List<String> likeFoods;
    private List<String> dislikeFoods;
    private List<String> dietaryPreferences;
    private Integer spicyLevel;

    /**
     * 회원의 음식 취향 정보를 저장하는 DTO 생성자.
     *
     * @param genres 회원이 선호하는 음식 장르 목록
     * @param likeFoods 회원이 좋아하는 음식 목록
     * @param dislikeFoods 회원이 선호하지 않는 음식 목록
     * @param dietaryPreferences 회원의 식단 성향 목록
     * @param spicyLevel 회원이 선호하는 매운맛 정도 (정수 값)
     */
    public TasteDto(List<String> genres,
                            List<String> likeFoods,
                            List<String> dislikeFoods,
                            List<String> dietaryPreferences,
                            Integer spicyLevel) {
        this.genres = genres;
        this.likeFoods = likeFoods;
        this.dislikeFoods = dislikeFoods;
        this.dietaryPreferences = dietaryPreferences;
        this.spicyLevel = spicyLevel;
    }
}
